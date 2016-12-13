package ch.unibe.ese.team1.controller;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team1.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team1.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.SignupService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.UserUpdateService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserPicture;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.UserDao;

/**
 * Handles all requests concerning user accounts and profiles.
 */
@Controller
public class ProfileController {

	@Autowired
	private SignupService signupService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserUpdateService userUpdateService;

	@Autowired
	private VisitService visitService;

	@Autowired
	private AdService adService;
	
	@Autowired
	private UserDao userDao;
	
	private SearchForm searchForm;
	
	private final static String CLIENT = "644276133344-lihdlm9a03d6qssgdm24qmlqv1saj2d8.apps.googleusercontent.com";
	private final static String DEFAULT_ROLE = "user";

	
	@Autowired
	@Qualifier("org.springframework.security.authenticationManager")
	private AuthenticationManager authenticationManager;

	/** Returns the login page. */
	@RequestMapping(value = "/login")
	public ModelAndView loginPage() {
		ModelAndView model = new ModelAndView("login");
		return model;
	}

	/** Returns the signup page. */
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView signupPage() {

		adService.endMessages();
		
		ModelAndView model = new ModelAndView("signup");
		model.addObject("signupForm", new SignupForm());
		return model;
	}

	/** Validates the signup form and on success persists the new user. */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView signupResultPage(@Valid SignupForm signupForm,
			BindingResult bindingResult) {
		ModelAndView model;
		if (!bindingResult.hasErrors()) {
			signupService.saveFrom(signupForm);
			model = new ModelAndView("login");
			model.addObject("confirmationMessage", "Signup complete!");
		} else {
			model = new ModelAndView("signup");
			model.addObject("signupForm", signupForm);
		}
		return model;
	}
	
	/** Make new google auth account or if exists already, login **/
    @RequestMapping(value = "/signInWithGoogle", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView googleLogin(@RequestParam("token") String token) throws GeneralSecurityException, IOException {
        
        JacksonFactory jacksonFactory = new JacksonFactory();
        NetHttpTransport transport = new NetHttpTransport();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
            .setAudience(Arrays.asList(CLIENT))
            .setIssuer("accounts.google.com")
            .build();

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String lastName = (String) payload.get("family_name");
            String firstName = (String) payload.get("given_name");
            String email = payload.getEmail();
            String imageUrl = (String) payload.get("picture");

            User user = userService.findUserByEmail(email);
    		if (user == null) {
    			signupService.saveFromGoogle(lastName, firstName, imageUrl, email);
    			user = userService.findUserByEmail(email);
    		}
    		Authentication request = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
    		Authentication result = authenticationManager.authenticate(request);
    		SecurityContextHolder.getContext().setAuthentication(result);
    		ModelAndView model = new ModelAndView("index");
    		return model;
	    }
        return new ModelAndView("index");
    }
	

	/** Checks and returns whether a user with the given email already exists. */
	@RequestMapping(value = "/signup/doesEmailExist", method = RequestMethod.POST)
	public @ResponseBody boolean doesEmailExist(@RequestParam String email) {
		return signupService.doesUserWithUsernameExist(email);
	}

	/** Shows the edit profile page. */
	@RequestMapping(value = "/profile/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfilePage(Principal principal) {

		adService.endMessages();
		
		ModelAndView model = new ModelAndView("editProfile");
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		model.addObject("editProfileForm", new EditProfileForm());
		model.addObject("currentUser", user);
		return model;
	}

	/** Handles the request for editing the user profile. */
	@RequestMapping(value = "/profile/editProfile", method = RequestMethod.POST)
	public ModelAndView editProfileResultPage(
			@Valid EditProfileForm editProfileForm,
			BindingResult bindingResult, Principal principal) {
		ModelAndView model;
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (!bindingResult.hasErrors() && editProfileForm.getSubmitType().equals("update")) {
			userUpdateService.updateFrom(username, editProfileForm);
			model = new ModelAndView("updatedProfile");
			
			if(!(editProfileForm.getUsername().equals(username))){
				model = new ModelAndView("redirect:/logout");
				model.addObject("message", "Your Profile has been updated! Log in with your new name.");				
				return model;
			}
			
			model.addObject("message", "Your Profile has been updated!" );
			model.addObject("currentUser", null);editProfileForm.getUsername();
			return model;
		} else if (!bindingResult.hasErrors() && editProfileForm.getSubmitType().equals("upgrade")) {
			userUpdateService.upgradeFrom(username, editProfileForm);
			model = new ModelAndView("updatedProfile");
			model.addObject("message", "Your Profile has been upgraded!");
			model.addObject("currentUser", null);editProfileForm.getUsername();
			return model;
		} else {
			model = new ModelAndView("updatedProfile");
			
			model.addObject("message",
					"Something went wrong, please contact the WebAdmin if the problem persists!");
			return model;
		}
	}

	/** Displays the public profile of the user with the given id. */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView user(@RequestParam("id") long id, Principal principal) {

		adService.endMessages();
		
		ModelAndView model = new ModelAndView("user");
		User user = userService.findUserById(id);
		if (principal != null) {
			String username = principal.getName();
			User user2 = userService.findUserByUsername(username);
			long principalID = user2.getId();
			model.addObject("principalID", principalID);
		}
		model.addObject("user", user);
		model.addObject("messageForm", new MessageForm());
		return model;
	}

	/** Displays the schedule page of the currently logged in user. */
	@RequestMapping(value = "/profile/schedule", method = RequestMethod.GET)
	public ModelAndView schedule(Principal principal) {

		adService.endMessages();
		
		ModelAndView model = new ModelAndView("schedule");
		User user = userService.findUserByUsername(principal.getName());

		// visits, i.e. when the user sees someone else's property
		Iterable<Visit> visits = visitService.getVisitsForUser(user);
		model.addObject("visits", visits);

		// presentations, i.e. when the user presents a property
		Iterable<Ad> usersAds = adService.getAdsByUser(user);
		ArrayList<Visit> usersPresentations = new ArrayList<Visit>();

		for (Ad ad : usersAds) {
			try {
				usersPresentations.addAll((ArrayList<Visit>) visitService
						.getVisitsByAd(ad));
			} catch (Exception e) {
			}
		}

		model.addObject("presentations", usersPresentations);
		return model;
	}

	/** Returns the visitors page for the visit with the given id. */
	@RequestMapping(value = "/profile/visitors", method = RequestMethod.GET)
	public ModelAndView visitors(@RequestParam("visit") long id) {

		adService.endMessages();
		
		ModelAndView model = new ModelAndView("visitors");
		Visit visit = visitService.getVisitById(id);
		Iterable<User> visitors = visit.getSearchers();

		model.addObject("visitors", visitors);

		Ad ad = visit.getAd();
		model.addObject("ad", ad);
		return model;
	}
	
	@ModelAttribute
	public SearchForm getSearchForm() {
		if (searchForm == null) {
			searchForm = new SearchForm();
		}
		return searchForm;
	}
}
