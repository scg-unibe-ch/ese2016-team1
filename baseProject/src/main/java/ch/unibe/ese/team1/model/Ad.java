package ch.unibe.ese.team1.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/** Describes an advertisement that users can place and search for. */
@Entity
public class Ad {

	@Id
	@GeneratedValue
	private long id;
	
	private long altId;

	private boolean auctionMessage = false;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String street;

	@Column(nullable = false)
	private int zipcode;

	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Temporal(TemporalType.DATE)
	private Date moveInDate;

	@Temporal(TemporalType.DATE)
	private Date moveOutDate;
	
	@Column(nullable = false)
	private String saleType;

	@Column(nullable = false)
	private int prizePerMonth;
	
	@Column(nullable = true)
	private int retailPrice;
	
	@Column(nullable = true)
	private Date endOfAuction;
	
	@Column(nullable = false)
	private int currentBidding;
	
	@Column(nullable = true)
	private String currentBuyer;
	
	public static final int DEFAULT_MIN_BID = 1000;
	
	private boolean auctionEnded;

	@Column(nullable = false)
	private int squareFootage;

	@Column(nullable = false)
	@Lob
	private String roomDescription;

	@Column(nullable = false)
	@Lob
	private String preferences;

	@Column(nullable = false)
	private String roommates;

	@Fetch(FetchMode.SELECT)
	@ManyToMany(fetch = FetchType.EAGER)
	private List<User> registeredRoommates;

	@Column(nullable = false)
	private boolean smokers;

	@Column(nullable = false)
	private boolean animals;

	@Column(nullable = false)
	private boolean garden;

	@Column(nullable = false)
	private boolean balcony;

	@Column(nullable = false)
	private boolean cellar;
	
	@Column(nullable = false)
	private boolean washingMachine;
	
	@Column(nullable = false)
	private boolean dishwasher;

	@Column(nullable = false)
	private boolean furnished;

	@Column(nullable = false)
	private boolean cable;

	@Column(nullable = false)
	private boolean garage;

	@Column(nullable = false)
	private boolean internet;

	@Column(nullable = false)
	private String roomType;

	@Fetch(FetchMode.SELECT)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AdPicture> pictures;

	@ManyToOne(optional = false)
	private User user;
	
	@OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Visit> visits;

	private double latitude;

	private double longitude;
	
	////////////////////////////////////////////////////////////////////////////////////////////

	public boolean getAuctionMessage(){
		return auctionMessage;
	}
	
	public void setAuctionMessage(){
		auctionMessage = true;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public boolean getSmokers() {
		return smokers;
	}

	public void setSmokers(boolean allowsSmokers) {
		this.smokers = allowsSmokers;
	}

	public boolean getAnimals() {
		return animals;
	}

	public void setAnimals(boolean allowsAnimals) {
		this.animals = allowsAnimals;
	}

	public boolean getGarden() {
		return garden;
	}

	public void setGarden(boolean hasGarden) {
		this.garden = hasGarden;
	}

	public boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(boolean hasBalcony) {
		this.balcony = hasBalcony;
	}

	public boolean getCellar() {
		return cellar;
	}

	public void setCellar(boolean hasCellar) {
		this.cellar = hasCellar;
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

	public void setDishwasher(boolean dishwasher) {
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

	public void setCable(boolean hasCable) {
		this.cable = hasCable;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getAltId() {
		return altId;
	}

	public void setAltId(long altId) {
		this.altId = altId;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public Date getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(Date moveInDate) {
		this.moveInDate = moveInDate;
	}

	public void setMoveOutDate(Date moveOutDate) {
		this.moveOutDate = moveOutDate;
	}
	
	public void setEndOfAuction(Date endOfAuction) {
		this.endOfAuction = endOfAuction;
	}
	
	public Date getEndOfAuction(){
		return endOfAuction;
	}
	
	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public int getPrizePerMonth() {
		return prizePerMonth;
	}

	public void setPrizePerMonth(int prizePerMonth) {
		this.prizePerMonth = prizePerMonth;
	}
	
	public int getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(int retailPrice) {
		this.retailPrice = retailPrice;
	}
	
	public int getCurrentBidding() {
		return currentBidding;
	}

	public void setCurrentBidding(int currentBidding) {
		this.currentBidding = currentBidding;
	}
	
	public String getCurrentBuyer() {
		return currentBuyer;
	}

	public void setCurrentBuyer(String currentBuyer) {
		this.currentBuyer = currentBuyer;
	}
	
	public int getNextPossibleBid() {
		return (int) Math.round(Math.max((currentBidding * 1.05), Ad.DEFAULT_MIN_BID));
	}
	
	public boolean getAuctionEnded() {
		this.auctionEnded = this.getAuctionEndTimeBeforeToday() 
				|| this.getAuctionEndedCurrentBiddingHigherThanRetailPrice();
		return this.auctionEnded;
	}
	
	public boolean getAuctionEndTimeBeforeToday() {
		Date today = new Date();
		return this.endOfAuction.before(today);
	}
	
	public boolean getAuctionEndedCurrentBiddingHigherThanRetailPrice() {
		return (this.currentBidding >= this.retailPrice) &&
				this.retailPrice != 0;
	}
	
	public void addFifteenMinutesToAuctionEndedIfNecessary() {
		Date today = new Date();
		int fifteenMinutesInMilliseconds = 900000;
		if (this.endOfAuction.getTime() - today.getTime() < fifteenMinutesInMilliseconds) { 
			long newDateMilliseconds = this.endOfAuction.getTime() + fifteenMinutesInMilliseconds;
			Date newDate = new Date(newDateMilliseconds);
			this.setEndOfAuction(newDate);
		}
	}
	
	public int getSquareFootage() {
		return squareFootage;
	}

	public void setSquareFootage(int squareFootage) {
		this.squareFootage = squareFootage;
	}

	public String getRoomDescription() {
		return roomDescription;
	}

	public void setRoomDescription(String roomDescription) {
		this.roomDescription = roomDescription;
	}

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public String getRoommates() {
		return roommates;
	}

	public void setRoommates(String roommates) {
		this.roommates = roommates;
	}

	public List<AdPicture> getPictures() {
		return pictures;
	}

	public void setPictures(List<AdPicture> pictures) {
		this.pictures = pictures;
	}

	public Date getMoveOutDate() {
		return moveOutDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getDate(boolean date) {
		if (date)
			return moveInDate;
		else
			return moveOutDate;
	}

	public List<User> getRegisteredRoommates() {
		return registeredRoommates;
	}

	public void setRegisteredRoommates(List<User> registeredRoommates) {
		this.registeredRoommates = registeredRoommates;
	}

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}
	
	public List<String> getVisitsAsStrings() {
		List<String> names = new LinkedList<String>();
		for (Visit tempVisit : visits) {
			names.add(tempVisit.getStartTimestamp().toString().substring(0, 10) + ";" + tempVisit.getStartTimestamp().toString().substring(11, 16) + ";" + tempVisit.getEndTimestamp().toString().substring(11, 16));
		}
		return names;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	// equals method is defined to check for id only
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ad other = (Ad) obj;
		if (id != other.id)
			return false;
		return true;
	}
}