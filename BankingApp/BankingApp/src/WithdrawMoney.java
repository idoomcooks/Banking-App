import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class WithdrawMoney extends JFrame implements ActionListener {
    JTextField amountText;
    JButton withdrawButton;
    Connection connection;
    String username;

    public WithdrawMoney(String username) {
        this.username = username;  // Store the logged-in username
        connectToDatabase();

        setTitle("Withdraw Money");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel amountLabel = new JLabel("Amount to withdraw:");
        add(amountLabel);

        amountText = new JTextField(10);
        add(amountText);

        withdrawButton = new JButton("Withdraw");
        add(withdrawButton);
        withdrawButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        double amount = Double.parseDouble(amountText.getText());
        if (withdrawAmount(username, amount)) {
            JOptionPane.showMessageDialog(this, "Withdrawal Successful!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Withdrawal failed. Check your balance.");
        }
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "baller6969");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed.");
        }
    }

    private boolean withdrawAmount(String username, double amount) {
        try {
            String query = "UPDATE users SET balance = balance - ? WHERE username = ? AND balance >= ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setDouble(1, amount);
            pst.setString(2, username);
            pst.setDouble(3, amount);

            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
