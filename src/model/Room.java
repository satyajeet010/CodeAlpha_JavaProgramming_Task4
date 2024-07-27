//Room.java

package model;

public class Room {
    private int roomId;
    private String roomType;
    private double price;
    private boolean isAvailable;
    
    public Room() {}//added extra
    
    public Room(int roomId, String roomType, double price) {//added extra
		super();
		this.roomId = roomId;
		this.roomType = roomType;
		this.price = price;
	}
	// Getters and setters
    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}