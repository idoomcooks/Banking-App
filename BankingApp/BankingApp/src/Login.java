import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Login extends JFrame implements ActionListener {
    // Declare components
    JTextField userText;
    JPasswordField passText;
    JButton loginButton, registerButton;
    Connection connection;

    public Login() {
        // Establish MySQL Connection
        connectToDatabase();

        // Create the custom background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("/login2.jpg");  // Use your image path here
        backgroundPanel.setLayout(null);
        
        // Create login form UI components on top of background panel
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 30);
        userLabel.setForeground(Color.black);  // Optional: Set text color for contrast
        backgroundPanel.add(userLabel);

        userText = new JTextField();
        userText.setBounds(150, 50, 150, 30);
        backgroundPanel.add(userText);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 30);
        passLabel.setForeground(Color.black);
        backgroundPanel.add(passLabel);

        passText = new JPasswordField();
        passText.setBounds(150, 100, 150, 30);
        backgroundPanel.add(passText);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 30);
        loginButton.addActionListener(this);
        backgroundPanel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(150, 200, 100, 30);
        registerButton.addActionListener(e -> {
            new Register();  // Open Register form
            this.dispose();  // Close Login form
        });
        backgroundPanel.add(registerButton);

        // Set up the main frame
        setTitle("Banking System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set the content pane to the background panel
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = userText.getText();
        String password = new String(passText.getPassword());

        if (authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new MainDashboard(username);  // Pass username to MainDashboard
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
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

    private boolean authenticateUser(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Custom JPanel for the background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(getClass().getResource(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Background image not found at " + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Draw the image, scaled to fit the JPanel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
