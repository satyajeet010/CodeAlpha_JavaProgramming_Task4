//ReservationService.java

package service;

import database.DatabaseConnector;
import model.Reservation;
import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private Connection connection;

    public ReservationService() {
        try {
            this.connection = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean makeReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (customer_name, room_id, checkin_date, checkout_date, total_amount, no_of_days) VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE rooms SET is_available = 0 WHERE room_id ="+reservation.getRoomId();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, reservation.getCustomerName());
            pstmt.setInt(2, reservation.getRoomId());
            pstmt.setTimestamp(3, new Timestamp(reservation.getCheckinDate().getTime()));
            pstmt.setTimestamp(4, new Timestamp(reservation.getCheckoutDate().getTime()));
            pstmt.setDouble(5, reservation.getTotalAmount());
            pstmt.setInt(6, reservation.getNoOfDays());
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0) {
            	Statement stmt = connection.createStatement();
            	stmt.executeUpdate(updateSql);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getRoomPrice(int roomId) throws SQLException {
        String sql = "SELECT price FROM rooms WHERE room_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        }
        return 0;
    }

    public List<Room> searchAvailableRooms(String roomType) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE room_type = ? AND is_available = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, roomType);
            ResultSet rs = pstmt.executeQuery();
            List<Room> rooms = new ArrayList<>();
            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomType(rs.getString("room_type"));
                room.setPrice(rs.getDouble("price"));
                rooms.add(room);
            }
            return rooms;
        }
    }
    public Room getRoomById(int roomId) throws SQLException {//added extra
        
        
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, roomId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String type = rs.getString("room_type");
            double price = rs.getInt("price");
            return new Room(roomId, type, price);
        }
        return null;
    }
}