package gui;

import core.EntryExitService;
import core.ParkingLotManager;
import domain.ParkingSpot;
import domain.Ticket;
import domain.Vehicle;
import shared.Enums.VehicleType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EntryExitPanel extends JPanel {

    private EntryExitService service;
    
    // Layout Manager for switching pages
    private CardLayout cardLayout;
    private JPanel cardsContainer; 

    // Logic Components
    private JTextField tfEntryPlate;
    private JComboBox<VehicleType> cbVehicleType;
    private JComboBox<String> cbAvailableSpots; 
    private JTextArea taTicketArea;
    private JTextField tfExitPlate;
    private JTextArea taBillArea;
    private JButton btnPay;
    
    private ParkingSpot currentExitSpot; 
    private double currentTotalAmount;

    public EntryExitPanel() {
        service = new EntryExitService();
        setLayout(new BorderLayout());

        // Initialize CardLayout
        cardLayout = new CardLayout();
        cardsContainer = new JPanel(cardLayout);

        //seperate 3 page
        JPanel menuPage = createMenuPage();
        JPanel entryPage = createEntryPage();
        JPanel exitPage = createExitPage();

        cardsContainer.add(menuPage, "MENU");
        cardsContainer.add(entryPage, "ENTRY");
        cardsContainer.add(exitPage, "EXIT");

        add(cardsContainer, BorderLayout.CENTER);
        
        // will start wif menu page first
        cardLayout.show(cardsContainer, "MENU");
    }

    private JPanel createMenuPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        JPanel buttonBox = new JPanel(new GridLayout(2, 1, 10, 20));
        
        JButton btnGoEntry = new JButton("Vehicle Entry");
        btnGoEntry.setFont(new Font("Arial", Font.BOLD, 18));
        btnGoEntry.setPreferredSize(new Dimension(200, 60));
        
        JButton btnGoExit = new JButton("Vehicle Exit");
        btnGoExit.setFont(new Font("Arial", Font.BOLD, 18));
        btnGoExit.setPreferredSize(new Dimension(200, 60));

        // switch to entry page
        btnGoEntry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardsContainer, "ENTRY");
            }
        });

        // switch to exit page
        btnGoExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardsContainer, "EXIT");
            }
        });

        buttonBox.add(btnGoEntry);
        buttonBox.add(btnGoExit);
        panel.add(buttonBox);

        return panel;
    }

    private JPanel createEntryPage() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBack = new JButton("<< Back to Menu");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardsContainer, "MENU");
            }
        });
        header.add(btnBack);
        panel.add(header, BorderLayout.NORTH);
        
        //entry form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        formPanel.add(new JLabel("Plate Number:"));
        tfEntryPlate = new JTextField();
        formPanel.add(tfEntryPlate);
        
        formPanel.add(new JLabel("Vehicle Type:"));
        cbVehicleType = new JComboBox<>(VehicleType.values());
        formPanel.add(cbVehicleType);
        
        JButton btnFindSpots = new JButton("Find Spots");
        btnFindSpots.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFindSpots();
            }
        });
        formPanel.add(btnFindSpots);
        
        cbAvailableSpots = new JComboBox<>();
        formPanel.add(cbAvailableSpots);

        JButton btnPark = new JButton("Park & Generate Ticket");
        btnPark.setBackground(new Color(177, 203, 222));
        btnPark.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePark();
            }
        });
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // display ticket
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createTitledBorder("Ticket Output"));
        footer.setPreferredSize(new Dimension(800, 150));
        
        taTicketArea = new JTextArea();
        taTicketArea.setEditable(false);
        taTicketArea.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.add(new JScrollPane(taTicketArea), BorderLayout.CENTER);
        
        panel.add(footer, BorderLayout.SOUTH);
        panel.add(btnPark, BorderLayout.EAST);

        return panel;
    }

    private JPanel createExitPage() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBack = new JButton("<< Back to Menu");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cardLayout.show(cardsContainer, "MENU");
            }
        });
        header.add(btnBack);
        panel.add(header, BorderLayout.NORTH);

        //exit form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        formPanel.add(new JLabel("Enter Plate Number:"));
        tfExitPlate = new JTextField();
        formPanel.add(tfExitPlate);

        formPanel.add(new JLabel("")); 

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnCalc = new JButton("Calculate Bill");
        btnCalc.setPreferredSize(new Dimension(120, 30)); 
        btnCalc.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleCalculateBill();
            }
        });
        buttonContainer.add(btnCalc);
        formPanel.add(buttonContainer);

        panel.add(formPanel, BorderLayout.CENTER);

        // bill details
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createTitledBorder("Payment"));
        footer.setPreferredSize(new Dimension(800, 170));

        taBillArea = new JTextArea();
        taBillArea.setEditable(false);
        taBillArea.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.add(new JScrollPane(taBillArea), BorderLayout.CENTER);

        btnPay = new JButton("Pay & Exit");
        btnPay.setEnabled(false);
        btnPay.setPreferredSize(new Dimension(100, 30));
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handlePayment();
            }
        });
        
        footer.add(btnPay, BorderLayout.SOUTH);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private void handleFindSpots() {
        String plate = tfEntryPlate.getText();
        VehicleType type = (VehicleType) cbVehicleType.getSelectedItem();
        
        if (plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Plate Number!");
            return;
        }
        
        if (service.findVehicleByPlate(plate) != null) {
            JOptionPane.showMessageDialog(this, 
                "Error: Vehicle " + plate + " is already exist!\n" +
                "Please exit the vehicle first.",
                "Duplicate Entry",
                JOptionPane.ERROR_MESSAGE);
            return; 
        }
        
        Vehicle tempV = new Vehicle(plate, type);
        List<ParkingSpot> spots = service.findAvailableSpots(tempV);
        
        cbAvailableSpots.removeAllItems();
        if (spots.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No spots available for " + type);
        } else {
            for (ParkingSpot s : spots) {
                cbAvailableSpots.addItem(s.getId());
            }
        }
    }

    private void handlePark() {
        String spotId = (String) cbAvailableSpots.getSelectedItem();
        String plate = tfEntryPlate.getText().trim();
        VehicleType type = (VehicleType) cbVehicleType.getSelectedItem();
        
        // error
        if (plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Error: You must enter a Plate Number before generating a ticket!",
                "Missing Input",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (spotId == null) {
            JOptionPane.showMessageDialog(this, "Please find and select a spot first.");
            return;
        }
        
        // check whether plate duplicate or nah
        if (service.findVehicleByPlate(plate) != null) {
             JOptionPane.showMessageDialog(this, "Vehicle is already parked!");
             return;
        }
        
        Vehicle v = new Vehicle(plate, type);
        
        // search for spot
        ParkingSpot selectedSpot = null;
        for (ParkingSpot s : ParkingLotManager.getInstance().getAllSpots()) {
            if (s.getId().equals(spotId)) {
                selectedSpot = s;
                break;
            }
        }
        
        if (selectedSpot != null) {
            Ticket t = service.processEntry(v, selectedSpot);
            taTicketArea.setText(t.toString());
            
            JOptionPane.showMessageDialog(this, "Vehicle Parked Successfully!\nReturning to Main Menu...");
            
            // Clear fields
            tfEntryPlate.setText("");
            cbAvailableSpots.removeAllItems();
            taTicketArea.setText(""); 
            cardLayout.show(cardsContainer, "MENU");
        }
    }

    private void handleCalculateBill() {
        String plate = tfExitPlate.getText().trim();
        currentExitSpot = service.findVehicleByPlate(plate);
        
        if (currentExitSpot == null) {
            JOptionPane.showMessageDialog(this, "Vehicle not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Retrieve vehicle details
        Vehicle v = currentExitSpot.getCurrentVehicle();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        
        double fee = service.calculateFee(currentExitSpot);
        double fine = service.calculateFine(currentExitSpot);
        currentTotalAmount = fee + fine;
        
        //format the date and time
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String entryTimeStr = v.getEntryTime().format(dtf);
        String exitTimeStr = now.format(dtf);
        
        //this one for calculate duration 
        long hoursParked = (long) (fee / currentExitSpot.getRate());
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== PARKING BILL ===\n");
        sb.append("Plate No:  ").append(v.getPlateNumber()).append("\n");
        sb.append("Vehicle Type: ").append(v.getType()).append("\n"); 
        sb.append("--------------------------------\n");
        sb.append("Entry Time: ").append(entryTimeStr).append("\n");
        sb.append("Exit Time:  ").append(exitTimeStr).append("\n");
        sb.append("Duration:   ~").append(hoursParked).append(" hours\n");
        sb.append("--------------------------------\n");
        sb.append("Rate:      RM ").append(currentExitSpot.getRate()).append("/hr\n");
        sb.append("--------------------\n");
        sb.append("Parking Fee: RM ").append(String.format("%.2f", fee)).append("\n");
                      
        if (fine > 0) {
            sb.append("Fine (Overstay): RM ").append(String.format("%.2f", fine)).append("\n");
        }
        
        sb.append("--------------------\n");
        sb.append("TOTAL DUE:   RM ").append(String.format("%.2f", currentTotalAmount)).append("\n");
        
        taBillArea.setText(sb.toString());
        btnPay.setEnabled(true);
    }

    private void handlePayment() {
        if (currentExitSpot != null) {
            service.processPayment(currentExitSpot, currentTotalAmount);
            JOptionPane.showMessageDialog(this, "Payment Successful.");
            
            taBillArea.setText("");
            tfExitPlate.setText("");
            btnPay.setEnabled(false);
            currentExitSpot = null;
            
            cardLayout.show(cardsContainer, "MENU");
        }
    }
}