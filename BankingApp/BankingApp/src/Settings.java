import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Settings extends JFrame implements ActionListener {
    JTextField userText, dobText, emailText;
    JPasswordField passText;
    JButton saveButton;
    Connection connection;
    String username;

    public Settings(String username) {
        this.username = username;
        connectToDatabase();

        setTitle("Settings");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel userLabel = new JLabel("New Username:");
        add(userLabel);
        userText = new JTextField(15);
        add(userText);

        JLabel passLabel = new JLabel("New Password:");
        add(passLabel);
        passText = new JPasswordField(15);
        add(passText);

        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        add(dobLabel);
        dobText = new JTextField(15);
        add(dobText);

        JLabel emailLabel = new JLabel("Email:");
        add(emailLabel);
        emailText = new JTextField(15);
        add(emailText);

        saveButton = new JButton("Save Changes");
        add(saveButton);
        saveButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String newUsername = userText.getText();
        String newPassword = new String(passText.getPassword());
        String newDOB = dobText.getText();
        String newEmail = emailText.getText();

        if (updateUserDetails(username, newUsername, newPassword, newDOB, newEmail)) {
            JOptionPane.showMessageDialog(this, "Changes Saved!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
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

    private boolean updateUserDetails(String oldUsername, String newUsername, String password, String dob, String email) {
        try {
            String query = "UPDATE users SET username = ?, password = ?, dob = ?, email = ? WHERE username = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, newUsername);
            pst.setString(2, password);
            pst.setString(3, dob);
            pst.setString(4, email);
            pst.setString(5, oldUsername);

            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
