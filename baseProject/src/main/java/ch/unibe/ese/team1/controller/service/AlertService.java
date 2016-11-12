package ch.unibe.ese.team1.controller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.lang.Math;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.forms.AlertForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Alert;
import ch.unibe.ese.team1.model.Location;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;

/**
 * Provides and handles persistence operations for adding, editing and deleting
 * alerts.
 */
@Service
public class AlertService {

	@Autowired
	UserDao userDao;

	@Autowired
	AlertDao alertDao;

	@Autowired
	MessageDao messageDao;

	@Autowired
	private GeoDataService geoDataService;

	/**
	 * Persists a new alert with the data from the alert form to the database.
	 * 
	 * @param alertForm
	 *            the form to take the data from
	 * @param user
	 *            the user to associate the new alert to
	 */
	@Transactional
	public void saveFrom(AlertForm alertForm, User user) {
		Alert alert = new Alert();

		String zip = alertForm.getCity().substring(0, 4);
		alert.setZipcode(Integer.parseInt(zip));
		alert.setCity(alertForm.getCity().substring(7));

		alert.setBuy(alertForm.getBuy());
		alert.setRent(alertForm.getRent());
		alert.setPrice(alertForm.getPrice());
		alert.setRadius(alertForm.getRadius());
		alert.setRoom(alertForm.getRoom());
		alert.setStudio(alertForm.getStudio());
		alert.setFlat(alertForm.getFlat());
		alert.setHouse(alertForm.getHouse());
		alert.setSmokers(alertForm.getSmokers());
		alert.setGarden(alertForm.getGarden());
		alert.setCellar(alertForm.getCellar());
		alert.setCable(alertForm.getCable());
		alert.setInternet(alertForm.getInternet());
		alert.setAnimals(alertForm.getAnimals());
		alert.setBalcony(alertForm.getBalcony());
		alert.setFurnished(alertForm.getFurnished());
		alert.setGarage(alertForm.getGarage());
		alert.setEarliestMoveInDate(alertForm.getEarliestMoveInDate());
		alert.setLatestMoveInDate(alertForm.getLatestMoveInDate());
		alert.setEarliestMoveOutDate(alertForm.getEarliestMoveOutDate());
		alert.setLatestMoveOutDate(alertForm.getLatestMoveOutDate());
		alert.setUser(user);
		alertDao.save(alert);
	}

	/**
	 * Returns all alerts that belong to the given user.
	 */
	@Transactional
	public Iterable<Alert> getAlertsByUser(User user) {
		return alertDao.findByUser(user);
	}

	/** Deletes the alert with the given id. */
	@Transactional
	public void deleteAlert(Long id) {
		alertDao.delete(id);
	}

	/**
	 * Triggers all alerts that match the given ad. For every user, only one
	 * message is sent.
	 */
	@Transactional
	public void triggerAlerts(Ad ad) {
		int adPrice = ad.getPrizePerMonth();
		Iterable<Alert> alerts = alertDao.findByPriceGreaterThan(adPrice - 1);

		// loop through all ads with matching city and price range, throw out
		// mismatches
		Iterator<Alert> alertIterator = alerts.iterator();
		while (alertIterator.hasNext()) {
			Alert alert = alertIterator.next();
			if (typeMismatchWith(ad, alert) || radiusMismatchWith(ad, alert) || priceMismatchWith(ad, alert)
					|| ad.getUser().equals(alert.getUser()) || smokersMismatchWith(ad, alert)
					 || gardenMismatchWith(ad, alert) || cellarMismatchWith(ad, alert)
					 || cableMismatchWith(ad, alert) || internetMismatchWith(ad, alert)
					 || animalsMismatchWith(ad, alert) || balconyMismatchWith(ad, alert)
					 || furnishedMismatchWith(ad, alert) || garageMismatchWith(ad, alert)
					 || moveInDateMismatchWith(ad, alert) || moveOutDateMismatchWith(ad, alert)
					 || buyMismatchWith(ad, alert) || rentMismatchWith(ad, alert))
				alertIterator.remove();
		}

		// send only one message per user, no matter how many alerts were
		// triggered
		List<User> users = new ArrayList<User>();
		for (Alert alert : alerts) {
			User user = alert.getUser();
			if (!users.contains(user)) {
				users.add(user);
			}
		}

		// send messages to all users with matching alerts
		for (User user : users) {
			Date now = new Date();
			Message message = new Message();
			message.setSubject("It's a match!");
			message.setText(getAlertText(ad));
			message.setSender(userDao.findByUsername("System"));
			message.setRecipient(user);
			message.setState(MessageState.UNREAD);
			message.setDateSent(now);
			messageDao.save(message);
		}
	}

	/**
	 * Returns the text for an alert message with the properties of the given
	 * ad.
	 */
	private String getAlertText(Ad ad) {
		return "Dear user,<br>good news. A new ad matching one of your alerts has been "
				+ "entered into our system. You can visit it here:<br><br>"
				+ "<a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a><br><br>"
				+ "Good luck and enjoy,<br>"
				+ "Your FlatFindr crew";
	}
	
