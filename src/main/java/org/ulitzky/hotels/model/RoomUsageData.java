package org.ulitzky.hotels.model;

import java.util.List;

public class RoomUsageData {
	
	private int numberOfRooms;
	private int totalIncome;
	
	public RoomUsageData() {
		this.numberOfRooms = 0;
		this.totalIncome = 0;
	}
	
	public static RoomUsageData fromAmountsList(final List<Integer> amountsList) {
		RoomUsageData result = new RoomUsageData();
		result.setNumberOfRooms(amountsList.size());
		result.setTotalIncome(amountsList.stream().mapToInt(Integer::intValue).sum());
		return result;
	}
	
	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(final int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public int getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(final int totalIncome) {
		this.totalIncome = totalIncome;
	}
	
	@Override
	public String toString() {
		return String.format("%d (%d EUR)", numberOfRooms, totalIncome);
	}

	
	

}
