package shared.ui;

import core.ParkingLotManager;
import domain.ParkingSpot;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ReportingPanel extends JPanel implements ActionListener {

    private JTextArea reportArea; // Display area (Lec04 Slide 520)
    private JButton btnOccupancyReport, btnRevenueReport, btnClear;

    public ReportingPanel() {
        setLayout(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout());

        btnOccupancyReport = new JButton("Occupancy Report");
        btnOccupancyReport.addActionListener(this);

        btnRevenueReport = new JButton("Revenue Report");
        btnRevenueReport.addActionListener(this);

        btnClear = new JButton("Clear Screen");
        btnClear.addActionListener(this);

        controls.add(btnOccupancyReport);
        controls.add(btnRevenueReport);
        controls.add(btnClear);
        add(controls, BorderLayout.NORTH);

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Monospaced for alignment
        reportArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOccupancyReport) {
            generateOccupancyReport();
        } else if (e.getSource() == btnRevenueReport) {
            generateRevenueReport();
        } else if (e.getSource() == btnClear) {
            reportArea.setText("");
        }
    }

    private void generateOccupancyReport() {
        ParkingLotManager manager = ParkingLotManager.getInstance();
        List<ParkingSpot> spots = manager.getAllSpots();

        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("       CURRENT OCCUPANCY REPORT          \n");
        sb.append("=========================================\n");
        sb.append(String.format("%-10s | %-12s | %-10s\n", "Spot ID", "Type", "Status"));
        sb.append("-----------------------------------------\n");

        for (ParkingSpot spot : spots) {
            String status = spot.isOccupied() ? "[OCCUPIED]" : "Available";
            sb.append(String.format("%-10s | %-12s | %-10s\n",
                    spot.getId(), spot.getType(), status));
        }
        sb.append("=========================================\n");
        sb.append("Total Spots: " + spots.size() + "\n");

        reportArea.setText(sb.toString());
    }

    private void generateRevenueReport() {
        ParkingLotManager manager = ParkingLotManager.getInstance();

        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("           REVENUE REPORT                \n");
        sb.append("=========================================\n");
        sb.append("Total Revenue: RM " + String.format("%.2f", manager.getTotalRevenue()) + "\n");
        sb.append("Current Fine Scheme: " + manager.getFineScheme() + "\n");
        sb.append("=========================================\n");

        reportArea.setText(sb.toString());
    }
}