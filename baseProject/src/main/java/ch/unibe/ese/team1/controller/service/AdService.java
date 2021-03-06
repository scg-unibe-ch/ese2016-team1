package ch.unibe.ese.team1.controller.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.MailService;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.Location;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.MessageDao;

/** Handles all persistence operations concerning ad placement and retrieval. */
@Service
public class AdService {

	@Autowired
	private AdDao adDao;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private UserService userService;

	@Autowired
	private GeoDataService geoDataService;

	/**
	 * Handles persisting a new ad to the database.
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
			User user) throws ParseException {
		
		Ad ad = new Ad();

		Date now = new Date();
		ad.setCreationDate(now);

		ad.setTitle(placeAdForm.getTitle());

		ad.setStreet(placeAdForm.getStreet());

		ad.setRoomType(placeAdForm.getRoomType());
		ad.setSaleType(placeAdForm.getSaleType());

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

		ad.setRoomDescription(placeAdForm.getRoomDescription());
		ad.setPreferences(placeAdForm.getPreferences());
		ad.setRoommates(placeAdForm.getRoommates());

		// ad description values
		ad.setSmokers(placeAdForm.isSmokers());
		ad.setAnimals(placeAdForm.isAnimals());
		ad.setGarden(placeAdForm.getGarden());
		ad.setBalcony(placeAdForm.getBalcony());
		ad.setCellar(placeAdForm.getCellar());
		ad.setWashingMachine(placeAdForm.getWashingMachine());
		ad.setDishwasher(placeAdForm.getDishwasher());;
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
		ad.setRegisteredRoommates(registeredUserRommates);

		// visits
		List<Visit> visits = new LinkedList<>();
		List<String> visitStrings = placeAdForm.getVisits();
		if (visitStrings != null) {
			for (String visitString : visitStrings) {
				Visit visit = new Visit();
				// format is 28-02-2014;10:02;13:14
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
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
		}

		ad.setUser(user);
		
		adDao.save(ad);

		return ad;
	}

	/**
	 * Gets the ad that has the given id.
	 * 
	 * @param id
	 *            the id that should be searched for
	 * @return the found ad or null, if no ad with this id exists
	 */
	@Transactional
	public Ad getAdById(long id) {
		return adDao.findOne(id);
	}
	
	public void saveAd(Ad ad) {
		adDao.save(ad);
	}

	/** Returns all ads in the database */
	@Transactional
	public Iterable<Ad> getAllAds() {
		return adDao.findAll();
	}

	/**
	 * Returns the newest ads in the database. Parameter 'newest' says how many.
	 */
	@Transactional
	public Iterable<Ad> getNewestAds(int newest) {
		Iterable<Ad> allAds = adDao.findAll();
		List<Ad> ads = new ArrayList<Ad>();
		for (Ad ad : allAds)
			ads.add(ad);
		Collections.sort(ads, new Comparator<Ad>() {
			@Override
			public int compare(Ad ad1, Ad ad2) {
				return ad2.getCreationDate().compareTo(ad1.getCreationDate());
			}
		});
		List<Ad> fourNewest = new ArrayList<Ad>();
		for (int i = 0; i < newest; i++)
			fourNewest.add(ads.get(i));
		return fourNewest;
	}
	
	/**
	 * Returns adds in given coordinates and radius
	 */
	@Transactional
	public Iterable<Ad> getAdsInRadius(double[] coordinates, double radius) {
		Iterable<Ad> allAds = adDao.findAll();
		List<Ad> ads = new ArrayList<Ad>();
		for (Ad ad : allAds) {
			ads.add(ad);
		}
		return ads;
	}

