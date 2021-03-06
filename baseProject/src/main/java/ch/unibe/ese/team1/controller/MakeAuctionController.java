package ch.unibe.ese.team1.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team1.controller.pojos.MailService;
import ch.unibe.ese.team1.controller.pojos.PictureUploader;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.EditAdService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;

import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;


/**
 * This controller handles all requests concerning editing ads.
 */
@Controller
public class MakeAuctionController {

	private final static String IMAGE_DIRECTORY = PlaceAdController.IMAGE_DIRECTORY;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private AdService adService;

	@Autowired
	private EditAdService editAdService;

	@Autowired
	private UserService userService;

	private PictureUploader pictureUploader;
	
	@Autowired
	private MessageDao messageDao;

	/**
	 * Serves the page that allows the user to edit the ad with the given id.
	 */
	@RequestMapping(value = "/makeAuction", method = RequestMethod.GET)
	public ModelAndView editAdPage(@RequestParam long id, Principal principal) {

		adService.endMessages();
		
		ModelAndView model = new ModelAndView("makeAuction");
		Ad ad = adService.getAdById(id);
		model.addObject("ad", ad);

		PlaceAdForm form = editAdService.fillForm(ad);

		model.addObject("placeAdForm", form);

		String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
		if (pictureUploader == null) {
			pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
		}		

		return model;
	}

	/**
	 * Processes the bids and buy-outs and sends messages.
	 * 
	 * Principal principal
	 */
	@RequestMapping(value = "/makeAuction", method = RequestMethod.POST)
	public String bid(@RequestParam Map<String,String> requestParams, Principal principal, RedirectAttributes redirectAttributes) {
		String strid=requestParams.get("id");
		String strprice=requestParams.get("price");
		long id = Long.parseLong(strid);
		
		int price;
		try{
			price = Integer.parseInt(strprice);
		}
		catch (Exception name) {	
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"There was an error! Pleas make sure you place a valid bid!");
			return "redirect:/ad?id="+id;
		}
		
		Ad ad = adService.getAdById(id);
		ad.addFifteenMinutesToAuctionEndedIfNecessary();
		
		if (price >= ad.getNextPossibleBid()) {
			ad.setCurrentBidding(price);
			
			String loggedInUserEmail = (principal == null) ? "" : principal.getName();		
			
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
			
			if(ad.getCurrentBuyer()!=null){
				Message message2;
				message2 = new Message();
				message2.setSubject("Auction " + ad.getTitle());
				message2.setSender(sender);
				message2.setRecipient(userService.findUserByUsername(ad.getCurrentBuyer()));
				message2.setState(MessageState.UNREAD);
				message2.setDateSent(calendar.getTime());
				message2.setDateShow(calendar.getTime());

				MailService mail = new MailService();
				User user = userDao.findByUsername(ad.getCurrentBuyer());
				if ((price >= ad.getRetailPrice()) && (ad.getRetailPrice() > 0)) {
					if(user.getPremium())
						mail.sendEmail(ad.getCurrentBuyer(),5,"http://localhost:8080/ad?id="+id);
					message2.setText("We are sorry to anounce, someone just bought out the Auction you were leading: <a href=\"http://localhost:8080/ad?id="+id + "\">"+ ad.getTitle() + ".</a> ");
				}
				else{
					if(user.getPremium())
						mail.sendEmail(ad.getCurrentBuyer(),2,"http://localhost:8080/ad?id="+id);
					message2.setText("Someone just replaced you as current highest Bidder on this Auction: <a href=\"http://localhost:8080/ad?id="+id + "\">"+ ad.getTitle() + ".</a> ");
				}
				if(!(ad.getCurrentBuyer().equals(loggedInUserEmail))){
				messageDao.save(message2);
				}			
			}
			
			ad.setCurrentBuyer(loggedInUserEmail);
			MailService mail = new MailService();
			if ((price >= ad.getRetailPrice()) && (ad.getRetailPrice() > 0)) {
				message.setText("We are happy to anounce, someone just bought out your Auction, Congratulations: <a href=\"http://localhost:8080/ad?id="+id + "\">"+ ad.getTitle() + "!</a> ");
				
				Message message3;
				message3 = new Message();
				message3.setSubject("Auction " + ad.getTitle());
				message3.setSender(sender);
				message3.setRecipient(userService.findUserByUsername(loggedInUserEmail));
				message3.setState(MessageState.UNREAD);
				message3.setDateSent(calendar.getTime());
				message3.setDateShow(calendar.getTime());
				message3.setText("You bought out this Auction. Congratulations on your newes purchase! <a href=\"http://localhost:8080/ad?id="+id + "\">"+ ad.getTitle() + "!</a> ");
				messageDao.save(message3);
				mail.sendEmail(loggedInUserEmail,7,"http://localhost:8080/ad?id="+id);
				ad.setAuctionMessage();
				
				if(ad.getUser().getPremium())
					mail.sendEmail(ad.getUser().getEmail(),4,"http://localhost:8080/ad?id="+id);
				redirectAttributes.addFlashAttribute("confirmationMessage",
						"Congratulations, you just have bought out this auction!");				
			}
			else{
				message.setText("Someone has bidded on your auction. It has now risen to " + ad.getCurrentBidding() + ": <a href=\"http://localhost:8080/ad?id="+id + "\">"+ ad.getTitle() + "!</a> ");
				if(ad.getUser().getPremium())
					mail.sendEmail(ad.getUser().getEmail(),3,"http://localhost:8080/ad?id="+id);
				redirectAttributes.addFlashAttribute("confirmationMessage",
						"Your Bidding has been placed. You are now the current highest Bidder!");	
			}
			
			messageDao.save(message);			
			adService.saveAd(ad);			
		}		
		return "redirect:/ad?id="+id;
	}		
}
