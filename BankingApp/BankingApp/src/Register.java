import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Register extends JFrame implements ActionListener {
    JTextField userText, dobText, emailText, phoneText;
    JPasswordField passText;
    JButton registerButton;
    Connection connection;

    public Register() {
        // Establish MySQL Connection
        connectToDatabase();

        setTitle("Banking System - Register");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 20, 100, 30);
        add(userLabel);

        userText = new JTextField();
        userText.setBounds(150, 20, 150, 30);
        add(userText);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 70, 100, 30);
        add(passLabel);

        passText = new JPasswordField();
        passText.setBounds(150, 70, 150, 30);
        add(passText);

        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        dobLabel.setBounds(50, 120, 200, 30);
        add(dobLabel);

        dobText = new JTextField();
        dobText.setBounds(250, 120, 100, 30);
        add(dobText);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 170, 100, 30);
        add(emailLabel);

        emailText = new JTextField();
        emailText.setBounds(150, 170, 150, 30);
        add(emailText);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(50, 220, 100, 30);
        add(phoneLabel);

        phoneText = new JTextField();
        phoneText.setBounds(150, 220, 150, 30);
        add(phoneText);

        registerButton = new JButton("Register");
        registerButton.setBounds(150, 270, 100, 30);
        add(registerButton);
        registerButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = userText.getText();
        String password = new String(passText.getPassword());
        String dob = dobText.getText();
        String email = emailText.getText();
        String phone = phoneText.getText();

        if (registerUser(username, password, dob, email, phone)) {
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            new Login();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Username may already exist.");
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

    private boolean registerUser(String username, String password, String dob, String email, String phone) {
        try {
            String query = "INSERT INTO users (username, password, dob, email, phone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, dob);
            pst.setString(4, email);
            pst.setString(5, phone);

            int rowsInserted = pst.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        new Register();
    }
}
