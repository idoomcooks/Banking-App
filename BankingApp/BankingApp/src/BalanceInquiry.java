import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class BalanceInquiry extends JFrame {
    JLabel balanceLabel;
    Connection connection;

    public BalanceInquiry(String username) {
        connectToDatabase();
        setTitle("Balance Inquiry");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        double balance = getUserBalance(username);
        balanceLabel = new JLabel("Your balance: $" + balance);
        add(balanceLabel);

        setVisible(true);
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

    private double getUserBalance(String username) {
        try {
            String query = "SELECT balance FROM users WHERE username = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }
}
