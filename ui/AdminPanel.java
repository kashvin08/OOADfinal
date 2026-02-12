package shared.ui;

import core.ParkingLotManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

    public class AdminPanel extends JPanel implements ActionListener {

        // Components defined as class fields so we can access them in actionPerformed
        private JLabel lblRevenue, lblOccupancy, lblVehicleCount;
        private JButton btnRefresh, btnUpdateScheme;
        private JRadioButton rbFixed, rbProgressive, rbHourly;
        private ButtonGroup fineGroup;

        public AdminPanel() {

            setLayout(new BorderLayout());


            JLabel header = new JLabel("Administrative Dashboard", JLabel.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 18));
            add(header, BorderLayout.NORTH);
            JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // 2 rows, 1 col
            JPanel statsPanel = new JPanel(new GridLayout(3, 2));
            statsPanel.setBorder(BorderFactory.createTitledBorder("Live Statistics"));

            statsPanel.add(new JLabel("Total Revenue:"));
            lblRevenue = new JLabel("RM 0.00");
            lblRevenue.setForeground(new Color(0, 102, 0)); // Dark Green
            statsPanel.add(lblRevenue);

            statsPanel.add(new JLabel("Occupancy Rate:"));
            lblOccupancy = new JLabel("0%");
            statsPanel.add(lblOccupancy);

            statsPanel.add(new JLabel("Vehicles Parked:"));
            lblVehicleCount = new JLabel("0");
            statsPanel.add(lblVehicleCount);

            centerPanel.add(statsPanel);
            JPanel settingsPanel = new JPanel(new FlowLayout()); // FlowLayout (Lec04 Slide 544)
            settingsPanel.setBorder(BorderFactory.createTitledBorder("Fine Management"));

            settingsPanel.add(new JLabel("Select Fine Scheme:"));

            rbFixed = new JRadioButton("Fixed (RM50)");
            rbProgressive = new JRadioButton("Progressive");
            rbHourly = new JRadioButton("Hourly (RM20)");

            fineGroup = new ButtonGroup();
            fineGroup.add(rbFixed);
            fineGroup.add(rbProgressive);
            fineGroup.add(rbHourly);

            rbFixed.setSelected(true);

            settingsPanel.add(rbFixed);
            settingsPanel.add(rbProgressive);
            settingsPanel.add(rbHourly);

            btnUpdateScheme = new JButton("Apply Scheme");
            btnUpdateScheme.addActionListener(this); // Event Handling (Lec04 Slide 750)
            settingsPanel.add(btnUpdateScheme);

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
            ParkingLotManager manager = ParkingLotManager.getInstance();

            lblRevenue.setText("RM " + String.format("%.2f", manager.getTotalRevenue()));
            lblOccupancy.setText(String.format("%.1f%%", manager.calculateOccupancyRate()));
            lblVehicleCount.setText(String.valueOf(manager.getTotalParkedVehicles()));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnRefresh) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Dashboard updated!");
            }
            else if (e.getSource() == btnUpdateScheme) {
                String selectedScheme = "Fixed";
                if (rbProgressive.isSelected()) selectedScheme = "Progressive";
                if (rbHourly.isSelected()) selectedScheme = "Hourly";
                
                ParkingLotManager.getInstance().setFineScheme(selectedScheme);

                JOptionPane.showMessageDialog(this, "Fine Scheme updated to: " + selectedScheme);
            }
        }
    }