	private boolean moveInDateMismatchWith(Ad ad, Alert alert) {
		Date moveInDateAd = null;
		Date earliestInDateAlert= null;
		Date latestInDateAlert = null;
		boolean mismatch = false;

		// parse move-in and move-out dates
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			moveInDateAd = ad.getMoveInDate();
		try {
			earliestInDateAlert = formatter.parse(alert.getEarliestMoveInDate());
		} catch (Exception e) {	}
		try {
			latestInDateAlert = formatter.parse(alert.getLatestMoveInDate());
		} catch (Exception e) {	}
		
		if(earliestInDateAlert != null && latestInDateAlert != null &&
				(!moveInDateAd.after(earliestInDateAlert) && !moveInDateAd.before(latestInDateAlert)))
			mismatch = true;
		
		return mismatch;
	}
	
	private boolean moveOutDateMismatchWith(Ad ad, Alert alert) {
		Date moveOutDateAd = null;
		Date earliestOutDateAlert= null;
		Date latestOutDateAlert = null;
		boolean mismatch = false;

		// parse move-in and move-out dates
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			moveOutDateAd = ad.getMoveInDate();
		try {
			earliestOutDateAlert = formatter.parse(alert.getEarliestMoveOutDate());
		} catch (Exception e) {	}
		try {
			latestOutDateAlert = formatter.parse(alert.getLatestMoveOutDate());
		} catch (Exception e) {	}
		
		if(earliestOutDateAlert != null && latestOutDateAlert != null &&
				(!moveOutDateAd.after(earliestOutDateAlert) || !moveOutDateAd.before(latestOutDateAlert)))
			mismatch = true;
		
		return mismatch;
	}
	
	

	/** Checks if an ad is conforming to the criteria in an alert. */
	private boolean typeMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if (!alert.getRoomType(ad.getRoomType()))
			mismatch = true;
		return mismatch;
	}
	
	/** Checks if the price of an ad is conforming to the criteria in an alert. */
	private boolean priceMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if (alert.getPrice() < ad.getRetailPrice())
			mismatch = true;
		return mismatch;
	}
	
	private boolean smokersMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(alert.getSmokers() && !ad.getSmokers())
			mismatch = true;
		return mismatch;
	}
	
	private boolean gardenMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(alert.getGarden() && !ad.getGarden())
			mismatch = true;
		return mismatch;
	}
	
	private boolean cellarMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(alert.getCellar() && !ad.getCellar())
			mismatch = true;
		return mismatch;
	}
	
	private boolean cableMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(alert.getCable() && !ad.getCable())
			mismatch = true;
		return mismatch;
	}
	
	private boolean internetMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(alert.getInternet() && !ad.getInternet())
			mismatch = true;
		return mismatch;
	}
	
	private boolean animalsMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(alert.getAnimals() && !ad.getAnimals())
			mismatch = true;
		return mismatch;
	}
	
	private boolean balconyMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(alert.getBalcony() && !ad.getBalcony())
			mismatch = true;
		return mismatch;
	}
	
	private boolean furnishedMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(alert.getFurnished() && !ad.getFurnished())
			mismatch = true;
		return mismatch;
	}
	
	private boolean garageMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(alert.getGarage() && !ad.getGarage())
			mismatch = true;
		return mismatch;
	}
	
	private boolean buyMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(!alert.getBuy() || !alert.getRent()) {
			if(alert.getBuy() && !ad.getSaleType().equals("Buy") && !ad.getSaleType().equals("Auction"))
				mismatch = true;
		}
		return mismatch;
	}
	
	private boolean rentMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(!alert.getBuy() || !alert.getRent()) {
			if(alert.getRent() && !ad.getSaleType().equals("Rent"))
				mismatch = true;
		}
		return mismatch;
	}

	/**
	 * Checks whether an ad is for a place too far away from the alert.
	 * 
	 * @param ad
	 *            the ad to compare to the alert
	 * @param alert
	 *            the alert to compare to the ad
	 * 
	 * @return true in case the alert does not match the ad (the ad is too far
	 *         away), false otherwise
	 */
	private boolean radiusMismatchWith(Ad ad, Alert alert) {
		final int earthRadiusKm = 6380;
		Location adLocation = geoDataService.getLocationsByCity(ad.getCity())
				.get(0);
		Location alertLocation = geoDataService.getLocationsByCity(
				alert.getCity()).get(0);

		double radSinLat = Math.sin(Math.toRadians(adLocation.getLatitude()));
		double radCosLat = Math.cos(Math.toRadians(adLocation.getLatitude()));
		double radLong = Math.toRadians(adLocation.getLongitude());
		double radLongitude = Math.toRadians(alertLocation.getLongitude());
		double radLatitude = Math.toRadians(alertLocation.getLatitude());
		double distance = Math.acos(radSinLat * Math.sin(radLatitude)
				+ radCosLat * Math.cos(radLatitude)
				* Math.cos(radLong - radLongitude))
				* earthRadiusKm;
		return (distance > alert.getRadius());
	}
	
	//for testing
	public boolean radiusMismatch(Ad ad, Alert alert) {
		return radiusMismatchWith(ad, alert);
	}
	
	//for testing
	public boolean typeMismatch(Ad ad, Alert alert) {
		return typeMismatchWith(ad, alert);
	}
}