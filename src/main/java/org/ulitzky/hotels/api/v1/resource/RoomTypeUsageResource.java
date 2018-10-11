package org.ulitzky.hotels.api.v1.resource;

public class RoomTypeUsageResource {

	private final String roomType;
	private final int roomsUsage;
	private final int income;
	
	public RoomTypeUsageResource(String roomType, int roomsUsage, int income) {
		super();
		this.roomType = roomType;
		this.roomsUsage = roomsUsage;
		this.income = income;
	}
	public String getRoomType() {
		return roomType;
	}
	public int getRoomsUsage() {
		return roomsUsage;
	}
	public int getIncome() {
		return income;
	}
	
	
	
}
