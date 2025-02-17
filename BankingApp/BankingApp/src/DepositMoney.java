import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class DepositMoney extends JFrame implements ActionListener {
    JTextField amountText;
    JButton depositButton;
    Connection connection;
    String username;

    public DepositMoney(String username) {
        this.username = username;  // Store the logged-in username
        connectToDatabase();

        setTitle("Deposit Money");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel amountLabel = new JLabel("Amount to deposit:");
        add(amountLabel);

        amountText = new JTextField(10);
        add(amountText);

        depositButton = new JButton("Deposit");
        add(depositButton);
        depositButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        double amount = Double.parseDouble(amountText.getText());
        if (depositAmount(username, amount)) {
            JOptionPane.showMessageDialog(this, "Deposit Successful!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Deposit failed.");
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

    private boolean depositAmount(String username, double amount) {
        try {
            String query = "UPDATE users SET balance = balance + ? WHERE username = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setDouble(1, amount);
            pst.setString(2, username);

            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
