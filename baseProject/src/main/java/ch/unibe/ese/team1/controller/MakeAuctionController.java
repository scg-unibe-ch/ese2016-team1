package ch.unibe.ese.team1.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team1.controller.pojos.MailService;
import ch.unibe.ese.team1.controller.pojos.PictureUploader;
import ch.unibe.ese.team1.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.AlertService;
import ch.unibe.ese.team1.controller.service.EditAdService;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.PictureMeta;
import ch.unibe.ese.team1.model.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.MessageDao;


/**
 * This controller handles all requests concerning editing ads.
 */
@Controller
public class MakeAuctionController {

	private final static String IMAGE_DIRECTORY = PlaceAdController.IMAGE_DIRECTORY;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private AdService adService;

	@Autowired
	private EditAdService editAdService;

	@Autowired
	private UserService userService;

	@Autowired
	private AlertService alertService;

	private PictureUploader pictureUploader;

	private ObjectMapper objectMapper;
	
	@Autowired
	private MessageDao messageDao;

	/**
	 * Serves the page that allows the user to edit the ad with the given id.
	 */
	@RequestMapping(value = "/makeAuction", method = RequestMethod.GET)
	public ModelAndView editAdPage(@RequestParam long id, Principal principal) {
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
	 * Processes the edit ad form and displays the result page to the user.
	 * 
	 * Principal principal
	 */
//	@RequestMapping(value = "/makeAuction", method = RequestMethod.POST)
//	public ModelAndView editAdPageWithForm(@Valid PlaceAdForm placeAdForm,
//			BindingResult result, Principal principal,
//			RedirectAttributes redirectAttributes, @RequestParam long adId) {
//		ModelAndView model = new ModelAndView("placeAd");
//		if (!result.hasErrors()) {
//			//String username = principal.getName();
//			//User user = userService.findUserByUsername(username);
//
//			String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
//			if (pictureUploader == null) {
//				pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
//			}
//			List<String> fileNames = pictureUploader.getFileNames();
//			Ad ad = editAdService.saveFrom(placeAdForm, fileNames, null, adId);
//
//			// triggers all alerts that match the placed ad
//			alertService.triggerAlerts(ad);
//
//			// reset the picture uploader
//			this.pictureUploader = null;
//
//			model = new ModelAndView("redirect:/");
//			redirectAttributes.addFlashAttribute("confirmationMessage",
//					"You have placed your Auction sucessfully.");
//		}
//
//		return model;
//	}
	
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
			System.out.println("QQQQQQ");
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"There was an error! Pleas make sure you place a valid bid!");
			return "redirect:/ad?id="+id;
		}
		
		//int price = Integer.parseInt(strprice);
		Ad ad = adService.getAdById(id);
		ad.addFifteenMinutesToAuctionEndedIfNecessary();
		
		if (price >= ad.getNextPossibleBid()) {
			ad.setCurrentBidding(price);
			
			String loggedInUserEmail = (principal == null) ? "" : principal.getName();		
			
			Message message;
			message = new Message();
			message.setSubject("Auction: " + ad.getTitle());
			message.setSender(ad.getUser());
			message.setRecipient(ad.getUser());
			message.setState(MessageState.UNREAD);
			
			Calendar calendar = Calendar.getInstance();
			// java.util.Calendar uses a month range of 0-11 instead of the
			// XMLGregorianCalendar which uses 1-12
			calendar.setTimeInMillis(System.currentTimeMillis());
			message.setDateSent(calendar.getTime());
			
			
			if(ad.getCurrentBuyer()!=null){
				Message message2;
				message2 = new Message();
				message2.setSubject("Auction " + ad.getTitle());
				message2.setSender(userService.findUserByUsername(ad.getCurrentBuyer()));
				message2.setRecipient(userService.findUserByUsername(ad.getCurrentBuyer()));
				message2.setState(MessageState.UNREAD);
				message2.setDateSent(calendar.getTime());

				

				
				System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
				MailService mail = new MailService();
				if (price >= ad.getRetailPrice()) {
					mail.sendEmail(ad.getCurrentBuyer(),5);
					message2.setText("We are sorry to anounce, someone just bought out the Auction you were leading.");

				}
				else{
					mail.sendEmail(ad.getCurrentBuyer(),2);
					message2.setText("Someone just replaced you as current highest Bidder on this Auction." );

				}

				messageDao.save(message2);
				
			}
			
			ad.setCurrentBuyer(loggedInUserEmail);
			
			System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
			MailService mail = new MailService();
			if (price >= ad.getRetailPrice()) {
				message.setText("We are happy to anounce, someone just bought out your Auction, Congratulations!");
				mail.sendEmail(ad.getUser().getEmail(),4);
				redirectAttributes.addFlashAttribute("confirmationMessage",
						"Congratulations, you just have bought out this auction!");
				
			}
			else{
				message.setText("Someone has bidded on your auction. It has now risen to " + ad.getCurrentBidding() + ".");
				mail.sendEmail(ad.getUser().getEmail(),3);
				redirectAttributes.addFlashAttribute("confirmationMessage",
						"Your Bidding has been placed. You are now the current highest Bidder!");
				
			}
			
			messageDao.save(message);
			
			adService.saveAd(ad);
			
		}
		
		return "redirect:/ad?id="+id;
	}	

	
}