	/**
	 * Returns all ads that match the parameters given by the form. This list
	 * can possibly be empty.
	 * 
	 * @param searchForm
	 *            the form to take the search parameters from
	 * @return an Iterable of all search results
	 */
	@Transactional
	public Iterable<Ad> queryResults(SearchForm searchForm) {
		
		List<Ad> locatedResults = new ArrayList<>();
		int maxPrize = searchForm.getPrize() + 1;
		if(searchForm.getRoomHelper())
			locatedResults = addResultsFromRoomType(locatedResults, "Room", maxPrize);
		if(searchForm.getStudioHelper()) 
			locatedResults = addResultsFromRoomType(locatedResults, "Studio", maxPrize);
		if(searchForm.getFlatHelper())
			locatedResults = addResultsFromRoomType(locatedResults, "Flat", maxPrize);
		if(searchForm.getHouseHelper()) 
			locatedResults = addResultsFromRoomType(locatedResults, "House", maxPrize);

		//if search from map
		List<Location> locations;
		double radSinLat;
		double radCosLat;
		double radLong;
		final int earthRadiusKm = 6380;
		
		if (searchForm.getCity().equals("<<from coordinates>>")) {
			locations = geoDataService.getAllLocations();
			radSinLat = Math.sin(Math.toRadians(searchForm.getLatitude()));
			radCosLat = Math.cos(Math.toRadians(searchForm.getLatitude()));
			radLong = Math.toRadians(searchForm.getLongitude());
		} else {
			// filter out zipcode
			String city = searchForm.getCity().substring(7);
	
			// get the location that the user searched for and take the one with the
			// lowest zip code
			Location searchedLocation = geoDataService.getLocationsByCity(city)
					.get(0);
	
			locations = geoDataService.getAllLocations();
			radSinLat = Math.sin(Math.toRadians(searchedLocation
					.getLatitude()));
			radCosLat = Math.cos(Math.toRadians(searchedLocation
					.getLatitude()));
			radLong = Math.toRadians(searchedLocation.getLongitude());
		}

		/*
		 * calculate the distances (Java 8) and collect all matching zipcodes.
		 * The distance is calculated using the law of cosines.
		 * http://www.movable-type.co.uk/scripts/latlong.html
		 */
		List<Integer> zipcodes = locations
				.parallelStream()
				.filter(location -> {
					double radLongitude = Math.toRadians(location
							.getLongitude());
					double radLatitude = Math.toRadians(location.getLatitude());
					double distance = Math.acos(radSinLat
							* Math.sin(radLatitude) + radCosLat
							* Math.cos(radLatitude)
							* Math.cos(radLong - radLongitude))
							* earthRadiusKm;
					return distance < searchForm.getRadius();
				}).map(location -> location.getZip())
				.collect(Collectors.toList());

		locatedResults = locatedResults.stream()
				.filter(ad -> zipcodes.contains(ad.getZipcode()))
				.collect(Collectors.toList());

		
		// filtering for the special needs
					// buy
					if (searchForm.getBuy() && !searchForm.getRent()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getSaleType().equals("Auction") && !ad.getSaleType().equals("Buy"))
								iterator.remove();
						}
					}

					// rent
					if (searchForm.getRent() && !searchForm.getBuy()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getSaleType().equals("Rent"))
								iterator.remove();
						}
					}
					// smokers
					if (searchForm.getSmokers()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getSmokers())
								iterator.remove();
						}
					}

					// animals
					if (searchForm.getAnimals()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getAnimals())
								iterator.remove();
						}
					}

					// garden
					if (searchForm.getGarden()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getGarden())
								iterator.remove();
						}
					}

					// balcony
					if (searchForm.getBalcony()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getBalcony())
								iterator.remove();
						}
					}

					// cellar
					if (searchForm.getCellar()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getCellar())
								iterator.remove();
						}
					}

					// washingMachine
					if (searchForm.getWashingMachine()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getWashingMachine())
								iterator.remove();
						}
					}

					// dishwasher
					if (searchForm.getDishwasher()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getDishwasher())
								iterator.remove();
						}
					}

					// furnished
					if (searchForm.getFurnished()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getFurnished())
								iterator.remove();
						}
					}

					// cable
					if (searchForm.getCable()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getCable())
								iterator.remove();
						}
					}

					// garage
					if (searchForm.getGarage()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getGarage())
								iterator.remove();
						}
					}

					// internet
					if (searchForm.getInternet()) {
						Iterator<Ad> iterator = locatedResults.iterator();
						while (iterator.hasNext()) {
							Ad ad = iterator.next();
							if (!ad.getInternet())
								iterator.remove();
						}
					}
					
					// remove Auctions already ended
					Iterator<Ad> iterator = locatedResults.iterator();
					while (iterator.hasNext()) {
						Ad ad = iterator.next();
						if (ad.getSaleType().equals("Auction") && ad.getAuctionEnded())
							iterator.remove();
					}
				
			// filter for additional criteria
			// prepare date filtering - by far the most difficult filter
			Date earliestInDate = null;
			Date latestInDate = null;
			Date earliestOutDate = null;
			Date latestOutDate = null;

			// parse move-in and move-out dates
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			try {
				earliestInDate = formatter.parse(searchForm
						.getEarliestMoveInDate());
			} catch (Exception e) {
			}
			try {
				latestInDate = formatter
						.parse(searchForm.getLatestMoveInDate());
			} catch (Exception e) {
			}
			try {
				earliestOutDate = formatter.parse(searchForm
						.getEarliestMoveOutDate());
			} catch (Exception e) {
			}
			try {
				latestOutDate = formatter.parse(searchForm
						.getLatestMoveOutDate());
			} catch (Exception e) {
			}

			// filtering by dates
			locatedResults = validateDate(locatedResults, true, earliestInDate,
					latestInDate);
			locatedResults = validateDate(locatedResults, false,
					earliestOutDate, latestOutDate);
			
		locatedResults = givePriorityToPremium(locatedResults);
		return locatedResults;
	}
	
	private List<Ad> givePriorityToPremium(List<Ad> ads) {
		List<Ad> noPremiumAds = new ArrayList<Ad>();
		Iterator<Ad> iterator = ads.iterator();
		while(iterator.hasNext()) {
			Ad ad = iterator.next();
			User user = ad.getUser();
			if (!user.getPremium()) {
				noPremiumAds.add(ad);
				iterator.remove();
			}
		}
		ads.addAll(noPremiumAds);		
		return ads;
	}

	private List<Ad> validateDate(List<Ad> ads, boolean inOrOut,
			Date earliestDate, Date latestDate) {
		if (ads.size() > 0) {
			// Move-in dates
			// Both an earliest AND a latest date to compare to
			if (earliestDate != null) {
				if (latestDate != null) {
					Iterator<Ad> iterator = ads.iterator();
					while (iterator.hasNext()) {
						Ad ad = iterator.next();
						if (ad.getDate(inOrOut).compareTo(earliestDate) < 0
								|| ad.getDate(inOrOut).compareTo(latestDate) > 0) {
							iterator.remove();
						}
					}
				}
				// only an earliest date
				else {
					Iterator<Ad> iterator = ads.iterator();
					while (iterator.hasNext()) {
						Ad ad = iterator.next();
						if (ad.getDate(inOrOut).compareTo(earliestDate) < 0)
							iterator.remove();
					}
				}
			}
			// only a latest date
			else if (latestDate != null && earliestDate == null) {
				Iterator<Ad> iterator = ads.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getDate(inOrOut).compareTo(latestDate) > 0)
						iterator.remove();
				}
			} else {
			}
		}
		return ads;
	}

	/** Returns all ads that were placed by the given user. */
	public Iterable<Ad> getAdsByUser(User user) {
		return adDao.findByUser(user);
	}

	/**
	 * Checks if the email of a user is already contained in the given string.
	 * 
	 * @param email
	 *            the email string to search for
	 * @param alreadyAdded
	 *            the string of already added emails, which should be searched
	 *            in
	 * 
	 * @return true if the email has been added already, false otherwise
	 */
	public Boolean checkIfAlreadyAdded(String email, String alreadyAdded) {
		email = email.toLowerCase();
		alreadyAdded = alreadyAdded.replaceAll("\\s+", "").toLowerCase();
		String delimiter = "[:;]+";
		String[] toBeTested = alreadyAdded.split(delimiter);
		for (int i = 0; i < toBeTested.length; i++) {
			if (email.equals(toBeTested[i])) {
				return true;
			}
		}
		return false;
	}

	private List<Ad> addResultsFromRoomType(List<Ad> filtredResults, String string, int maxPrize) {
		Iterable<Ad> results = adDao.findByRoomTypeAndPrizePerMonthLessThan(string, maxPrize);
		for(Ad ad : results) {
			filtredResults.add(ad);
		}
		return filtredResults;
	}
	
	public void endMessages(){
		
		Iterable<Ad> ads = getAllAds();
		for(Ad ad : ads){		
			if(ad.getSaleType().equals("Auction")){
			if(ad.getAuctionEnded()){
			if(ad.getAuctionMessage()==false){	

				ad.setAuctionMessage();
				
				User sender = userService.findUserByUsername("System");
				
				Message message;
				message = new Message();
				message.setSubject("Auction: " + ad.getTitle());
				message.setSender(sender);
				message.setRecipient(ad.getUser());
				message.setState(MessageState.UNREAD);
				Calendar calendar = Calendar.getInstance();
				// java.util.Calendar uses a month range of 0-11 instead of the
				// XMLGregorianCalendar which uses 1-12
				calendar.setTimeInMillis(System.currentTimeMillis());
				message.setDateSent(calendar.getTime());
				message.setDateShow(calendar.getTime());
				
				if(ad.getCurrentBuyer()==null){
					message.setText("Your following Auction ended without an interessed Buyer. We are sorry. <a href=\"http://localhost:8080/ad?id="+ad.getId() + "\">"+ ad.getTitle() + ".</a> ");
				}
				else{
					message.setText("Your following Auction ended. Conctatulations on your new sale! <a href=\"http://localhost:8080/ad?id="+ad.getId() + "\">"+ ad.getTitle() + ".</a> ");
				}
				
				messageDao.save(message);
				ad.setAuctionMessage();
				
				message = new Message();
				message.setSubject("Auction: " + ad.getTitle());
				message.setSender(sender);
				message.setRecipient(userService.findUserByUsername(ad.getCurrentBuyer()));
				message.setState(MessageState.UNREAD);
				//Calendar calendar = Calendar.getInstance();
				// java.util.Calendar uses a month range of 0-11 instead of the
				// XMLGregorianCalendar which uses 1-12
				calendar.setTimeInMillis(System.currentTimeMillis());
				message.setDateSent(calendar.getTime());
				message.setDateShow(calendar.getTime());
				message.setText("The following Auction ended leaving you the highest Bidder. Conctatulations on your new purchase! <a href=\"http://localhost:8080/ad?id="+ad.getId() + "\">"+ ad.getTitle() + ".</a> ");
				messageDao.save(message);
				ad.setAuctionMessage();
				adDao.save(ad);
				
				MailService mail = new MailService();
				mail.sendEmail(ad.getCurrentBuyer(),7,"http://localhost:8080/ad?id="+ad.getId());
				mail.sendEmail(ad.getUser().getEmail(),6,"http://localhost:8080/ad?id="+ad.getId());
				
			}	
			}
			}
		}
	}
	
}