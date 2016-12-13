package ch.unibe.ese.team1.test.testData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.service.GeoDataService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.Location;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.UserDao;

/** This inserts several ad elements into the database. */
@Service
public class AdTestDataSaver {

	@Autowired
	private AdDao adDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private GeoDataService geoDataService;

	@Transactional
	public void saveTestData() throws Exception {
		User bernerBaer = userDao.findByUsername("user@bern.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		User oprah = userDao.findByUsername("oprah@winfrey.com");
		User jane = userDao.findByUsername("jane@doe.com");
		User hans = userDao.findByUsername("hans@unibe.ch");
		User mathilda = userDao.findByUsername("mathilda@unibe.ch");
		User premium = userDao.findByUsername("premiumKing@premium.ch");
		
		List<User> regRoommatesAdBern = new LinkedList<User>();
		regRoommatesAdBern.add(hans);
		regRoommatesAdBern.add(mathilda);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		
		Date creationDate1 = formatter.parse("03.10.2014");
		Date creationDate2 = formatter.parse("11.10.2014");
		Date creationDate3 = formatter.parse("25.10.2014");
		Date creationDate4 = formatter.parse("02.11.2014");
		Date creationDate5 = formatter.parse("25.11.2013");
		Date creationDate6 = formatter.parse("01.12.2014");
		Date creationDate7 = formatter.parse("16.11.2014");
		Date creationDate8 = formatter.parse("27.11.2014");
		
		Date moveInDate1 = formatter.parse("15.12.2014");
		Date moveInDate2 = formatter.parse("21.12.2014");
		Date moveInDate3 = formatter.parse("01.01.2015");
		Date moveInDate4 = formatter.parse("15.01.2015");
		Date moveInDate5 = formatter.parse("01.02.2015");
		Date moveInDate6 = formatter.parse("01.03.2015");
		Date moveInDate7 = formatter.parse("15.03.2015");
		Date moveInDate8 = formatter.parse("16.02.2015");
		Date moveInDate9 = formatter.parse("01.01.2017");
		
		Date moveOutDate1 = formatter.parse("31.03.2015");
		Date moveOutDate2 = formatter.parse("30.04.2015");
		Date moveOutDate3 = formatter.parse("31.03.2016");
		Date moveOutDate4 = formatter.parse("01.07.2015");
		Date moveOutDate5 = formatter.parse("30.09.2016");
		
		Date auctionEndDate1 = formatter.parse("30.09.2016");
		Date auctionEndDate2 = formatter.parse("30.09.2017");
		
		
		
		String roomDescription1 = "The room is a part of 3.5 rooms apartment completely renovated"
				+ "in 2010 at Kramgasse, Bern. The apartment is about 50 m2 on 1st floor."
				+ "Apt is equipped modern kitchen, hall and balcony. Near to shops and public"
				+ "transportation. Monthly rent is 500 CHF including charges. Internet + TV + landline"
				+ "charges are separate. If you are interested, feel free to drop me a message"
				+ "to have an appointment for a visit or can write me for any further information";
		String preferences1 = "Uncomplicated, open minded and easy going person (m / w),"
				+ "non-smoker, can speak English, which of course fits in the WG, and who likes dogs."
				+ "Cleanliness is must. Apart from personal life, sometimes glass of wine,"
				+ "eat and cook together and go out in the evenings.";

		Ad adBern = new Ad();
		adBern.setZipcode(3011);
		adBern.setMoveInDate(moveInDate1);
		adBern.setCreationDate(creationDate1);
		adBern.setMoveOutDate(moveOutDate1);
		adBern.setSaleType("Buy");
		adBern.setPrizePerMonth(400);
		adBern.setRetailPrice(1000000);
		adBern.setSquareFootage(50);
		adBern.setRoomType("Room");
		adBern.setSmokers(false);
		adBern.setAnimals(true);
		adBern.setRoomDescription(roomDescription1);
		adBern.setPreferences(preferences1);
		adBern.setRoommates("One roommate");
		adBern.setUser(bernerBaer);
		adBern.setRegisteredRoommates(regRoommatesAdBern);
		adBern.setTitle("Roommate wanted in Bern");
		adBern.setStreet("Kramgasse 22");
		adBern.setCity("Bern");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		List<Location> searchedLocations = geoDataService.getLocationsByCity(adBern.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adBern.getCity()).get(0);
			adBern.setLatitude(searchedLocation.getLatitude());
			adBern.setLongitude(searchedLocation.getLongitude());		
		} else {
			adBern.setLatitude(-1);
			adBern.setLongitude(-1);
		}
		adBern.setGarden(true);
		adBern.setBalcony(true);
		adBern.setCellar(true);
		adBern.setFurnished(true);
		adBern.setWashingMachine(false);
		adBern.setDishwasher(true);
		adBern.setCable(true);
		adBern.setGarage(true);
		adBern.setInternet(true);
		List<AdPicture> pictures = new ArrayList<>();
		pictures.add(createPicture(adBern, "/img/test/ad1_1.jpg"));
		pictures.add(createPicture(adBern, "/img/test/ad1_2.jpg"));
		pictures.add(createPicture(adBern, "/img/test/ad1_3.jpg"));
		adBern.setPictures(pictures);
		adDao.save(adBern);

		String studioDescription2 = "It is small studio close to the"
				+ "university and the bahnhof. The lovely neighbourhood"
				+ "Langgasse makes it an easy place to feel comfortable."
				+ "The studio is close to a Migross, Denner and the Coop."
				+ "The studio is 60m2. It has it own Badroom and kitchen."
				+ "Nothing is shared. The studio is fully furnished. The"
				+ "studio is also provided with a balcony. So if you want to"
				+ "have a privat space this could totally be good place for you."
				+ "Be aware it is only till the end of March. The price is from"
				+ "550- 700 CHF, But there is always room to talk about it.";
		String roomPreferences2 = "I would like to have an easy going person who"
				+ "is trustworthy and can take care of the flat. No animals please."
				+ "Non smoker preferred.";
		
		Ad adBern2 = new Ad();
		adBern2.setZipcode(3012);
		adBern2.setMoveInDate(moveInDate2);
		adBern2.setCreationDate(creationDate2);
		adBern2.setMoveOutDate(moveOutDate4);
		adBern2.setSaleType("Rent");
		adBern2.setPrizePerMonth(700);
		adBern2.setRetailPrice(2000000);
		adBern2.setSquareFootage(60);
		adBern2.setRoomType("Studio");
		adBern2.setSmokers(false);
		adBern2.setAnimals(true);
		adBern2.setRoomDescription(studioDescription2);
		adBern2.setPreferences(roomPreferences2);
		adBern2.setRoommates("None");
		adBern2.setUser(ese);
		adBern2.setTitle("Cheap studio in Bern!");
		adBern2.setStreet("Längassstr. 40");
		adBern2.setCity("Bern");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adBern2.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adBern2.getCity()).get(0);
			adBern2.setLatitude(searchedLocation.getLatitude());
			adBern2.setLongitude(searchedLocation.getLongitude());		
		} else {
			adBern2.setLatitude(-1);
			adBern2.setLongitude(-1);
		}
		adBern2.setGarden(false);
		adBern2.setBalcony(false);
		adBern2.setCellar(false);
		adBern2.setFurnished(false);
		adBern2.setWashingMachine(false);
		adBern2.setDishwasher(false);
		adBern2.setCable(false);
		adBern2.setGarage(false);
		adBern2.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBern2, "/img/test/ad2_1.jpg"));
		pictures.add(createPicture(adBern2, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adBern2, "/img/test/ad2_3.jpg"));
		adBern2.setPictures(pictures);
		adDao.save(adBern2);

		String studioDescription3 = " In the center of Gundeli (5 Min. away from the"
				+ "station, close to Tellplatz) there is a lovely house, covered with"
				+ "savage wine stocks, without any neighbours but with a garden. The"
				+ "house has two storey, on the first floor your new room is waiting"
				+ "for you. The house is totally equipped with everything a household "
				+ ": washing machine, kitchen, batroom, W-Lan...if you don´t have any"
				+ "furniture, don´t worry, I am sure, we will find something around"
				+ "the house. The price for the room and all included is 480 CHF /month. "
				+ " (29, Graphic designer) and Linda (31, curator) are looking for a"
				+ "new female flatmate from December on.";
		String roomPreferences3 = "smoking female flatmate";
		
		Ad adBasel = new Ad();
		adBasel.setZipcode(4051);
		adBasel.setMoveInDate(moveInDate3);
		adBasel.setMoveOutDate(moveOutDate2);
		adBasel.setCreationDate(creationDate3);
		adBasel.setSaleType("Auction");
		adBasel.setEndOfAuction(auctionEndDate1);
		adBasel.setPrizePerMonth(480);
		adBasel.setRetailPrice(3000000);
		adBasel.setSquareFootage(10);
		adBasel.setRoomType("Studio");
		adBasel.setSmokers(true);
		adBasel.setAnimals(false);
		adBasel.setRoomDescription(studioDescription3);
		adBasel.setPreferences(roomPreferences3);
		adBasel.setRoommates("None");
		adBasel.setUser(bernerBaer);
		adBasel.setTitle("Nice, bright studio in the center of Basel");
		adBasel.setStreet("Bruderholzstrasse 32");
		adBasel.setCity("Basel");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adBasel.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adBasel.getCity()).get(0);
			adBasel.setLatitude(searchedLocation.getLatitude());
			adBasel.setLongitude(searchedLocation.getLongitude());		
		} else {
			adBasel.setLatitude(-1);
			adBasel.setLongitude(-1);
		}
		adBasel.setGarden(false);
		adBasel.setBalcony(false);
		adBasel.setCellar(false);
		adBasel.setWashingMachine(true);
		adBasel.setDishwasher(true);
		adBasel.setFurnished(false);
		adBasel.setCable(false);
		adBasel.setGarage(false);
		adBasel.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBasel, "/img/test/ad3_1.jpg"));
		pictures.add(createPicture(adBasel, "/img/test/ad3_2.jpg"));
		pictures.add(createPicture(adBasel, "/img/test/ad3_3.jpg"));
		adBasel.setPictures(pictures);
		adDao.save(adBasel);
		
		String studioDescription4 = "Flatshare of 3 persons. Flat with 5 rooms"
				+ "on the second floor. The bedroom is about 60 square meters"
				+ "with access to a nice balcony. In addition to the room, the"
				+ "flat has: a living room, a kitchen, a bathroom, a seperate WC,"
				+ "a storage in the basement, a balcony, a laundry room in the basement."
				+ "The bedroom is big and bright and has a nice parquet floor."
				+ "Possibility to keep some furnitures like the bed.";
		String roomPreferences4 = "an easy going flatmate man or woman between 20 and 30";
		
		Ad adOlten = new Ad();
		adOlten.setZipcode(4600);
		adOlten.setMoveInDate(moveInDate4);
		adOlten.setCreationDate(creationDate4);
		adOlten.setSaleType("Buy");
		adOlten.setPrizePerMonth(430);
		adOlten.setRetailPrice(43000);
		adOlten.setSquareFootage(60);
		adOlten.setRoomType("Room");
		adOlten.setSmokers(true);
		adOlten.setAnimals(false);
		adOlten.setRoomDescription(studioDescription4);
		adOlten.setPreferences(roomPreferences4);
		adOlten.setRoommates("One roommate");
		adOlten.setUser(ese);
		adOlten.setTitle("Roommate wanted in Olten City");
		adOlten.setStreet("Zehnderweg 5");
		adOlten.setCity("Olten");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adOlten.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adOlten.getCity()).get(0);
			adOlten.setLatitude(searchedLocation.getLatitude());
			adOlten.setLongitude(searchedLocation.getLongitude());		
		} else {
			adOlten.setLatitude(-1);
			adOlten.setLongitude(-1);
		}
		adOlten.setGarden(false);
		adOlten.setBalcony(true);
		adOlten.setCellar(true);
		adOlten.setFurnished(true);
		adOlten.setWashingMachine(true);
		adOlten.setDishwasher(false);
		adOlten.setCable(true);
		adOlten.setGarage(false);
		adOlten.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adOlten, "/img/test/ad4_1.png"));
		pictures.add(createPicture(adOlten, "/img/test/ad4_2.png"));
		pictures.add(createPicture(adOlten, "/img/test/ad4_3.png"));
		adOlten.setPictures(pictures);
		adDao.save(adOlten);

		String studioDescription5 = "Studio meublé au 3ème étage, comprenant"
				+ "une kitchenette entièrement équipée (frigo, plaques,"
				+ "four et hotte), une pièce à vivre donnant sur un balcon,"
				+ "une salle de bains avec wc. Cave, buanderie et site satellite"
				+ "à disposition.";
		String roomPreferences5 = "tout le monde est bienvenu";
		
		Ad adNeuchâtel = new Ad();
		adNeuchâtel.setZipcode(2000);
		adNeuchâtel.setMoveInDate(moveInDate5);
		adNeuchâtel.setMoveOutDate(moveOutDate3);
		adNeuchâtel.setCreationDate(creationDate5);
		adNeuchâtel.setSaleType("Buy");
		adNeuchâtel.setPrizePerMonth(410);
		adNeuchâtel.setRetailPrice(41000);
		adNeuchâtel.setSquareFootage(40);
		adNeuchâtel.setRoomType("Studio");
		adNeuchâtel.setSmokers(true);
		adNeuchâtel.setAnimals(false);
		adNeuchâtel.setRoomDescription(studioDescription5);
		adNeuchâtel.setPreferences(roomPreferences5);
		adNeuchâtel.setRoommates("None");
		adNeuchâtel.setUser(bernerBaer);
		adNeuchâtel.setTitle("Studio extrèmement bon marché à Neuchâtel");
		adNeuchâtel.setStreet("Rue de l'Hôpital 11");
		adNeuchâtel.setCity("Neuchâtel");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adNeuchâtel.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adNeuchâtel.getCity()).get(0);
			adNeuchâtel.setLatitude(searchedLocation.getLatitude());
			adNeuchâtel.setLongitude(searchedLocation.getLongitude());		
		} else {
			adNeuchâtel.setLatitude(-1);
			adNeuchâtel.setLongitude(-1);
		}
		adNeuchâtel.setGarden(true);
		adNeuchâtel.setBalcony(false);
		adNeuchâtel.setCellar(true);
		adNeuchâtel.setFurnished(true);
		adNeuchâtel.setCable(false);
		adNeuchâtel.setWashingMachine(true);
		adNeuchâtel.setDishwasher(true);
		adNeuchâtel.setGarage(false);
		adNeuchâtel.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_1.jpg"));
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_2.jpg"));
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_3.jpg"));
		adNeuchâtel.setPictures(pictures);
		adDao.save(adNeuchâtel);

		String studioDescription6 = "A place just for yourself in a very nice part of Biel."
				+ "A studio for 1-2 persons with a big balcony, bathroom, kitchen and furniture already there."
				+ "It's quiet and nice, very close to the old city of Biel.";
		String roomPreferences6 = "A nice and easy going person. Minimum rent is two months";
		
		Ad adBiel = new Ad();
		adBiel.setZipcode(2503);
		adBiel.setMoveInDate(moveInDate6);
		adBiel.setMoveOutDate(moveOutDate5);
		adBiel.setCreationDate(creationDate6);
		adBiel.setSaleType("Auction");
		adBiel.setEndOfAuction(auctionEndDate1);
		adBiel.setPrizePerMonth(480);
		adBiel.setRetailPrice(48000);
		adBiel.setSquareFootage(10);
		adBiel.setRoomType("Studio");
		adBiel.setSmokers(true);
		adBiel.setAnimals(false);
		adBiel.setRoomDescription(studioDescription6);
		adBiel.setPreferences(roomPreferences6);
		adBiel.setRoommates("None");
		adBiel.setUser(ese);
		adBiel.setTitle("Direkt am Quai: hübsches Studio");
		adBiel.setStreet("Oberer Quai 12");
		adBiel.setCity("Biel/Bienne");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adBiel.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adBiel.getCity()).get(0);
			adBiel.setLatitude(searchedLocation.getLatitude());
			adBiel.setLongitude(searchedLocation.getLongitude());		
		} else {
			adBiel.setLatitude(-1);
			adBiel.setLongitude(-1);
		}
		adBiel.setGarden(false);
		adBiel.setBalcony(false);
		adBiel.setCellar(false);
		adBiel.setFurnished(false);
		adBiel.setCable(false);
		adBiel.setWashingMachine(true);
		adBiel.setDishwasher(true);
		adBiel.setGarage(false);
		adBiel.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBiel, "/img/test/ad6_1.png"));
		pictures.add(createPicture(adBiel, "/img/test/ad6_2.png"));
		pictures.add(createPicture(adBiel, "/img/test/ad6_3.png"));
		adBiel.setPictures(pictures);
		adDao.save(adBiel);
		
		
		String roomDescription7 = "The room is a part of 3.5 rooms apartment completely renovated"
				+ "in 2010 at Kramgasse, Bern. The apartment is about 50 m2 on 1st floor."
				+ "Apt is equipped modern kitchen, hall and balcony. Near to shops and public"
				+ "transportation. Monthly rent is 500 CHF including charges. Internet + TV + landline"
				+ "charges are separate. If you are interested, feel free to drop me a message"
				+ "to have an appointment for a visit or can write me for any further information";
		String preferences7 = "Uncomplicated, open minded and easy going person (m / w),"
				+ "non-smoker, can speak English, which of course fits in the WG, and who likes dogs."
				+ "Cleanliness is must. Apart from personal life, sometimes glass of wine,"
				+ "eat and cook together and go out in the evenings.";

		Ad adZurich = new Ad();
		adZurich.setZipcode(8000);
		adZurich.setMoveInDate(moveInDate7);
		adZurich.setCreationDate(creationDate7);
		adZurich.setMoveOutDate(moveOutDate5);
		adZurich.setSaleType("Rent");
		adZurich.setPrizePerMonth(480);
		adZurich.setRetailPrice(4800);
		adZurich.setSquareFootage(32);
		adZurich.setRoomType("Room");
		adZurich.setSmokers(false);
		adZurich.setAnimals(false);
		adZurich.setRoomDescription(roomDescription7);
		adZurich.setPreferences(preferences7);
		adZurich.setRoommates("One roommate");
		adZurich.setUser(oprah);
		adZurich.setTitle("Roommate wanted in Zürich");
		adZurich.setStreet("Hauptstrasse 61");
		adZurich.setCity("Zürich");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adZurich.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adZurich.getCity()).get(0);
			adZurich.setLatitude(searchedLocation.getLatitude());
			adZurich.setLongitude(searchedLocation.getLongitude());		
		} else {
			adZurich.setLatitude(-1);
			adZurich.setLongitude(-1);
		}
		adZurich.setGarden(false);
		adZurich.setBalcony(true);
		adZurich.setCellar(false);
		adZurich.setFurnished(true);
		adZurich.setWashingMachine(true);
		adZurich.setDishwasher(true);
		adZurich.setCable(true);
		adZurich.setGarage(true);
		adZurich.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adZurich, "/img/test/ad1_3.jpg"));
		pictures.add(createPicture(adZurich, "/img/test/ad1_2.jpg"));
		pictures.add(createPicture(adZurich, "/img/test/ad1_1.jpg"));
		adZurich.setPictures(pictures);
		adDao.save(adZurich);
	
		
		String studioDescription8 = "It is small studio close to the"
				+ "university and the bahnhof. The lovely neighbourhood"
				+ "Langgasse makes it an easy place to feel comfortable."
				+ "The studio is close to a Migross, Denner and the Coop."
				+ "The studio is 60m2. It has it own Badroom and kitchen."
				+ "Nothing is shared. The studio is fully furnished. The"
				+ "studio is also provided with a balcony. So if you want to"
				+ "have a privat space this could totally be good place for you."
				+ "Be aware it is only till the end of March. The price is from"
				+ "550- 700 CHF, But there is always room to talk about it.";
		String roomPreferences8 = "I would like to have an easy going person who"
				+ "is trustworthy and can take care of the flat. No animals please."
				+ "Non smoker preferred.";
		
		Ad adLuzern = new Ad();
		adLuzern.setZipcode(6000);
		adLuzern.setMoveInDate(moveInDate8);
		adLuzern.setCreationDate(creationDate2);
		adLuzern.setSaleType("Buy");
		adLuzern.setPrizePerMonth(700);
		adLuzern.setRetailPrice(70000);
		adLuzern.setSquareFootage(60);
		adLuzern.setRoomType("Studio");
		adLuzern.setSmokers(false);
		adLuzern.setAnimals(false);
		adLuzern.setRoomDescription(studioDescription8);
		adLuzern.setPreferences(roomPreferences8);
		adLuzern.setRoommates("None");
		adLuzern.setUser(oprah);
		adLuzern.setTitle("Elegant Studio in Lucerne");
		adLuzern.setStreet("Schwanenplatz 61");
		adLuzern.setCity("Luzern");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adLuzern.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adLuzern.getCity()).get(0);
			adLuzern.setLatitude(searchedLocation.getLatitude());
			adLuzern.setLongitude(searchedLocation.getLongitude());		
		} else {
			adLuzern.setLatitude(-1);
			adLuzern.setLongitude(-1);
		}
		adLuzern.setGarden(false);
		adLuzern.setBalcony(false);
		adLuzern.setCellar(false);
		adLuzern.setFurnished(false);
		adLuzern.setWashingMachine(true);
		adLuzern.setDishwasher(true);
		adLuzern.setCable(false);
		adLuzern.setGarage(false);
		adLuzern.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLuzern, "/img/test/ad2_3.jpg"));
		pictures.add(createPicture(adLuzern, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adLuzern, "/img/test/ad2_1.jpg"));
		adLuzern.setPictures(pictures);
		adDao.save(adLuzern);

		String studioDescription9 = " In the center of Gundeli (5 Min. away from the"
				+ "station, close to Tellplatz) there is a lovely house, covered with"
				+ "savage wine stocks, without any neighbours but with a garden. The"
				+ "house has two storey, on the first floor your new room is waiting"
				+ "for you. The house is totally equipped with everything a household "
				+ ": washing machine, kitchen, batroom, W-Lan...if you don´t have any"
				+ "furniture, don´t worry, I am sure, we will find something around"
				+ "the house. The price for the room and all included is 480 CHF /month. "
				+ " (29, Graphic designer) and Linda (31, curator) are looking for a"
				+ "new female flatmate from December on.";
		String roomPreferences9 = "smoking female flatmate";
		
		Ad adAarau = new Ad();
		adAarau.setZipcode(5000);
		adAarau.setMoveInDate(moveInDate3);
		adAarau.setMoveOutDate(moveOutDate4);
		adAarau.setCreationDate(creationDate8);
		adAarau.setSaleType("Auction");
		adAarau.setEndOfAuction(auctionEndDate2);
		adAarau.setPrizePerMonth(800);
		adAarau.setRetailPrice(80000);
		adAarau.setSquareFootage(26);
		adAarau.setRoomType("Studio");
		adAarau.setSmokers(true);
		adAarau.setAnimals(false);
		adAarau.setRoomDescription(studioDescription9);
		adAarau.setPreferences(roomPreferences9);
		adAarau.setRoommates("None");
		adAarau.setUser(oprah);
		adAarau.setTitle("Beautiful studio in Aarau");
		adAarau.setStreet("Bruderholzstrasse 32");
		adAarau.setCity("Aarau");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adAarau.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adAarau.getCity()).get(0);
			adAarau.setLatitude(searchedLocation.getLatitude());
			adAarau.setLongitude(searchedLocation.getLongitude());		
		} else {
			adAarau.setLatitude(-1);
			adAarau.setLongitude(-1);
		}
		adAarau.setGarden(false);
		adAarau.setBalcony(true);
		adAarau.setCellar(false);
		adAarau.setFurnished(true);
		adAarau.setWashingMachine(true);
		adAarau.setDishwasher(true);
		adAarau.setCable(false);
		adAarau.setGarage(false);
		adAarau.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adAarau, "/img/test/ad3_3.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad3_2.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad3_1.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad2_3.jpg"));
		
		adAarau.setPictures(pictures);
		adDao.save(adAarau);
		
		String studioDescription10 = "Flatshare of 3 persons. Flat with 5 rooms"
				+ "on the second floor. The bedroom is about 60 square meters"
				+ "with access to a nice balcony. In addition to the room, the"
				+ "flat has: a living room, a kitchen, a bathroom, a seperate WC,"
				+ "a storage in the basement, a balcony, a laundry room in the basement."
				+ "The bedroom is big and bright and has a nice parquet floor."
				+ "Possibility to keep some furnitures like the bed.";
		String roomPreferences10 = "an easy going flatmate man or woman between 20 and 30";
		
		Ad adDavos = new Ad();
		adDavos.setZipcode(7260);
		adDavos.setMoveInDate(moveInDate2);
		adDavos.setCreationDate(creationDate4);
		adDavos.setSaleType("Rent");
		adDavos.setPrizePerMonth(1100);
		adDavos.setRetailPrice(11000);
		adDavos.setSquareFootage(74);
		adDavos.setRoomType("Room");
		adDavos.setSmokers(true);
		adDavos.setAnimals(false);
		adDavos.setRoomDescription(studioDescription10);
		adDavos.setPreferences(roomPreferences10);
		adDavos.setRoommates("One roommate");
		adDavos.setUser(oprah);
		adDavos.setTitle("Free room in Davos City");
		adDavos.setStreet("Kathrinerweg 5");
		adDavos.setCity("Davos");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adDavos.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adDavos.getCity()).get(0);
			adDavos.setLatitude(searchedLocation.getLatitude());
			adDavos.setLongitude(searchedLocation.getLongitude());		
		} else {
			adDavos.setLatitude(-1);
			adDavos.setLongitude(-1);
		}
		adDavos.setGarden(false);
		adDavos.setBalcony(true);
		adDavos.setCellar(true);
		adDavos.setFurnished(true);
		adDavos.setWashingMachine(true);
		adDavos.setDishwasher(true);
		adDavos.setCable(true);
		adDavos.setGarage(false);
		adDavos.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adDavos, "/img/test/ad4_3.png"));
		pictures.add(createPicture(adDavos, "/img/test/ad4_2.png"));
		pictures.add(createPicture(adDavos, "/img/test/ad4_1.png"));
		adDavos.setPictures(pictures);
		adDao.save(adDavos);

		String studioDescription11 = "Studio meublé au 3ème étage, comprenant"
				+ "une kitchenette entièrement équipée (frigo, plaques,"
				+ "four et hotte), une pièce à vivre donnant sur un balcon,"
				+ "une salle de bains avec wc. Cave, buanderie et site satellite"
				+ "à disposition.";
		String roomPreferences11 = "tout le monde est bienvenu";
		
		Ad adLausanne = new Ad();
		adLausanne.setZipcode(1000);
		adLausanne.setMoveInDate(moveInDate5);
		adLausanne.setMoveOutDate(moveOutDate3);
		adLausanne.setCreationDate(creationDate5);
		adLausanne.setSaleType("Buy");
		adLausanne.setPrizePerMonth(360);
		adLausanne.setRetailPrice(36000);
		adLausanne.setSquareFootage(8);
		adLausanne.setRoomType("Room");
		adLausanne.setSmokers(true);
		adLausanne.setAnimals(false);
		adLausanne.setRoomDescription(studioDescription11);
		adLausanne.setPreferences(roomPreferences11);
		adLausanne.setRoommates("None");
		adLausanne.setUser(oprah);
		adLausanne.setTitle("Studio extrèmement bon marché à Lausanne");
		adLausanne.setStreet("Rue de l'Eglise 26");
		adLausanne.setCity("Lausanne");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adLausanne.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adLausanne.getCity()).get(0);
			adLausanne.setLatitude(searchedLocation.getLatitude());
			adLausanne.setLongitude(searchedLocation.getLongitude());		
		} else {
			adLausanne.setLatitude(-1);
			adLausanne.setLongitude(-1);
		}
		adLausanne.setGarden(true);
		adLausanne.setBalcony(false);
		adLausanne.setCellar(true);
		adLausanne.setFurnished(true);
		adLausanne.setWashingMachine(true);
		adLausanne.setDishwasher(true);
		adLausanne.setCable(false);
		adLausanne.setGarage(false);
		adLausanne.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLausanne, "/img/test/ad5_3.jpg"));
		pictures.add(createPicture(adLausanne, "/img/test/ad5_2.jpg"));
		pictures.add(createPicture(adLausanne, "/img/test/ad5_1.jpg"));
		adLausanne.setPictures(pictures);
		adDao.save(adLausanne);

		String studioDescription12 = "A place just for yourself in a very nice part of Biel."
				+ "A studio for 1-2 persons with a big balcony, bathroom, kitchen and furniture already there."
				+ "It's quiet and nice, very close to the old city of Biel.";
		String roomPreferences12 = "A nice and easy going person. Minimum rent is two months";
		
		Ad adLocarno = new Ad();
		adLocarno.setZipcode(6600);
		adLocarno.setMoveInDate(moveInDate6);
		adLocarno.setMoveOutDate(moveOutDate5);
		adLocarno.setCreationDate(creationDate6);
		adLocarno.setSaleType("Auction");
		adLocarno.setEndOfAuction(auctionEndDate2);
		adLocarno.setPrizePerMonth(960);
		adLocarno.setCurrentBidding(1500);
		adLocarno.setRetailPrice(9600);
		adLocarno.setSquareFootage(42);
		adLocarno.setRoomType("Room");
		adLocarno.setSmokers(true);
		adLocarno.setAnimals(false);
		adLocarno.setRoomDescription(studioDescription12);
		adLocarno.setPreferences(roomPreferences12);
		adLocarno.setRoommates("None");
		adLocarno.setUser(jane);
		adLocarno.setTitle("Malibu-style Beachhouse");
		adLocarno.setStreet("Kirchweg 12");
		adLocarno.setCity("Locarno");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adLocarno.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adLocarno.getCity()).get(0);
			adLocarno.setLatitude(searchedLocation.getLatitude());
			adLocarno.setLongitude(searchedLocation.getLongitude());		
		} else {
			adLocarno.setLatitude(-1);
			adLocarno.setLongitude(-1);
		}
		adLocarno.setGarden(false);
		adLocarno.setBalcony(false);
		adLocarno.setCellar(false);
		adLocarno.setFurnished(false);
		adLocarno.setWashingMachine(true);
		adLocarno.setDishwasher(true);
		adLocarno.setCable(false);
		adLocarno.setGarage(false);
		adLocarno.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLocarno, "/img/test/ad6_3.png"));
		pictures.add(createPicture(adLocarno, "/img/test/ad6_2.png"));
		pictures.add(createPicture(adLocarno, "/img/test/ad6_1.png"));
		adLocarno.setPictures(pictures);
		adDao.save(adLocarno);

		String studioDescription13 = "Villa Miragalli is a house of 240 square meters (2583 sq.ft) that sits on a property of 10,000 square meters (108,000 sq.ft)."
				+ "It has several balconies and terraces; and is suitable for a maximum of ten people (8+2)."
				+ "The Villa has two floors, with four bedrooms (2 master suites and 2 guest rooms) and four bathrooms, as well as a 4000 square meter (43,000 sq. ft) garden. Exotic Ipé hardwood flooring is laid throughout the house."
				+ "The Villa is set behind an automatic wide gate with plenty of covered parking spaces.  From the parking area, the house can be easily accessed (just one step) for all types of guests."
				+ "Villa Miragalli is fully furnished with personalized A/C and heating throughout the entire house and bedrooms, as well as a hot tub (which can be enjoyed all year), an infinity swimming pool, outdoor kitchen, gazebo, barbecue, hammock, Sky TV, Playstation 3, home theatre system, iPod station, alarm, free fast Wi-Fi (bring your laptop or smartphone), and all the comforts you need to make this your home away from home.";
		String roomPreferences13 = "Shut up and buy it.";
		
		Ad adLyss = new Ad();
		adLyss.setZipcode(3250);
		adLyss.setMoveInDate(moveInDate9);
		adLyss.setCreationDate(creationDate6);
		adLyss.setSaleType("Auction");
		adLyss.setEndOfAuction(auctionEndDate2);
		adLyss.setPrizePerMonth(960);
		adLyss.setCurrentBidding(1000000);
		adLyss.setRetailPrice(1600000);
		adLyss.setSquareFootage(240);
		adLyss.setRoomType("House");
		adLyss.setSmokers(true);
		adLyss.setAnimals(true);
		adLyss.setRoomDescription(studioDescription13);
		adLyss.setPreferences(roomPreferences13);
		adLyss.setRoommates("None");
		adLyss.setUser(jane);
		adLyss.setTitle("Villa Miragalli");
		adLyss.setStreet("Tulpenweg 23");
		adLyss.setCity("Lyss");
		//add location to add
		//set -1 to lon and -1 to lat if no results exist
		searchedLocations = geoDataService.getLocationsByCity(adLyss.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adLyss.getCity()).get(0);
			adLyss.setLatitude(searchedLocation.getLatitude());
			adLyss.setLongitude(searchedLocation.getLongitude());		
		} else {
			adLyss.setLatitude(-1);
			adLyss.setLongitude(-1);
		}
		adLyss.setGarden(true);
		adLyss.setBalcony(true);
		adLyss.setCellar(true);
		adLyss.setFurnished(true);
		adLyss.setWashingMachine(true);
		adLyss.setDishwasher(true);
		adLyss.setCable(true);
		adLyss.setGarage(true);
		adLyss.setInternet(true);
		adLyss.setUser(premium);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLyss, "/img/test/ad13_3.jpg"));
		pictures.add(createPicture(adLyss, "/img/test/ad13_2.jpg"));
		pictures.add(createPicture(adLyss, "/img/test/ad13_1.jpg"));
		adLyss.setPictures(pictures);
		adDao.save(adLyss);

	}

	private AdPicture createPicture(Ad ad, String filePath) {
		AdPicture picture = new AdPicture();
		picture.setFilePath(filePath);
		return picture;
	}

}