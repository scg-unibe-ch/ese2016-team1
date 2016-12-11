package ch.unibe.ese.team1.controller.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.Location;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.AdPictureDao;

/** Provides services for editing ads in the database. */
@Service
public class EditAdService {

	@Autowired
	private AdService adService;

	@Autowired
	private AdDao adDao;

	@Autowired
	private AdPictureDao adPictureDao;

	@Autowired
	private UserService userService;
	
	@Autowired
	private GeoDataService geoDataService;

	/**
	 * Handles persisting an edited ad to the database.
	 * 
	 * @param placeAdForm
	 *            the form to take the data from
	 * @param a
	 *            list of the file paths the pictures are saved under
	 * @param the
	 *            currently logged in user
	 * @throws ParseException 
	 */
	@Transactional
	public Ad saveFrom(PlaceAdForm placeAdForm, List<String> filePaths,
			User user, long adId, List<User> roomies, List<AdPicture> pics, List<String> visis) throws ParseException {



		Ad ad = adService.getAdById(adId);
		
		

		Date now = new Date();
		ad.setCreationDate(now);

		ad.setTitle(placeAdForm.getTitle());

		ad.setStreet(placeAdForm.getStreet());

		ad.setRoomType(placeAdForm.getRoomType());

		// take the zipcode - first four digits
		String zip = placeAdForm.getCity().substring(0, 4);
		ad.setZipcode(Integer.parseInt(zip));
		ad.setCity(placeAdForm.getCity().substring(7));
		
		//add location to add
		List<Location> searchedLocations = geoDataService.getLocationsByCity(ad.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(ad.getCity()).get(0);
			ad.setLatitude(searchedLocation.getLatitude());
			ad.setLongitude(searchedLocation.getLongitude());		
		} else {
			ad.setLatitude(-1);
			ad.setLongitude(-1);
		}

		Calendar calendar = Calendar.getInstance();
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12
		try {
			if (placeAdForm.getMoveInDate().length() >= 1) {
				int dayMoveIn = Integer.parseInt(placeAdForm.getMoveInDate()
						.substring(8, 10));
				int monthMoveIn = Integer.parseInt(placeAdForm.getMoveInDate()
						.substring(5, 7));
				int yearMoveIn = Integer.parseInt(placeAdForm.getMoveInDate()
						.substring(0, 4));
				calendar.set(yearMoveIn, monthMoveIn - 1, dayMoveIn);
				ad.setMoveInDate(calendar.getTime());
			}

			if (placeAdForm.getMoveOutDate().length() >= 1) {
				int dayMoveOut = Integer.parseInt(placeAdForm.getMoveOutDate()
						.substring(8, 10));
				int monthMoveOut = Integer.parseInt(placeAdForm
						.getMoveOutDate().substring(5, 7));
				int yearMoveOut = Integer.parseInt(placeAdForm.getMoveOutDate()
						.substring(0, 4));
				calendar.set(yearMoveOut, monthMoveOut - 1, dayMoveOut);
				ad.setMoveOutDate(calendar.getTime());
			}
			
			if(placeAdForm.getSaleType().equals("Auction")){
				if (placeAdForm.getEndOfAuction().length() >= 1) {
					SimpleDateFormat format = 
				            new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date time = format.parse(placeAdForm.getEndOfAuction());
					ad.setEndOfAuction(time);
				}
			ad.setCurrentBidding(placeAdForm.getCurrentBidding());
			}
		} catch (NumberFormatException e) {
		}

		ad.setPrizePerMonth(placeAdForm.getPrize());
		ad.setSquareFootage(placeAdForm.getSquareFootage());
		ad.setRetailPrice(placeAdForm.getRetailPrice());
		ad.setCurrentBidding(placeAdForm.getCurrentBidding());
		ad.setSaleType(placeAdForm.getSaleType());
		

		ad.setRoomDescription(placeAdForm.getRoomDescription());
		ad.setPreferences(placeAdForm.getPreferences());
		ad.setRoommates(placeAdForm.getRoommates());

		// ad description values
		ad.setSmokers(placeAdForm.isSmokers());
		ad.setAnimals(placeAdForm.isAnimals());
		ad.setGarden(placeAdForm.getGarden());
		ad.setBalcony(placeAdForm.getBalcony());
		ad.setCellar(placeAdForm.getCellar());
		ad.setFurnished(placeAdForm.isFurnished());
		ad.setCable(placeAdForm.getCable());
		ad.setGarage(placeAdForm.getGarage());
		ad.setInternet(placeAdForm.getInternet());

		/*
		 * Save the paths to the picture files, the pictures are assumed to be
		 * uploaded at this point!
		 */
		List<AdPicture> pictures = new ArrayList<>();
		for (String filePath : filePaths) {
			AdPicture picture = new AdPicture();
			picture.setFilePath(filePath);
			pictures.add(picture);
		}
		// add existing pictures
		for (AdPicture picture : pics) {
			pictures.add(picture);
		}
		ad.setPictures(pictures);

		/*
		 * Roommates are saved in the form as strings. They need to be converted
		 * into Users and saved as a List which will be accessible through the
		 * ad object itself.
		 */
		List<User> registeredUserRommates = new LinkedList<>();
		if (placeAdForm.getRegisteredRoommateEmails() != null) {
			for (String userEmail : placeAdForm.getRegisteredRoommateEmails()) {
				User roommateUser = userService.findUserByUsername(userEmail);
				registeredUserRommates.add(roommateUser);
			}
		}
		// add existing roommates
		for (User roommates : ad.getRegisteredRoommates()) {
			//registeredUserRommates.add(roommates);
		}
		for (User roommates : roomies) {
			registeredUserRommates.add(roommates);
		}
		ad.setRegisteredRoommates(registeredUserRommates);

		// visits
		List<Visit> visits = new LinkedList<>();
		List<String> visitStrings = new LinkedList<>();
		if(placeAdForm.getVisits() != null){
			visitStrings = placeAdForm.getVisits();
		}
		if(visis != null){
			for (String tempName : visis) {
				//UserDao userDao = null;
				if(tempName!= null){
					visitStrings.add(tempName);
					//ad.setTitle(ad.getTitle() + tempName);
				}
			}
		}
		if (visitStrings != null) {
			for (String visitString : visitStrings) {
				Visit visit = new Visit();
				// format is 28-02-2014;10:02;13:14
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String[] parts = visitString.split(";");
				String startTime = parts[0] + " " + parts[1];
				String endTime = parts[0] + " " + parts[2];
				Date startDate = null;
				Date endDate = null;
				try {
					startDate = dateFormat.parse(startTime);
					endDate = dateFormat.parse(endTime);
				} catch (ParseException ex) {
					ex.printStackTrace();
				}

				visit.setStartTimestamp(startDate);
				visit.setEndTimestamp(endDate);
				visit.setAd(ad);
				
				
				visits.add(visit);
				
			}

			ad.setVisits(visits);
			
ad.setStreet(""+visits.size());
ad.setStreet(ad.getStreet()+ad.getVisits().size());
			
		}

		if(user!=null){
			ad.setUser(user);
		}
		else{
			ad.setUser(ad.getUser());
		}

		adDao.save(ad);

		return ad;
	}

	
	
	
	
	

	
	
	
	/**
	 * Removes the picture with the given id from the list of pictures in the ad
	 * with the given id.
	 */
	@Transactional
	public void deletePictureFromAd(long adId, long pictureId) {
		Ad ad = adService.getAdById(adId);
		List<AdPicture> pictures = ad.getPictures();
		AdPicture picture = adPictureDao.findOne(pictureId);
		pictures.remove(picture);
		ad.setPictures(pictures);
		adDao.save(ad);
	}

	/**
	 * Fills a Form with the data of an ad.
	 */
	public PlaceAdForm fillForm(Ad ad) {
		PlaceAdForm adForm = new PlaceAdForm();

		adForm.setRoomDescription(ad.getRoomDescription());
		adForm.setPreferences(ad.getPreferences());
		adForm.setRoommates(ad.getRoommates());

		return adForm;
	}

	/**
	 * Deletes the roommate with the given id from the ad with the given id.
	 * 
	 * @param roommateId
	 *            the user to delete as roommate
	 * @param adId
	 *            the ad to delete the roommate from
	 */
	public void deleteRoommate(long roommateId, long adId) {
		Ad ad = adService.getAdById(adId);
		User roommate = userService.findUserById(roommateId);
		ad.getRegisteredRoommates().remove(roommate);
		adDao.save(ad);

	}

}
