package com.nutritiontracker.NutritionTrackerUserService.service;

import com.nutritiontracker.NutritionTrackerUserService.enumeration.Role;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailExistException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.UserNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.model.User;
import com.nutritiontracker.NutritionTrackerUserService.model.UserPrincipal;
import com.nutritiontracker.NutritionTrackerUserService.repo.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import java.util.List;

import static com.nutritiontracker.NutritionTrackerUserService.enumeration.Role.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@Component
@Transactional
@Qualifier("userDetailsService")
public class UserService implements UserServiceInterface, UserDetailsService {
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists.";
    public static final String NO_USER_FOUND_BY_EMAIL = "No user found by email: ";
    private final UserRepository userRepository;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private BCryptPasswordEncoder passwordEncoder;
    //private EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder/*, EmailService emailService*/) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        //this.emailService = emailService;
    }

    @Override
    public void deleteUser(String email){
        User user = userRepository.findUserByEmail(email);
        userRepository.deleteUserById(user.getId());
    }

    @Override
    public void resetPassword(String email) throws EmailNotFoundException, MessagingException {
        User user = userRepository.findUserByEmail(email);
        if(user == null){
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        LOGGER.info("New password: " + password);
        //emailService.sendNewPasswordEmail(user.getFirstname(), password, user.getEmail());
    }

    @Override
    public void changePassword(String email, String newPassword) throws EmailNotFoundException{


        User user = userRepository.findUserByEmail(email);

        if(user == null){
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }

        user.setPassword(encodePassword(newPassword));
        userRepository.save(user);
        LOGGER.info("New password: " + newPassword);
    }

    @Override
    public User changeStartingWeight(String email, int startingWeight) throws EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        user.setStartingWeight(startingWeight);
        userRepository.save(user);

        return user;
    }

    @Override
    public User changeCurrentWeight(String email, int currentWeight) throws EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        user.setWeight(currentWeight);
        userRepository.save(user);

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User addNewUser(String firstname, String lastname, String email, String role) throws UserNotFoundException, EmailExistException,
            MessagingException{
        validateNewEmail(EMPTY, email);
        User user = new User();
        String password = generatePassword();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(user);
        //emailService.sendNewPasswordEmail(firstname, password, email);
        LOGGER.info("Password is: " + password);

        return user;
    }

    @Override
    public User updateUser(String currentEmail, String newFirstname, String newLastname, String newEmail, String newHeight,
                           int newWeight, int newAge, String newActivityLevel, String newGoal, String role) throws
            UserNotFoundException, EmailExistException {
        User currentUser = validateNewEmail(currentEmail, newEmail);
        currentUser.setFirstname(newFirstname);
        currentUser.setLastname(newLastname);
        currentUser.setEmail(newEmail);
        currentUser.setHeight(newHeight);
        currentUser.setWeight(newWeight);
        currentUser.setAge(newAge);
        currentUser.setActivityLevel(newActivityLevel);
        currentUser.setGoal(newGoal);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);

        return currentUser;
    }

    @Override
    public User findUserById(Long id){
        return userRepository.findUserById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if(user == null){
            LOGGER.error(NO_USER_FOUND_BY_EMAIL + email);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }else{
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info("Returning found user by email: " + email);
            return userPrincipal;
        }
    }

    public User register(String firstname, String lastname, String email, String password) throws UserNotFoundException, EmailExistException {

        validateNewEmail(EMPTY, email);
        User user = new User();
        String encodedPassword = encodePassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        userRepository.save(user);

        return user;
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private User validateNewEmail(String currentUserEmail, String newEmail) throws EmailExistException, UserNotFoundException {
        User userByNewEmail = findUserByEmail(newEmail);

        if(StringUtils.isNotBlank(currentUserEmail)){
            User currentUser = findUserByEmail(currentUserEmail);

            if(currentUser == null){
                throw new UserNotFoundException(NO_USER_FOUND_BY_EMAIL + currentUserEmail);
            }

            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())){
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }

            return currentUser;
        }else{
            if(userByNewEmail != null){
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);

            }
            return null;
        }
    }
}