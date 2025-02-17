import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainDashboard extends JFrame {
    String username;
    JMenuBar menuBar;
    JMenu settingsMenu, visitBanksMenu;

    public MainDashboard(String username) {
        this.username = username;
        setTitle("Banking System Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up the background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("/logo.jpg");  // Assuming logo.jpg is in the src or resources folder
        backgroundPanel.setLayout(new BorderLayout());

        // Menu bar setup
        menuBar = new JMenuBar();
        setUpMenus();
        
        // Display welcome message
        JLabel welcomeLabel = new JLabel("Welcome to ONLINE BANKING ", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);  // Adjust color if needed
        backgroundPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Set the content pane
        setContentPane(backgroundPanel);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private void setUpMenus() {
        // Menu for financial transactions
        JMenu transactionMenu = new JMenu("Transactions");
        JMenuItem depositItem = new JMenuItem("Deposit Money");
        JMenuItem withdrawItem = new JMenuItem("Withdraw Money");
        JMenuItem transferItem = new JMenuItem("Transfer Money");
        JMenuItem checkBalanceItem = new JMenuItem("Check Balance");

        depositItem.addActionListener(e -> new DepositMoney(username));
        withdrawItem.addActionListener(e -> new WithdrawMoney(username));
        transferItem.addActionListener(e -> new TransferMoney(username));
        checkBalanceItem.addActionListener(e -> new CheckBalance(username));

        transactionMenu.add(depositItem);
        transactionMenu.add(withdrawItem);
        transactionMenu.add(transferItem);
        transactionMenu.add(checkBalanceItem);
        menuBar.add(transactionMenu);

        // Settings Menu
        settingsMenu = new JMenu("Settings");
        JMenuItem changeDetails = new JMenuItem("Change Username/Password/DOB/Email");
        
        // Add action listener for the settings menu item
        changeDetails.addActionListener(e -> {
            // Open the settings dialog or frame for changing user details
            new Settings(username);
        });
        
        settingsMenu.add(changeDetails);
        menuBar.add(settingsMenu);

        // Visit Other Banks Menu
        visitBanksMenu = new JMenu("Visit Other Banks");
        JMenuItem sbiMenuItem = new JMenuItem("Visit SBI");
        JMenuItem hdfcMenuItem = new JMenuItem("Visit HDFC Bank");
        JMenuItem kotakMenuItem = new JMenuItem("Visit Kotak Mahindra Bank");

        sbiMenuItem.addActionListener(e -> openWebpage("https://www.onlinesbi.sbi"));
        hdfcMenuItem.addActionListener(e -> openWebpage("https://www.hdfc.com"));
        kotakMenuItem.addActionListener(e -> openWebpage("https://www.kotak.com"));

        visitBanksMenu.add(sbiMenuItem);
        visitBanksMenu.add(hdfcMenuItem);
        visitBanksMenu.add(kotakMenuItem);
        menuBar.add(visitBanksMenu);
    }

    private void openWebpage(String urlString) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(urlString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Custom JPanel class to draw the background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                // Load image as a resource
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
        // This is just for testing. In real use, you would launch this after logging in.
        new MainDashboard("TestUser");
    }
}
