package ch.unibe.ese.team1.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team1.controller.pojos.MailService;
import ch.unibe.ese.team1.controller.pojos.PictureUploader;
import ch.unibe.ese.team1.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.BookmarkService;
import ch.unibe.ese.team1.controller.service.EditAdService;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdDao;

/**
 * This controller handles all requests concerning displaying ads and
 * bookmarking them.
 */
@Controller
public class AdController {

	@Autowired
	private AdService adService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private VisitService visitService;
	
	
	@Autowired
	private EditAdService editAdService;

	private final static String IMAGE_DIRECTORY = PlaceAdController.IMAGE_DIRECTORY;

	@Autowired
	private ServletContext servletContext;
	
	private PictureUploader pictureUploader;
	
	@Autowired
	private AdDao adDao;


	
	

	/** Gets the ad description page for the ad with the given id. */
	@RequestMapping(value = "/ad", method = RequestMethod.GET)
	public ModelAndView ad(@RequestParam("id") long id, Principal principal) {
		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);

		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());
		

		PlaceAdForm form = editAdService.fillForm(ad);
		model.addObject("placeAdForm", form);
		
		//String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
		//if (pictureUploader == null) {
		//	pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
		//}
		//List<String> fileNames = pictureUploader.getFileNames();
		
		//adDao.save(ad);
		
		

		String loggedInUserEmail = (principal == null) ? "" : principal
				.getName();
		model.addObject("loggedInUserEmail", loggedInUserEmail);

		model.addObject("visits", visitService.getVisitsByAd(ad));

		return model;
	}
	
	
	/**
	 * Gets the ad description page for the ad with the given id and also
	 * validates and persists the message passed as post data.
	 */
	@RequestMapping(value = "/ad", method = RequestMethod.POST)
	public ModelAndView messageSent(@RequestParam("id") long id,
			@Valid MessageForm messageForm, BindingResult bindingResult) {

		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);
		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());

		if (!bindingResult.hasErrors()) {
			messageService.saveFrom(messageForm);
		}
		return model;
	}


	
	@RequestMapping(value = "/ad3", method = RequestMethod.POST)
	public ModelAndView makeAuction( PlaceAdForm placeAdForm, BindingResult result
			, Principal principal, RedirectAttributes redirectAttributes, @RequestParam long adId
			, @RequestParam int oldBidding, @RequestParam int newBidding){//@RequestParam("newBidding") int newBidding, Principal principal, @RequestParam("oldBidding") int oldBidding, @RequestParam("adId") long adId){
		
		Ad ad = adService.getAdById(adId);
		
		ad.setAuctionPossible(false);
		ad.setCurrentBidding(newBidding);
		
		
		System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
		MailService mail1 = new MailService();
		mail1.sendEmail(ad.getUser().getEmail(),4);		
		if(ad.getCurrentBuyer()!=null){		
			System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
			MailService mail = new MailService();
			mail.sendEmail(ad.getCurrentBuyer(),5);
		}
		
		
		adDao.save(ad);
		
		ModelAndView model = new ModelAndView("redirect:/");
		redirectAttributes.addFlashAttribute("confirmationMessage",
				"Congratulations, you just bought out this auction!");
		return model;
	}
	
	@RequestMapping(value = "/ad2", method = RequestMethod.POST)
	public ModelAndView makeAuction2( PlaceAdForm placeAdForm2, BindingResult result
			, Principal principal, RedirectAttributes redirectAttributes, @RequestParam long adId
			, @RequestParam int oldBidding, @RequestParam int newBidding){//@RequestParam("newBidding") int newBidding, Principal principal, @RequestParam("oldBidding") int oldBidding, @RequestParam("adId") long adId){
		
		Ad ad = adService.getAdById(adId);
		
		int old = (int) (oldBidding * 1.05);
		if(placeAdForm2.getCurrentBidding() > old){
			if(placeAdForm2.getCurrentBidding() >= newBidding){
				ad.setAuctionPossible(false);
			}
			ad.setCurrentBidding(placeAdForm2.getCurrentBidding());
	
			System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
			MailService mail1 = new MailService();
			mail1.sendEmail(ad.getUser().getEmail(),3);		
			if(ad.getCurrentBuyer()!=null){		
				System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
				MailService mail = new MailService();
				mail.sendEmail(ad.getCurrentBuyer(),2);
			}
					
	String loggedInUserEmail = (principal == null) ? "" : principal
					.getName();		
	ad.setCurrentBuyer(loggedInUserEmail);
		
	
			
			adDao.save(ad);
		
			ModelAndView model = new ModelAndView("redirect:/");
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"You have successfully placed your Bidding!");
			return model;
		}
		else{
			ModelAndView model = new ModelAndView("redirect:/ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"Your Bidding was not accepted. Is it higher than 105% of the current one?");
			return model;
		}
	}
	
	
	

	/**
	 * Checks if the adID passed as post parameter is already inside user's
	 * List bookmarkedAds. In case it is present, true is returned changing
	 * the "Bookmark Ad" button to "Bookmarked". If it is not present it is
	 * added to the List bookmarkedAds.
	 * 
	 * @return 0 and 1 for errors; 3 to update the button to bookmarked 3 and 2
	 *         for bookmarking or undo bookmarking respectively 4 for removing
	 *         button completly (because its the users ad)
	 */
	@RequestMapping(value = "/bookmark", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public int isBookmarked(@RequestParam("id") long id,
			@RequestParam("screening") boolean screening,
			@RequestParam("bookmarked") boolean bookmarked, Principal principal) {
		// should never happen since no bookmark button when not logged in
		if (principal == null) {
			return 0;
		}
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (user == null) {
			// that should not happen...
			return 1;
		}
		List<Ad> bookmarkedAdsIterable = user.getBookmarkedAds();
		if (screening) {
			for (Ad ownAdIterable : adService.getAdsByUser(user)) {
				if (ownAdIterable.getId() == id) {
					return 4;
				}
			}
			for (Ad adIterable : bookmarkedAdsIterable) {
				if (adIterable.getId() == id) {
					return 3;
				}
			}
			return 2;
		}

		Ad ad = adService.getAdById(id);

		return bookmarkService.getBookmarkStatus(ad, bookmarked, user);
	}

	/**
	 * Fetches information about bookmarked rooms and own ads and attaches this
	 * information to the myRooms page in order to be displayed.
	 */
	@RequestMapping(value = "/profile/myRooms", method = RequestMethod.GET)
	public ModelAndView myRooms(Principal principal) {
		ModelAndView model;
		User user;
		if (principal != null) {
			model = new ModelAndView("myRooms");
			String username = principal.getName();
			user = userService.findUserByUsername(username);

			Iterable<Ad> ownAds = adService.getAdsByUser(user);

			model.addObject("bookmarkedAdvertisements", user.getBookmarkedAds());
			model.addObject("ownAdvertisements", ownAds);
			return model;
		} else {
			model = new ModelAndView("home");
		}

		return model;
	}

}