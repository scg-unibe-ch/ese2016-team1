package ch.unibe.ese.team1.controller.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.UserDao;

/** Handles updating a user's profile. */
@Service
public class UserUpdateService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	/** Handles updating an existing user in the database. */
	@Transactional
	public void updateFrom(String username, EditProfileForm editProfileForm) {
		
		System.out.println(username);
		
		User currentUser = userService.findUserByUsername(username);
		
		currentUser.setEmail(editProfileForm.getUsername());
		currentUser.setFirstName(editProfileForm.getFirstName());
		currentUser.setLastName(editProfileForm.getLastName());
		currentUser.setPassword(editProfileForm.getPassword());
		currentUser.setAboutMe(editProfileForm.getAboutMe());
		
		currentUser.setUsername(editProfileForm.getUsername());

		userDao.save(currentUser);
	}

	
	
}
