package ch.unibe.ese.team1.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.mapping.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team1.controller.MakeAuctionController;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.Location;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration

public class AuctionTest {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdDao adDao;
	
	@Autowired
	private GeoDataService geoDataService;
	
	@Test
	public void TestAuctionController() throws ParseException{
		
		//Preparation: Setting up a user, an ad, pictures and messages.
		ModelAndView model = new ModelAndView("makeAuction");
		
		User herbert = createUser("herbert@labert.ch", "password", "Herbert", "Trabert",
				Gender.MALE);
		herbert.setAboutMe("Herbert der Operator");
		userDao.save(herbert);
		
		User bert = createUser("bert@labert.ch", "passwort", "Bert", "Hilbert",
				Gender.MALE);
		herbert.setAboutMe("Bert der Barde");
		userDao.save(bert);
		

		List<User> regRoommatesAdBern = new LinkedList<User>();
		regRoommatesAdBern.add(herbert);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		Date creationDate1 = formatter.parse("03.10.2014");
		Date moveInDate1 = formatter.parse("15.12.2014");
		Date moveOutDate1 = formatter.parse("31.03.2019");
		Date auctionEndDate1 = formatter.parse("30.09.2017");
		Ad adBern = new Ad();
		adBern.setZipcode(3011);
		adBern.setMoveInDate(moveInDate1);
		adBern.setCreationDate(creationDate1);
		adBern.setMoveOutDate(moveOutDate1);
		adBern.setSaleType("Auction");
		adBern.setEndOfAuction(auctionEndDate1);
		adBern.setPrizePerMonth(400);
		adBern.setRetailPrice(5000);
		adBern.setSquareFootage(50);
		adBern.setRoomType("Room");
		adBern.setRoomDescription("abc");
		adBern.setPreferences("abd");
		adBern.setRoommates("One roommate");
		adBern.setUser(herbert);
		adBern.setRegisteredRoommates(regRoommatesAdBern);
		adBern.setTitle("Roommate wanted in Bern");
		adBern.setStreet("Silbergasse 10");
		adBern.setCity("Bern");

		List<Location> searchedLocations = geoDataService.getLocationsByCity(adBern.getCity());
		if (searchedLocations.size() > 0) {
			Location searchedLocation = geoDataService.getLocationsByCity(adBern.getCity()).get(0);
			adBern.setLatitude(searchedLocation.getLatitude());
			adBern.setLongitude(searchedLocation.getLongitude());		
		} else {
			adBern.setLatitude(-1);
			adBern.setLongitude(-1);
		}
		
		adBern.setSmokers(false);
		adBern.setAnimals(true);
		adBern.setGarden(true);
		adBern.setBalcony(false);
		adBern.setCellar(true);
		adBern.setFurnished(false);
		adBern.setCable(true);
		adBern.setGarage(false);
		adBern.setInternet(true);
		
		adDao.save(adBern);
		model.addObject("ad", adBern);
				
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		
		adBern.setId(1);
		
		Message message = new Message();
		message.setSubject("abc");
		message.setSender(herbert);
		message.setRecipient(bert);
		message.setState(MessageState.UNREAD);
		message.setDateSent(calendar.getTime());
		message.setDateShow(calendar.getTime());
		
		adBern.setCurrentBidding(adBern.getNextPossibleBid());
		adBern.setCurrentBuyer("bert@labert.ch");
		adBern.addFifteenMinutesToAuctionEndedIfNecessary();
		
		String endOfAuctionString = formatter.format(adBern.getEndOfAuction());
		String currentTimeString = formatter.format(calendar.getTime());
		int endOfAuctionInt = Integer.parseInt(endOfAuctionString);
		int currentTimeInt = Integer.parseInt(currentTimeString);
		
		
		//Testing
		assertEquals(1, adBern.getId());
		assertEquals("abc", message.getSubject());
		assertEquals(herbert, message.getSender());
		assertEquals(bert, message.getRecipient());
		assertEquals(MessageState.UNREAD, message.getState());
		assertEquals(calendar.getTime(), message.getDateSent());
		assertEquals(calendar.getTime(), message.getDateShow());
		assertTrue(endOfAuctionInt > currentTimeInt + 850000);
	}
	
	private User createUser(String email, String password, String firstName,
			String lastName, Gender gender) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);
		return user;
	}
}

