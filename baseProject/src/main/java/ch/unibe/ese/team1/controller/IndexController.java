package ch.unibe.ese.team1.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team1.controller.pojos.MailService;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.MessageDao;

/**
 * This controller handles request concerning the home page and several other
 * simple pages.
 */
@Controller
public class IndexController {

	@Autowired
	private AdService adService;
	
	@Autowired
	private AdDao adDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private UserService userService;
	
	private SearchForm searchForm;

	/** Displays the home page. */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView searchAd() {
		
		adService.endMessages();
		
		ModelAndView model = new ModelAndView("index");
		return model;
	}

	/** Displays the about us page. */
	@RequestMapping(value = "/about")
	public ModelAndView about() {
		return new ModelAndView("about");
	}

	/** Displays the disclaimer page. */
	@RequestMapping(value = "/disclaimer")
	public ModelAndView disclaimer() {
		return new ModelAndView("disclaimer");
	}
	
	@RequestMapping(value = "/getResultsFromMap", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resultsFromMap(@RequestParam("coordinates") double[] coordinates, 
			@RequestParam("radius") double radius) {
		ModelAndView model = new ModelAndView("results");
		model.addObject("results", adService.getAdsInRadius(coordinates, radius));
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






