package org.ulitzky.hotels.model.enums;

public enum RoomType {
	
	PREMIUM("Premium", 100), 
	ECONOMY("Economy", 0);
	
	private String description;
	private int minAmountToEnsure;
	
	RoomType(final String description, final int minAmountToEnsure)  {
		this.description = description;
		this.minAmountToEnsure = minAmountToEnsure;
	}

	public String getDescription() {
		return description;
	}

	public int getMinAmountToEnsure() {
		return minAmountToEnsure;
	}
	
	

}
