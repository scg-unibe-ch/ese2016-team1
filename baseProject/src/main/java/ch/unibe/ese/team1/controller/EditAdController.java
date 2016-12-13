package ch.unibe.ese.team1.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team1.controller.pojos.PictureUploader;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.AlertService;
import ch.unibe.ese.team1.controller.service.EditAdService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.PictureMeta;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdPictureDao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller handles all requests concerning editing ads.
 */
@Controller
public class EditAdController {

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
	private AdPictureDao adPictureDao;
	
	@Autowired
	private AlertService alertService;

	private PictureUploader pictureUploader;

	private ObjectMapper objectMapper;
	
	private List<User> roomies = new LinkedList<User>();
	
	private List<AdPicture> pics = new LinkedList<AdPicture>();
	
	private List<String> visis = new LinkedList<String>();

	/**
	 * Serves the page that allows the user to edit the ad with the given id.
	 */
	@RequestMapping(value = "/profile/editAd", method = RequestMethod.GET)
	public ModelAndView editAdPage(@RequestParam long id, Principal principal) {
		ModelAndView model = new ModelAndView("editAd");
		Ad ad = adService.getAdById(id);
		
		// user can only modify an ad if he is the creator of the ad. Otherwise, return permissionDenied page
		if (ad.getUser().getUsername().equals(principal.getName())) {

			ad.setAltId(1);

			visis = new LinkedList<String>();
			roomies = new LinkedList<User>();
			pics = new LinkedList<AdPicture>();
			if (ad.getRegisteredRoommates() != null) {
				roomies = ad.getRegisteredRoommates();
			}
			if (ad.getPictures() != null) {
				pics = ad.getPictures();
			}
			if (ad.getVisits() != null) {
				// visis = ad.getVisitsAsStrings();
				for (String tempName : ad.getVisitsAsStrings()) {
					if (!(visis.contains(tempName))) {
						visis.add(tempName);
					}
				}
			}
			model.addObject("visis", visis);

			model.addObject("ad", ad);

			PlaceAdForm form = editAdService.fillForm(ad);

			model.addObject("placeAdForm", form);

			String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
			if (pictureUploader == null) {
				pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
			}

			return model;
		} else {
			return new ModelAndView("permissionDenied");
		}
	}

	
	/**
	 * Processes the edit ad form and displays the result page to the user.
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/profile/editAd", method = RequestMethod.POST)
	public ModelAndView editAdPageWithForm(@Valid PlaceAdForm placeAdForm,
			BindingResult result, Principal principal,
			RedirectAttributes redirectAttributes, @RequestParam long adId) throws ParseException {
		ModelAndView model = new ModelAndView("placeAd");
		//if (!result.hasErrors()) {
		
			//	try{
				String username = principal.getName();
				User user = userService.findUserByUsername(username);

				
				if ((!result.hasErrors())     ){// && (placeAdForm.getCity().length() > 7) && (placeAdForm.getMoveInDate().length() == 10) && ((placeAdForm.getEndOfAuction().length() == 16) || (placeAdForm.getEndOfAuction().length() == 0)) && ((placeAdForm.getMoveOutDate().length() == 10) || (placeAdForm.getMoveOutDate().length() == 0))) {
				try{
				
					String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
					if (pictureUploader == null) {
						pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
					}
					List<String> fileNames = pictureUploader.getFileNames();
						
					
					
					Ad ad = editAdService.saveFrom(placeAdForm, fileNames, user, adId, roomies, pics, visis);

	
	roomies = new LinkedList<User>();
	pics = new LinkedList<AdPicture>();
	visis = new LinkedList<String>();
					
					// triggers all alerts that match the placed ad
					alertService.triggerAlerts(ad);
				
					// reset the picture uploader
					this.pictureUploader = null;

					model = new ModelAndView("redirect:/ad?id=" + ad.getId());
					redirectAttributes.addFlashAttribute("confirmationMessage",
							"Ad edited successfully. You can take a look at it below.");
					}
					catch(Error e){
						model = new ModelAndView("redirect:/profile/editAd?id=" + adId);
						redirectAttributes.addFlashAttribute("errorMessage",
							"There was an error. Check if your inputs are correct and try again."  );
					
					}
				} 
				else {
					model=new ModelAndView("editAd");

					String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
					if (pictureUploader == null) {
						pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
					}
					
					
					
					List<String> fileNames = pictureUploader.getFileNames();
					//List<AdPicture> pictures = new ArrayList<>();
					for (String filePath : fileNames) {
						AdPicture picture = new AdPicture();
						picture.setFilePath(filePath);
						//pics.add(picture);
					}

					

					
					//roomies = null;
					
					if(placeAdForm.getRegisteredRoommateEmails() != null){
						for (String tempName : placeAdForm.getRegisteredRoommateEmails()) {
							//UserDao userDao = null;
							User tempUser = userService.findUserByUsername(tempName);
							if(tempUser!=null){
								roomies.add(tempUser);
							}
						}
					}
					
					if(placeAdForm.getVisits() != null){
						for (String tempName : placeAdForm.getVisits()) {

							if(!(visis.contains(tempName))){
								visis.add(tempName);
							}
														
						}
					}
					//placeAdForm.setStreet(visis.get(0) + visis.get(1));
					
					
					
					
					
					model = new ModelAndView("editAd");
					Ad ad = adService.getAdById(adId);
					
					ad.setAltId(0);
					
					model.addObject("ad", ad);

					model.addObject("placeAdForm", placeAdForm);
					
					
					
					model.addObject("visis", visis);
					model.addObject("pics", pics);
					model.addObject("roomies", roomies);
					
					

				}
			return model;
		}


	/**
	 * Deletes the ad picture with the given id from the list of pictures from
	 * the ad, but not from the server.
	 */
	@RequestMapping(value = "/profile/editAd/deletePictureFromAd", method = RequestMethod.POST)
	public @ResponseBody void deletePictureFromAd(@RequestParam long adId,
			@RequestParam long pictureId) {
		//editAdService.deletePictureFromAd(adId, pictureId);
		AdPicture pic = adPictureDao.findOne(pictureId);
		pics.remove(pic);
	}

