package ch.unibe.ese.team1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Describes an alert. An alert can be created by a user. If ads matching the
 * criteria of the alert are added to the platform later, the user will be
 * notified.
 */
@Entity
public class Alert {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	private User user;

	@Column(nullable = false)
	private int zipcode;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int radius;

	@Column
	private boolean studio;

	@Column
	private boolean room;
	
	@Column
	private boolean flat;
	
	@Column
	private boolean house;
	
	@Column	
	private String roomTypeString;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
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
	
	public boolean getRoomType(String roomType) {
		boolean answer;
		switch(roomType) {
		case "Room":
			answer = getRoom();
			break;
		case "Studio":
			answer = getStudio();
			break;
		case "Flat":
			answer = getFlat();
			break;
		case "House":
			answer = getHouse();
			break;
		default:
			answer = false;
			break;
		}		
		return answer;
	}
	
	/**
	 * Speichert die Stringrepresentation für die Alerttabelle
	 */
	private void roomTypeToString() {
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
		
		public void defaultFilter() {
			//earliestMoveInDate = "-";
		}

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
}
