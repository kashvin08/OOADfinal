package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logic.*;

public class AdminPanel extends JPanel implements ActionListener {

    // Components defined as class fields so we can access them in actionPerformed
    private JLabel lblRevenue, lblOccupancy, lblVehicleCount, lblTotalFines, lblUnpaidFines;
    private JButton btnRefresh, btnUpdateScheme, btnViewFines;
    private JRadioButton rbSchemeA, rbSchemeB, rbSchemeC;
    private ButtonGroup fineGroup;
    
    // Finance integration
    private FinancialManager financialManager;

    public AdminPanel() {
        // Initialize financial manager
        this.financialManager = new FinancialManager();
        // Set default scheme
        this.financialManager.setFineScheme(new FineSchemeB());
        
        setLayout(new BorderLayout());


        JLabel header = new JLabel("Administrative Dashboard", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // 3 rows, 1 col
        
        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Live Statistics"));

        statsPanel.add(new JLabel("Total Revenue:"));
        lblRevenue = new JLabel("$0.00");
        lblRevenue.setForeground(new Color(0, 102, 0)); // Dark Green
        statsPanel.add(lblRevenue);

        statsPanel.add(new JLabel("Occupancy Rate:"));
        lblOccupancy = new JLabel("0%");
        statsPanel.add(lblOccupancy);

        statsPanel.add(new JLabel("Vehicles Parked:"));
        lblVehicleCount = new JLabel("0");
        statsPanel.add(lblVehicleCount);

        centerPanel.add(statsPanel);
        
        // Fine Statistics Panel
        JPanel fineStatsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        fineStatsPanel.setBorder(BorderFactory.createTitledBorder("Fine Statistics"));
        
        fineStatsPanel.add(new JLabel("Total Fines Issued:"));
        lblTotalFines = new JLabel("0");
        lblTotalFines.setForeground(new Color(200, 0, 0));
        fineStatsPanel.add(lblTotalFines);
        
        fineStatsPanel.add(new JLabel("Total Unpaid Amount:"));
        lblUnpaidFines = new JLabel("$0.00");
        lblUnpaidFines.setForeground(new Color(200, 0, 0));
        fineStatsPanel.add(lblUnpaidFines);
        
        centerPanel.add(fineStatsPanel);
        // Fine Management Panel
        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Fine Scheme Management"));

        settingsPanel.add(new JLabel("Select Fine Scheme:"));

        rbSchemeA = new JRadioButton("Option A: Flat Rate ($50)");
        rbSchemeB = new JRadioButton("Option B: Progressive (1x, 2x, 3x)");
        rbSchemeC = new JRadioButton("Option C: Percentage (50% per 30min)");

        fineGroup = new ButtonGroup();
        fineGroup.add(rbSchemeA);
        fineGroup.add(rbSchemeB);
        fineGroup.add(rbSchemeC);

        rbSchemeB.setSelected(true); // Default to Progressive

        settingsPanel.add(rbSchemeA);
        settingsPanel.add(rbSchemeB);
        settingsPanel.add(rbSchemeC);

        btnUpdateScheme = new JButton("Apply Scheme");
        btnUpdateScheme.addActionListener(this);
        settingsPanel.add(btnUpdateScheme);
        
        btnViewFines = new JButton("View Unpaid Fines");
        btnViewFines.addActionListener(this);
        settingsPanel.add(btnViewFines);

        centerPanel.add(settingsPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(this);
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshData();
    }
    
    // Constructor that accepts FinancialManager (for integration)
    public AdminPanel(FinancialManager financialManager) {
        this.financialManager = financialManager;
        
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Administrative Dashboard", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        
        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Live Statistics"));

        statsPanel.add(new JLabel("Total Revenue:"));
        lblRevenue = new JLabel("$0.00");
        lblRevenue.setForeground(new Color(0, 102, 0));
        statsPanel.add(lblRevenue);

        statsPanel.add(new JLabel("Occupancy Rate:"));
        lblOccupancy = new JLabel("0%");
        statsPanel.add(lblOccupancy);

        statsPanel.add(new JLabel("Vehicles Parked:"));
        lblVehicleCount = new JLabel("0");
        statsPanel.add(lblVehicleCount);

        centerPanel.add(statsPanel);
        
        // Fine Statistics Panel
        JPanel fineStatsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        fineStatsPanel.setBorder(BorderFactory.createTitledBorder("Fine Statistics"));
        
        fineStatsPanel.add(new JLabel("Total Fines Issued:"));
        lblTotalFines = new JLabel("0");
        lblTotalFines.setForeground(new Color(200, 0, 0));
        fineStatsPanel.add(lblTotalFines);
        
        fineStatsPanel.add(new JLabel("Total Unpaid Amount:"));
        lblUnpaidFines = new JLabel("$0.00");
        lblUnpaidFines.setForeground(new Color(200, 0, 0));
        fineStatsPanel.add(lblUnpaidFines);
        
        centerPanel.add(fineStatsPanel);
        
        // Fine Management Panel
        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Fine Scheme Management"));

        settingsPanel.add(new JLabel("Select Fine Scheme:"));

        rbSchemeA = new JRadioButton("Option A: Flat Rate ($50)");
        rbSchemeB = new JRadioButton("Option B: Progressive (1x, 2x, 3x)");
        rbSchemeC = new JRadioButton("Option C: Percentage (50% per 30min)");

        fineGroup = new ButtonGroup();
        fineGroup.add(rbSchemeA);
        fineGroup.add(rbSchemeB);
        fineGroup.add(rbSchemeC);

        // Set current scheme
        FineScheme currentScheme = financialManager.getCurrentFineScheme();
        if (currentScheme instanceof FineSchemeA) {
            rbSchemeA.setSelected(true);
        } else if (currentScheme instanceof FineSchemeB) {
            rbSchemeB.setSelected(true);
        } else if (currentScheme instanceof FineSchemeC) {
            rbSchemeC.setSelected(true);
        }

        settingsPanel.add(rbSchemeA);
        settingsPanel.add(rbSchemeB);
        settingsPanel.add(rbSchemeC);

        btnUpdateScheme = new JButton("Apply Scheme");
        btnUpdateScheme.addActionListener(this);
        settingsPanel.add(btnUpdateScheme);
        
        btnViewFines = new JButton("View Unpaid Fines");
        btnViewFines.addActionListener(this);
        settingsPanel.add(btnViewFines);

        centerPanel.add(settingsPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(this);
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshData();
    }

    private void refreshData() {
        // Update parking statistics (placeholder - would connect to ParkingLotManager)
        lblRevenue.setText("$0.00");
        lblOccupancy.setText("0%");
        lblVehicleCount.setText("0");
        
        // Update fine statistics
        if (financialManager != null) {
            List<Fine> allFines = financialManager.getAllFines();
            lblTotalFines.setText(String.valueOf(allFines.size()));
            
            double totalUnpaid = 0;
            for (Fine fine : allFines) {
                if (!fine.isPaid()) {
                    totalUnpaid += fine.getAmount();
                }
            }
            lblUnpaidFines.setText(String.format("$%.2f", totalUnpaid));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            refreshData();
            JOptionPane.showMessageDialog(this, "Dashboard updated!");
        }
        else if (e.getSource() == btnUpdateScheme) {
            FineScheme selectedScheme;
            String schemeName;
            
            if (rbSchemeA.isSelected()) {
                selectedScheme = new FineSchemeA();
                schemeName = "Option A: Flat Rate";
            } else if (rbSchemeB.isSelected()) {
                selectedScheme = new FineSchemeB();
                schemeName = "Option B: Progressive Rate";
            } else if (rbSchemeC.isSelected()) {
                selectedScheme = new FineSchemeC();
                schemeName = "Option C: Percentage-Based";
            } else {
                return;
            }
            
            if (financialManager != null) {
                financialManager.setFineScheme(selectedScheme);
                JOptionPane.showMessageDialog(this, 
                    "Fine Scheme updated to: " + schemeName + "\n\n" +
                    selectedScheme.getDescription(),
                    "Scheme Updated",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (e.getSource() == btnViewFines) {
            if (financialManager != null) {
                showUnpaidFinesDialog();
            }
        }
    }
    
    private void showUnpaidFinesDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Unpaid Fines", true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Top panel - Search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search by License Plate:"));
        JTextField searchField = new JTextField(15);
        topPanel.add(searchField);
        
        JLabel totalLabel = new JLabel("Total Unpaid: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(totalLabel);
        
        dialog.add(topPanel, BorderLayout.NORTH);
        
        // Center - Table
        String[] columns = {"Fine ID", "License Plate", "Amount", "Date", "Reason"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // Load unpaid fines
        double total = 0;
        for (Fine fine : financialManager.getAllFines()) {
            if (!fine.isPaid()) {
                tableModel.addRow(new Object[]{
                    fine.getFineId(),
                    fine.getLicensePlate(),
                    String.format("$%.2f", fine.getAmount()),
                    fine.getIssuedDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    fine.getReason()
                });
                total += fine.getAmount();
            }
        }
        totalLabel.setText(String.format("Total Unpaid: $%.2f", total));
        
        // Search functionality
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> {
            String plate = searchField.getText().trim();
            if (!plate.isEmpty()) {
                tableModel.setRowCount(0);
                double searchTotal = 0;
                for (Fine fine : financialManager.getUnpaidFines(plate)) {
                    tableModel.addRow(new Object[]{
                        fine.getFineId(),
                        fine.getLicensePlate(),
                        String.format("$%.2f", fine.getAmount()),
                        fine.getIssuedDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        fine.getReason()
                    });
                    searchTotal += fine.getAmount();
                }
                totalLabel.setText(String.format("Total Unpaid for %s: $%.2f", plate, searchTotal));
            }
        });
        topPanel.add(searchBtn);
        
        JButton showAllBtn = new JButton("Show All");
        showAllBtn.addActionListener(e -> {
            tableModel.setRowCount(0);
            double allTotal = 0;
            for (Fine fine : financialManager.getAllFines()) {
                if (!fine.isPaid()) {
                    tableModel.addRow(new Object[]{
                        fine.getFineId(),
                        fine.getLicensePlate(),
                        String.format("$%.2f", fine.getAmount()),
                        fine.getIssuedDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        fine.getReason()
                    });
                    allTotal += fine.getAmount();
                }
            }
            totalLabel.setText(String.format("Total Unpaid: $%.2f", allTotal));
            searchField.setText("");
        });
        topPanel.add(showAllBtn);
        
        // Bottom - Close button
        JPanel bottomPanel = new JPanel();
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        bottomPanel.add(closeBtn);
        
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    public FinancialManager getFinancialManager() {
        return financialManager;
    }
}