	/**
	 * Gets the descriptions for the pictures that were uploaded with the
	 * current picture uploader.
	 * 
	 * @return a list of picture descriptions or null if no pictures were
	 *         uploaded
	 */
	@RequestMapping(value = "/profile/editAd/getUploadedPictures", method = RequestMethod.POST)
	public @ResponseBody List<PictureMeta> getUploadedPictures() {
		if (pictureUploader == null) {
			return null;
		}
		return pictureUploader.getUploadedPictureMetas();
	}

	/**
	 * Uploads the pictures that are attached as multipart files to the request.
	 * The JSON representation, that is returned, is generated manually because
	 * the jQuery Fileupload plugin requires this special format.
	 * 
	 * @return A JSON representation of the uploaded files
	 */
	@RequestMapping(value = "/profile/editAd/uploadPictures", method = RequestMethod.POST)
	public @ResponseBody String uploadPictures(
			MultipartHttpServletRequest request) {
		List<MultipartFile> pictures = new LinkedList<>();
		Iterator<String> iter = request.getFileNames();

		while (iter.hasNext()) {
			pictures.add(request.getFile(iter.next()));
		}

		List<PictureMeta> uploadedPicturesMeta = pictureUploader
				.upload(pictures);

		objectMapper = new ObjectMapper();
		String jsonResponse = "{\"files\": ";
		try {
			jsonResponse += objectMapper
					.writeValueAsString(uploadedPicturesMeta);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		jsonResponse += "}";
		return jsonResponse;
	}

	/**
	 * Deletes the uploaded picture at the given relative url (relative to the
	 * webapp folder).
	 */
	@RequestMapping(value = "/profile/editAd/deletePicture", method = RequestMethod.POST)
	public @ResponseBody void deleteUploadedPicture(@RequestParam String url) {
		if (pictureUploader != null) {
			String realPath = servletContext.getRealPath(url);
			pictureUploader.deletePicture(url, realPath);
		}
	}

	/**
	 * Deletes the roommate with the given id.
	 * 
	 * @param userId
	 *            the id of the user to delete
	 * @param adId
	 *            the id of the ad to delete the user from
	 */
	@RequestMapping(value = "/profile/editAd/deleteRoommate", method = RequestMethod.POST)
	public @ResponseBody void deleteRoommate(@RequestParam long userId,
			@RequestParam long adId) {
		//editAdService.deleteRoommate(userId, adId);
		User user = userService.findUserById(userId);
		roomies.remove(user);
	}
}
