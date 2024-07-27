package gui;

import service.ReservationService;
import model.Room;
import model.Reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HotelReservationGUI extends JFrame {
    private ReservationService reservationService = new ReservationService();
    private JTextArea roomFeaturesArea = new JTextArea();
    private JTextField customerNameField = new JTextField(20);
    private JComboBox<String> roomTypeBox = new JComboBox<>();
    private JComboBox<String> roomIdBox = new JComboBox<>();
    private JSpinner checkinDateSpinner = new JSpinner(new SpinnerDateModel());
    private JSpinner checkoutDateSpinner = new JSpinner(new SpinnerDateModel());
    private JButton checkPriceButton = new JButton("Check Price");
    private JLabel availabilityStatusLabel = new JLabel();

    public HotelReservationGUI() {
        setTitle("Hotel Reservation System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Reservation Details"));
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roomTypeLabel = new JLabel("Room Type:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(roomTypeLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(roomTypeBox, gbc);

        JLabel roomIdLabel = new JLabel("Room ID:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(roomIdLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(roomIdBox, gbc);

        JLabel customerNameLabel = new JLabel("Customer Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(customerNameLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(customerNameField, gbc);

        JLabel checkinDateLabel = new JLabel("Check-in Date and Time:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(checkinDateLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(checkinDateSpinner, gbc);

        JLabel checkoutDateLabel = new JLabel("Check-out Date and Time:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(checkoutDateLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(checkoutDateSpinner, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(checkPriceButton, gbc);

        JButton bookButton = new JButton("Book Room", new ImageIcon("icons/book.png"));
        gbc.gridx = 2;
        gbc.gridy = 4;
        inputPanel.add(bookButton, gbc);

        roomFeaturesArea.setEditable(false);
        roomFeaturesArea.setBorder(BorderFactory.createTitledBorder("Room Features"));
        mainPanel.add(new JScrollPane(roomFeaturesArea), BorderLayout.CENTER);

        availabilityStatusLabel.setForeground(Color.RED);
        availabilityStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(availabilityStatusLabel, BorderLayout.SOUTH);

        // Set up JSpinner format
        JSpinner.DateEditor checkinEditor = new JSpinner.DateEditor(checkinDateSpinner, "yyyy-MM-dd HH:mm");
        checkinDateSpinner.setEditor(checkinEditor);
        JSpinner.DateEditor checkoutEditor = new JSpinner.DateEditor(checkoutDateSpinner, "yyyy-MM-dd HH:mm");
        checkoutDateSpinner.setEditor(checkoutEditor);

        roomTypeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoomIdBox();
                updateRoomFeatures();
                availabilityStatusLabel.setText("");
                checkPriceButton.setEnabled(true);
            }
        });

        checkPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkPrice();
            }
        });

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeReservation();
            }
        });

        setRoomTypes();
        setFontsAndColors();
        setTooltips();
    }

    private void setRoomTypes() {
        String[] roomTypes = {
            "Select Room Type", "Single Non-Ac", "Single Ac", "Double Non-Ac", "Double Ac",
            "Queen Non-Ac", "Queen Ac", "King Non-Ac", "King Ac"
        };
        for (String roomType : roomTypes) {
            roomTypeBox.addItem(roomType);
        }
    }

    private void updateRoomIdBox() {
        roomIdBox.removeAllItems();
        roomIdBox.addItem("Select Room ID");
        String roomType = (String) roomTypeBox.getSelectedItem();
        if (roomType != null && !roomType.equals("Select Room Type")) {
            try {
                List<Room> rooms = reservationService.searchAvailableRooms(roomType);
                if (rooms.isEmpty()) {
                    roomIdBox.addItem("No Rooms Available");
                    availabilityStatusLabel.setText("*currently this type of room is not available");
                } else {
                    for (Room room : rooms) {
                        roomIdBox.addItem(String.valueOf(room.getRoomId()));
                    }
                    availabilityStatusLabel.setText("");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
        	roomIdBox.removeAllItems();
            roomIdBox.addItem("Select Room ID");
        }
    }

    private void updateRoomFeatures() {
        String roomType = (String) roomTypeBox.getSelectedItem();
        String features = getRoomFeatures(roomType);
        roomFeaturesArea.setText(features);
    }

    private String getRoomFeatures(String roomType) {
        switch (roomType) {
            case "Single Ac":
                return "• Comfortable single bed\n" +
                       "• Air conditioning\n" +
                       "• Flat-screen TV with cable channels\n" +
                       "• High-speed Wi-Fi\n" +
                       "• Work desk with ergonomic chair\n" +
                       "• Private en-suite bathroom with hot shower\n" +
                       "• Fresh towels and complimentary toiletries\n" +
                       "• Minibar with refreshments\n" +
                       "• Tea and coffee-making facilities\n" +
                       "• Wardrobe with a safe for valuables\n" +
                       "• Modern design with warm colors and stylish furnishings\n" +
                       "• Room service and daily housekeeping";
            case "Single Non-Ac":
                return "• Comfortable single bed\n" +
                       "• Well-ventilated with large windows\n" +
                       "• Flat-screen TV with cable channels\n" +
                       "• Free Wi-Fi\n" +
                       "• Work desk with ergonomic chair\n" +
                       "• Private bathroom with hot shower\n" +
                       "• Clean towels and complimentary toiletries\n" +
                       "• Minibar with refreshments\n" +
                       "• Tea and coffee-making facilities\n" +
                       "• Wardrobe with a safe for valuables\n" +
                       "• Contemporary design with soothing colors and comfortable furnishings\n" +
                       "• Room service and daily housekeeping";
            case "Double Ac":
                return "• Comfortable double bed\n" +
                       "• Air conditioning\n" +
                       "• Flat-screen TV with cable channels\n" +
                       "• High-speed Wi-Fi\n" +
                       "• Work desk with ergonomic chair\n" +
                       "• Private en-suite bathroom with hot shower\n" +
                       "• Fresh towels and complimentary toiletries\n" +
                       "• Minibar with refreshments\n" +
                       "• Tea and coffee-making facilities\n" +
                       "• Wardrobe with a safe for valuables\n" +
                       "• Modern design with stylish furnishings and warm colors\n" +
                       "• Room service and daily housekeeping";
            case "Double Non-Ac":
                return "• Comfortable double bed\n" +
                       "• Well-ventilated with large windows\n" +
                       "• Flat-screen TV with cable channels\n" +
                       "• Free Wi-Fi\n" +
                       "• Work desk with ergonomic chair\n" +
                       "• Private bathroom with hot shower\n" +
                       "• Clean towels and complimentary toiletries\n" +
                       "• Minibar with refreshments\n" +
                       "• Tea and coffee-making facilities\n" +
                       "• Wardrobe with a safe for valuables\n" +
                       "• Contemporary design with soothing colors and comfortable furnishings\n" +
                       "• Room service and daily housekeeping";
            case "Queen Ac":
                return "• Spacious queen-sized bed\n" +
                       "• Air conditioning\n" +
                       "• Flat-screen TV with cable channels\n" +
                       "• High-speed Wi-Fi\n" +
                       "• Work desk with ergonomic chair\n" +
                       "• Private en-suite bathroom with hot shower\n" +
                       "• Fresh towels and complimentary toiletries\n" +
                       "• Minibar with refreshments\n" +
                       "• Tea and coffee-making facilities\n" +
                       "• Wardrobe with a safe for valuables\n" +
                       "• Elegant design with modern furnishings and soothing colors\n" +
                       "• Room service and daily housekeeping";
            case "Queen Non-Ac":
                return "• Spacious queen-sized bed\n" +
                       "• Well-ventilated with large windows\n" +
                       "• Flat-screen TV with cable channels\n" +
                       "• Free Wi-Fi\n" +
                       "• Work desk with ergonomic chair\n" +
                       "• Private bathroom with hot shower\n" +
                       "• Clean towels and complimentary toiletries\n" +
                       "• Minibar with refreshments\n" +
                       "• Tea and coffee-making facilities\n" +
                       "• Wardrobe with a safe for valuables\n" +
                       "• Elegant design with modern furnishings and soothing colors\n" +
                       "• Room service and daily housekeeping";
            case "King Ac":
                return "• Luxurious king-sized bed\n" +
                       "• Air conditioning\n" +
                       "• Flat-screen TV with cable channels\n" +
                       "• High-speed Wi-Fi\n" +
                       "• Work desk with ergonomic chair\n" +
                       "• Private en-suite bathroom with hot shower\n" +
                       "• Fresh towels and complimentary toiletries\n" +
                       "• Minibar with refreshments\n" +
                       "• Tea and coffee-making facilities\n" +
                       "• Wardrobe with a safe for valuables\n" +
                       "• Opulent design with high-end furnishings and stylish décor\n" +
                       "• Room service and daily housekeeping";
            case "King Non-Ac":
                return "• Luxurious king-sized bed\n" +
                       "• Well-ventilated with large windows\n" +
                       "• Flat-screen TV with cable channels\n" +
                       "• Free Wi-Fi\n" +
                       "• Work desk with ergonomic chair\n" +
                       "• Private bathroom with hot shower\n" +
                       "• Clean towels and complimentary toiletries\n" +
                       "• Minibar with refreshments\n" +
                       "• Tea and coffee-making facilities\n" +
                       "• Wardrobe with a safe for valuables\n" +
                       "• Opulent design with high-end furnishings and stylish décor\n" +
                       "• Room service and daily housekeeping";
            default:
                return "Select a room type to see its features.";
        }
    }

    private void checkPrice() {
        
        if (validateInputs()) {
            try {
            	String roomType = (String) roomTypeBox.getSelectedItem();
                String roomIdStr = (String) roomIdBox.getSelectedItem();
                int roomId = Integer.parseInt(roomIdStr);
                Room room = reservationService.getRoomById(roomId);
                if (room != null && room.getRoomType().equals(roomType)) {
                    Date checkinDate = (Date) checkinDateSpinner.getValue();
                    Date checkoutDate = (Date) checkoutDateSpinner.getValue();
                    
                    // Calculate total amount and no_of_days
                    long diffInMillies1 = Math.abs(checkoutDate.getTime() - checkinDate.getTime());
                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies1, TimeUnit.MILLISECONDS);
                    double roomPricePerDay = reservationService.getRoomPrice(roomId);
                    double totalAmount = (diffInDays + 1) * roomPricePerDay;
                    
                    JOptionPane.showMessageDialog(this, "Total price for selected room is: " + totalAmount);
                } else {
                    JOptionPane.showMessageDialog(this, "Selected room is not available for the selected type.");
                }
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error checking price.");
            }
        } 

    }

    private void makeReservation() {
        
        String customerName = customerNameField.getText();
        if ( validateInputs() && !customerName.isEmpty() ) {
            try {
            	String roomType = (String) roomTypeBox.getSelectedItem();
                String roomIdStr = (String) roomIdBox.getSelectedItem();
                int roomId = Integer.parseInt(roomIdStr);
                Room room = reservationService.getRoomById(roomId);
                if (room != null && room.getRoomType().equals(roomType)) {
                    Date checkinDate = (Date) checkinDateSpinner.getValue();
                    Date checkoutDate = (Date) checkoutDateSpinner.getValue();
                    
                    // Calculate total amount and no_of_days
                    long diffInMillies = Math.abs(checkoutDate.getTime() - checkinDate.getTime());
                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    double roomPricePerDay = reservationService.getRoomPrice(roomId);
                    double totalAmount = (diffInDays + 1) * roomPricePerDay;
                    int noOfDays = (int) (diffInDays + 1);
                    
                    Reservation reservation = new Reservation(customerName, roomId, checkinDate, checkoutDate, noOfDays, totalAmount);
                    reservationService.makeReservation(reservation);
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Reservation Successful!\nTotal amount: " + totalAmount);
                } else {
                    JOptionPane.showMessageDialog(this, "Selected room is not available for the selected type.");
                }
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error making reservation.");
            }
        } else {
        	if(customerName.isEmpty()) {
        		JOptionPane.showMessageDialog(this, " Customer Name is Required.");
        	}
            
        }
    }
    
    

    private boolean validateInputs() {
        Date checkinDate = (Date) checkinDateSpinner.getValue();
        Date checkoutDate = (Date) checkoutDateSpinner.getValue();
        String roomIdStr = (String) roomIdBox.getSelectedItem();
        String roomType = (String) roomTypeBox.getSelectedItem();
        


        // Get the current date and time
        Date now = new Date();

        // Check if all required fields are filled
        
        if (roomType == null || roomType.equals("Select Room Type")) {
            JOptionPane.showMessageDialog(this, "Room Type is required.");
            return false;
        }
        
        if (roomIdStr == null  || roomIdStr.equals("Select Room ID")) {
            JOptionPane.showMessageDialog(this, "Room ID is required.");
            return false;
        }
        

        // Check if check-in date is today or within the next 10 minutes
        long diffInMillies = checkinDate.getTime() - now.getTime();
        long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diffInMinutes < -10) {
            JOptionPane.showMessageDialog(this, "Check-in date must be today or within the next 10 minutes.");
            return false;
        }

        // Check if check-out date is after check-in date
        if (checkoutDate.before(checkinDate)) {
            JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.");
            return false;
        }

        // Check if check-out date is at least 1 hour after check-in date
        diffInMillies = checkoutDate.getTime() - checkinDate.getTime();
        long diffInHours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diffInHours < 1) {
            JOptionPane.showMessageDialog(this, "Check-out date must be at least 1 hour after check-in date.");
            return false;
        }

        return true;
    }

    private void clearFields() {
        roomTypeBox.setSelectedIndex(0);
        roomIdBox.removeAllItems();
        roomIdBox.addItem("Select Room ID");
        customerNameField.setText("");
        checkinDateSpinner.setValue(new Date());
        checkoutDateSpinner.setValue(new Date());
    }

    private void setFontsAndColors() {
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        for (Component comp : getAllComponents(this)) {
            if (comp instanceof JLabel) {
                comp.setFont(labelFont);
                comp.setForeground(new Color(50, 50, 150));
            } else if (comp instanceof JTextField || comp instanceof JTextArea || comp instanceof JComboBox || comp instanceof JSpinner) {
                comp.setFont(fieldFont);
                comp.setBackground(new Color(230, 230, 250));
            }
        }
    }

    private void setTooltips() {
        roomTypeBox.setToolTipText("Select the type of room you want to search for.");
        roomIdBox.setToolTipText("Select the ID of the room you want to book.");
        customerNameField.setToolTipText("Enter the name of the customer making the reservation.");
        checkinDateSpinner.setToolTipText("Pick the check-in date and time.");
        checkoutDateSpinner.setToolTipText("Pick the check-out date and time.");
    }

    private Component[] getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        for (Component comp : comps) {
            if (comp instanceof Container) {
                comps = concatenate(comps, getAllComponents((Container) comp));
            }
        }
        return comps;
    }

    private Component[] concatenate(Component[] a, Component[] b) {
        Component[] result = new Component[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservationGUI().setVisible(true));
    }
}