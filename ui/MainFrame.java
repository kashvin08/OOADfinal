package shared.ui;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("University Parking Management System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Admin Dashboard", new AdminPanel());
        tabbedPane.addTab("Reports", new ReportingPanel());

        add(tabbedPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}