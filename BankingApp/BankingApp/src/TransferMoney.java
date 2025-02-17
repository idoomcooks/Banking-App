import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class TransferMoney extends JFrame implements ActionListener {
    JTextField amountText, recipientText;
    JButton transferButton;
    Connection connection;
    String username;

    public TransferMoney(String username) {
        this.username = username;  // Store the logged-in username
        connectToDatabase();

        setTitle("Transfer Money");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel recipientLabel = new JLabel("Recipient Username:");
        add(recipientLabel);
        recipientText = new JTextField(15);
        add(recipientText);

        JLabel amountLabel = new JLabel("Amount to transfer:");
        add(amountLabel);
        amountText = new JTextField(10);
        add(amountText);

        transferButton = new JButton("Transfer");
        add(transferButton);
        transferButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        double amount = Double.parseDouble(amountText.getText());
        String recipient = recipientText.getText();
        if (transferAmount(username, recipient, amount)) {
            JOptionPane.showMessageDialog(this, "Transfer Successful!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Transfer failed. Check the recipient username or your balance.");
        }
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "(your sql password)");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed.");
        }
    }

    private boolean transferAmount(String sender, String recipient, double amount) {
        try {
            // Begin transaction
            connection.setAutoCommit(false);
            // Withdraw from sender
            String withdrawQuery = "UPDATE users SET balance = balance - ? WHERE username = ? AND balance >= ?";
            PreparedStatement withdrawPst = connection.prepareStatement(withdrawQuery);
            withdrawPst.setDouble(1, amount);
            withdrawPst.setString(2, sender);
            withdrawPst.setDouble(3, amount);
            int rowsWithdrawn = withdrawPst.executeUpdate();

            // Deposit to recipient
            String depositQuery = "UPDATE users SET balance = balance + ? WHERE username = ?";
            PreparedStatement depositPst = connection.prepareStatement(depositQuery);
            depositPst.setDouble(1, amount);
            depositPst.setString(2, recipient);
            int rowsDeposited = depositPst.executeUpdate();

            if (rowsWithdrawn > 0 && rowsDeposited > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
        return false;
    }
}
