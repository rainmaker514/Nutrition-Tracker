package com.nutritiontracker.NutritionTrackerUserService.service;

import com.nutritiontracker.NutritionTrackerUserService.authentication.JWTService;
import com.nutritiontracker.NutritionTrackerUserService.enumeration.Role;
import com.nutritiontracker.NutritionTrackerUserService.exception.CustomExceptionHandler;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailExistException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.UserNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.model.AuthenticationRequest;
import com.nutritiontracker.NutritionTrackerUserService.model.AuthenticationResponse;
import com.nutritiontracker.NutritionTrackerUserService.model.RegisterRequest;
import com.nutritiontracker.NutritionTrackerUserService.model.User;
import com.nutritiontracker.NutritionTrackerUserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

import static com.nutritiontracker.NutritionTrackerUserService.exception.CustomExceptionHandler.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@RequiredArgsConstructor
@Service
@Component
@Transactional
@Qualifier("userDetailsService")
public class UserService implements UserServiceInterface {
    public static final String NO_USER_FOUND_BY_EMAIL = "No user found by email: ";
    public static final String USER_NOT_FOUND = "User not found.";
    private final UserRepository userRepository;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    //private EmailService emailService;

    @Override
    public AuthenticationResponse register(RegisterRequest request) throws EmailExistException {
        User user = findUserByEmail(request.getEmail());

        if(user != null) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
        user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword()));

        var user = userRepository.findUserByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public User addNewUser(String firstname, String lastname, String email, Role role) throws EmailExistException{

        User user = findUserByEmail(email);

        if(user != null) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }

        String password = generatePassword();
        User newUser = User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();
        userRepository.save(newUser);
        user = newUser;

        /*User user = new User();
        String password = generatePassword();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(user);
        //emailService.sendNewPasswordEmail(firstname, password, email);
        LOGGER.info("Password is: " + password);*/


        return user;
    }

    @Override
    public User updateUser(String currentEmail, String newFirstname, String newLastname, String newEmail, String newHeight,
                           int newWeight, int newAge, String newActivityLevel, String newGoal, Role newRole) throws
            EmailNotFoundException, UserNotFoundException, EmailExistException {
        User currentUser = findUserByEmail(currentEmail);
        if(currentUser == null){
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + currentEmail);
        }

        currentUser = validateNewEmail(currentEmail, newEmail);
        currentUser.setFirstname(newFirstname);
        currentUser.setLastname(newLastname);
        currentUser.setEmail(newEmail);
        currentUser.setHeight(newHeight);
        currentUser.setWeight(newWeight);
        currentUser.setAge(newAge);
        currentUser.setActivityLevel(newActivityLevel);
        currentUser.setGoal(newGoal);
        currentUser.setRole(newRole);
        //currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);

        String jwtToken = jwtService.generateToken(currentUser);
        AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

        LOGGER.info("Token: " + jwtToken);

        return currentUser;
    }

    public void deleteUser(String email) throws UserNotFoundException {
        User user = findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        userRepository.deleteUserById(user.getId());
    }

    @Override
    public void resetPassword(String email) throws EmailNotFoundException {
        User user = findUserByEmail(email);
        if(user == null){
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        LOGGER.info("New password: " + password);
        //emailService.sendNewPasswordEmail(user.getFirstname(), password, user.getEmail());
    }

    @Override
    public void changePassword(String email, String newPassword) throws EmailNotFoundException{
        User user = findUserByEmail(email);

        if(user == null){
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
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
    public User findUserById(Long id){
        return userRepository.findUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    /*@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findUserByEmail(email);
        if(user == null){
            LOGGER.error(NO_USER_FOUND_BY_EMAIL + email);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }else{

            LOGGER.info("Returning found user by email: " + email);
            return user;
        }
    }*/

    /*private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }*/

    /*private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }*/

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