import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class TransactionHistory extends JFrame {
    Connection connection;

    public TransactionHistory(String username) {
        connectToDatabase();
        setTitle("Transaction History");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea transactionArea = new JTextArea();
        transactionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transactionArea);
        add(scrollPane, BorderLayout.CENTER);

        String history = getTransactionHistory(username);
        transactionArea.setText(history);

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

    private String getTransactionHistory(String username) {
        StringBuilder history = new StringBuilder();
        try {
            String query = "SELECT * FROM transactions WHERE username = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                history.append("Date: ").append(rs.getString("date"))
                       .append(", Amount: ").append(rs.getDouble("amount"))
                       .append(", Type: ").append(rs.getString("type")).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return history.toString();
    }
}
