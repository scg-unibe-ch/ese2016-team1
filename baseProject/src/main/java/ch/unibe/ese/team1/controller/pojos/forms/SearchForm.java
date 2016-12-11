package ch.unibe.ese.team1.controller.pojos.forms;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

/** This form is used for searching for an ad. */
public class SearchForm {

	private boolean filtered;

	@NotBlank(message = "Required")
	@Pattern(regexp = "(^[0-9]{4} - [-\\w\\s\\u00C0-\\u00FF\\/]*)|(<<from coordinates>>)", message = "Please pick a city from the list")
	private String city;
	
	private double longitude;
	private double latitude;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "Please enter a positive distance")
	private Integer radius;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "In your dreams.")
	private Integer prize;

	@AssertFalse(message = "Please select at least one type.")
	private boolean noRoomNoStudio;

	@AssertFalse(message = "Please select at least one type.")
	private boolean noBuyNoRent;
	
	private boolean buy;
	private boolean rent;
	
	public boolean getBuy() {
		return buy;
	}
	
	public void setBuy(boolean buy) {
		this.buy = buy;
	}
	
	public boolean getRent() {
		return rent;
	}
	
	public void setRent(boolean rent) {
		this.rent = rent;
	}
	
	public boolean getNoBuyNoRent() {
		return noBuyNoRent;
	}
	
	public void setNoBuyNoRent(boolean noBuyNoRent) {
		this.noBuyNoRent = noBuyNoRent;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Integer getPrize() {
		return prize;
	}

	public void setPrize(Integer prize) {
		this.prize = prize;
	}

	public boolean getNoRoomNoStudio() {
		return noRoomNoStudio;
	}

	public void setNoRoomNoStudio(boolean noRoomNoStudio) {
		this.noRoomNoStudio = noRoomNoStudio;
	}

	// //////////////////
	// Filtered results//
	// //////////////////

	public boolean getFiltered() {
		return filtered;
	}

	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}

	private String earliestMoveInDate;
	private String latestMoveInDate;
	private String earliestMoveOutDate;
	private String latestMoveOutDate;

	private boolean smokers;
	private boolean animals;
	private boolean garden;
	private boolean balcony;
	private boolean cellar;
	private boolean washingMachine;
	private boolean dishwasher;
	private boolean furnished;
	private boolean cable;
	private boolean garage;
	private boolean internet;

	private boolean roomHelper;

	// the ugly stuff
	private boolean studioHelper;
	private boolean flatHelper;
	private boolean houseHelper;

	public boolean getSmokers() {
		return smokers;
	}

	public void setSmokers(boolean smokers) {
		this.smokers = smokers;
	}

	public boolean getAnimals() {
		return animals;
	}

	public void setAnimals(boolean animals) {
		this.animals = animals;
	}

	public boolean getGarden() {
		return garden;
	}

	public void setGarden(boolean garden) {
		this.garden = garden;
	}

	public boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}

	public boolean getCellar() {
		return cellar;
	}

	public void setCellar(boolean cellar) {
		this.cellar = cellar;
	}

	public boolean getWashingMachine() {
		return washingMachine;
	}

	public void setWashingMachine(boolean washingMachine) {
		this.washingMachine = washingMachine;
	}

	public boolean getDishwasher() {
		return dishwasher;
	}

	public void setDishWasher(boolean dishwasher) {
		this.dishwasher = dishwasher;
	}

	public boolean getFurnished() {
		return furnished;
	}

	public void setFurnished(boolean furnished) {
		this.furnished = furnished;
	}

	public boolean getCable() {
		return cable;
	}

	public void setCable(boolean cable) {
		this.cable = cable;
	}

	public boolean getGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
		this.garage = garage;
	}

	public boolean getInternet() {
		return internet;
	}

	public void setInternet(boolean internet) {
		this.internet = internet;
	}

	public String getEarliestMoveInDate() {
		return earliestMoveInDate;
	}

	public void setEarliestMoveInDate(String earliestMoveInDate) {
		this.earliestMoveInDate = earliestMoveInDate;
	}

	public String getLatestMoveInDate() {
		return this.latestMoveInDate;
	}

	public void setLatestMoveInDate(String latestMoveInDate) {
		this.latestMoveInDate = latestMoveInDate;
	}

	public String getEarliestMoveOutDate() {
		return earliestMoveOutDate;
	}

	public void setEarliestMoveOutDate(String earliestMoveOutDate) {
		this.earliestMoveOutDate = earliestMoveOutDate;
	}

	public String getLatestMoveOutDate() {
		return latestMoveOutDate;
	}

	public void setLatestMoveOutDate(String latestMoveOutDate) {
		this.latestMoveOutDate = latestMoveOutDate;
	}

	public boolean getStudioHelper() {
		return studioHelper;
	}

	public void setStudioHelper(boolean helper) {
		this.studioHelper = helper;
	}

	public boolean getRoomHelper() {
		return roomHelper;
	}

	public void setRoomHelper(boolean helper) {
		this.roomHelper = helper;
	}
	
	public boolean getFlatHelper() {
		return flatHelper;
	}
	
	public void setFlatHelper(boolean flatHelper) {
		this.flatHelper = flatHelper;
	}
	
	public boolean getHouseHelper() {
		return houseHelper;
	}
	
	public void setHouseHelper(boolean houseHelper) {
		this.houseHelper = houseHelper;
	}
}