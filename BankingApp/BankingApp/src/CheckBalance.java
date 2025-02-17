import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class CheckBalance extends JFrame {
    private String username;
    private JLabel balanceLabel;

    public CheckBalance(String username) {
        this.username = username;
        setTitle("Check Balance");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        double balance = getUserBalance();
        balanceLabel = new JLabel("Your balance: $" + balance);
        add(balanceLabel);

        setVisible(true);
    }

    private double getUserBalance() {
        double balance = 0.0;
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "(your sql password)");

            String query = "SELECT balance FROM users WHERE username = ?";
            pst = connection.prepareStatement(query);
            pst.setString(1, username);

            rs = pst.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            } else {
                JOptionPane.showMessageDialog(this, "User not found in the database.");
            }

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found. Make sure it's added to the project dependencies.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database. Please check the database URL, username, or password.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return balance;
    }
}
