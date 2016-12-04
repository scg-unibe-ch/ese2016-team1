package ch.unibe.ese.team1.controller.pojos.forms;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import ch.unibe.ese.team1.model.User;

/** This form is used when a user wants to create a new alert. */
public class AlertForm {	
	private User user;

	private boolean studio;
	private boolean room;
	private boolean flat;
	private boolean house;
	
	private String roomTypeString;

	@NotBlank(message = "Required")
	@Pattern(regexp = "^[0-9]{4} - [-\\w\\s\\u00C0-\\u00FF]*", message = "Please pick a city from the list")
	private String city;

	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "Please enter a positive distance")
	private Integer radius;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "In your dreams.")
	private Integer price;
	
	private int zipCode;

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
	
	public int getZipCode() {
		return zipCode;
	}
	public void setZipCode(int zip) {
		this.zipCode = zip;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public boolean getStudio() {
		return studio;
	}

	public void setStudio(boolean studio) {
		this.studio = studio;
		roomTypeToString();
	}

	public boolean getRoom() {
		return room;
	}

	public void setRoom(boolean room) {
		this.room = room;
		roomTypeToString();
	}
	
	public boolean getFlat() {
		return flat;
	}
	
	public void setFlat(boolean flat) {
		this.flat = flat;
		roomTypeToString();
	}
	
	public boolean getHouse() {
		return house;
	}
	
	public void setHouse(boolean house) {
		this.house = house;
		roomTypeToString();
	}

	public boolean getNoRoomNoStudio() {
		return noRoomNoStudio;
	}

	public void setNoRoomNoStudio(boolean noRoomNoStudio) {
		this.noRoomNoStudio = noRoomNoStudio;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Speichert die Stringrepresentation für die Alerttabelle
	 */
	public void roomTypeToString() {
		String roomTypeString = "void";
		if(room)
			roomTypeString = "Room";
		if(studio) {
			if(roomTypeString.equals("void"))
				roomTypeString = "Studio";
			else
				roomTypeString += ", Studio";
		}
		if(flat) {
			if(roomTypeString.equals("void"))
				roomTypeString = "Flat";
			else
				roomTypeString += ", Flat";
		}
		if(house) {
			if(roomTypeString.equals("void"))
				roomTypeString = "House";
			else
				roomTypeString += ", House";
		}
		setRoomTypeString(roomTypeString);
	}
	
	public void setRoomTypeString(String string) {
		this.roomTypeString = string;
	}
	
	public String getRoomTypeString() {
		return roomTypeString;
	}
	
	// //////////////////
	// Filtered results//
	// //////////////////

	private String earliestMoveInDate;
	private String latestMoveInDate;
	private String earliestMoveOutDate;
	private String latestMoveOutDate;

	private boolean smokers;
	private boolean animals;
	private boolean garden;
	private boolean balcony;
	private boolean cellar;
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
