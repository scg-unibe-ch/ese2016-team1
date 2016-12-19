package ch.unibe.ese.team1.controller.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.MailService;
import ch.unibe.ese.team1.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserPicture;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.UserDao;
//
import ch.unibe.ese.team1.model.dao.MessageDao;

/** Handles the persisting of new users */
@Service
public class SignupService {
	
	private static final String DEFAULT_ROLE = "ROLE_USER";
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private UserService userService;

	/** Handles persisting a new user to the database. */
	@Transactional
	public void saveFrom(SignupForm signupForm) {
		User user = new User();
		user.setUsername(signupForm.getEmail());
		user.setEmail(signupForm.getEmail());
		user.setFirstName(signupForm.getFirstName());
		user.setLastName(signupForm.getLastName());
		user.setPassword(signupForm.getPassword());
		user.setEnabled(true);
		user.setGender(signupForm.getGender());
		user.setPremium(false);
		
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole(DEFAULT_ROLE);
		role.setUser(user);
		userRoles.add(role);
		
		user.setUserRoles(userRoles);
		
		userDao.save(user);
		

		Message message;
		message = new Message();
		message.setSubject("Welcome " + signupForm.getFirstName() + " " + signupForm.getLastName());
		message.setText("Hello new User");
		message.setSender(user);
		message.setRecipient(user);
		message.setState(MessageState.UNREAD);
		Calendar calendar = Calendar.getInstance();
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12
		calendar.setTimeInMillis(System.currentTimeMillis());
		message.setDateSent(calendar.getTime());
		message.setDateShow(calendar.getTime());

		MailService mail = new MailService();
		mail.sendEmail(signupForm.getEmail(),0,"");
		
		messageDao.save(message);
		
	}
	
	// save user from google profile
	public void saveFromGoogle(String lastName, String firstName, String imageUrl, String email) {
		SignupForm signupForm = new SignupForm();
		signupForm.setEmail(email);
		signupForm.setFirstName(firstName);
		signupForm.setLastName(lastName);
		signupForm.setGender(Gender.UNKNOWN);
		SecureRandom random = new SecureRandom();		
		signupForm.setPassword(new BigInteger(130, random).toString(32));
		this.saveFrom(signupForm);
		User user = userService.findUserByEmail(email);
		// set picture for user
		UserPicture picture = new UserPicture();
		picture.setFilePath(imageUrl);
		picture.setUser(user);
		user.setPicture(picture);
		user.setGoogleUser(true);
		user.setPremium(false);
		userDao.save(user);	
	}
	
	/**
	 * Returns whether a user with the given username already exists.
	 * @param username the username to check for
	 * @return true if the user already exists, false otherwise
	 */
	@Transactional
	public boolean doesUserWithUsernameExist(String username){
		return userDao.findByUsername(username) != null;
	}
}
