//Reservation.java

package model;

import java.util.Date;

public class Reservation {
    

	private int reservationId;
    private String customerName;
    private int roomId;
    private Date checkinDate;
    private Date checkoutDate;
    private double totalAmount;
    private int noOfDays;
    
    public Reservation() {}//ADDED extra
    public Reservation(String customerName, int roomId, Date checkinDate, Date checkoutDate,int noOfDays, double totalAmount) {//added extra
		super();
		this.customerName = customerName;
		this.roomId = roomId;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.totalAmount = totalAmount;
		this.noOfDays = noOfDays;
	}
    
    
    // Getters and setters  for all fields
    public int getReservationId() {
        return reservationId;
    }
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public Date getCheckinDate() {
        return checkinDate;
    }
    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }
    public Date getCheckoutDate() {
        return checkoutDate;
    }
    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }
}