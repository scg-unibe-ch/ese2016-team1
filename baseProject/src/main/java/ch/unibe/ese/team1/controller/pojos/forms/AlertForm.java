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

	@AssertFalse(message = "Please select either or both types")
	private boolean noRoomNoStudio;

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
}
