package com.nutritiontracker.NutritionTrackerUserService.controller;

import com.nutritiontracker.NutritionTrackerUserService.enumeration.Role;
import com.nutritiontracker.NutritionTrackerUserService.exception.CustomExceptionHandler;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailExistException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.UserNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.model.AuthenticationRequest;
import com.nutritiontracker.NutritionTrackerUserService.model.AuthenticationResponse;
import com.nutritiontracker.NutritionTrackerUserService.model.HttpResponse;
import com.nutritiontracker.NutritionTrackerUserService.model.RegisterRequest;
import com.nutritiontracker.NutritionTrackerUserService.model.User;
import com.nutritiontracker.NutritionTrackerUserService.service.UserServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import jakarta.persistence.NoResultException;
import java.util.List;

import static com.nutritiontracker.NutritionTrackerUserService.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController extends CustomExceptionHandler {

    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED = "User deleted!";
    public static final String PASSWORD_CHANGE = "Password changed.";
    public static final String GENERIC_SUCCESS = "Action successful.";
    private UserServiceInterface userServiceInterface;
    private AuthenticationManager authenticationManager;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserController(UserServiceInterface userServiceInterface, AuthenticationManager authenticationManager) {
        this.userServiceInterface = userServiceInterface;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws EmailExistException {
        return ResponseEntity.ok(userServiceInterface.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userServiceInterface.authenticate(request));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<User> addNewUser(@RequestParam("firstname") String firstname,
                                           @RequestParam("lastname") String lastname,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") Role role) throws EmailExistException {
        LOGGER.info("hey");
        return ResponseEntity.ok(userServiceInterface.addNewUser(firstname, lastname, email, role));
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestParam("currentEmail") String currentEmail,
                                           @RequestParam("firstname") String firstname,
                                           @RequestParam("lastname") String lastname,
                                           @RequestParam("email") String email,
                                           @RequestParam("height") String height,
                                           @RequestParam("weight") int weight,
                                           @RequestParam("age") int age,
                                           @RequestParam("activityLevel") String activityLevel,
                                           @RequestParam("goal") String goal,
                                           @RequestParam("role") Role role) throws EmailNotFoundException {

        User updatedUser = userServiceInterface.updateUser (currentEmail, firstname, lastname, email, height, weight, age,
                activityLevel, goal, role);

        return new ResponseEntity<>(updatedUser, OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<HttpResponse> changePassword(@RequestParam("email") String email, @RequestParam("newPassword")
            String newPassword) throws EmailNotFoundException {

        userServiceInterface.changePassword(email, newPassword);

        return response(OK, PASSWORD_CHANGE);
    }

    @PutMapping("/change-starting-weight")
    public ResponseEntity<User> changeStartingWeight(@RequestParam("email") String email, @RequestParam("startingWeight")
            int startingWeight) throws EmailNotFoundException {

        User user = userServiceInterface.changeStartingWeight(email, startingWeight);

        return new ResponseEntity<>(user, OK);
    }

    @PutMapping("/change-current-weight")
    public ResponseEntity<User> changeCurrentWeight(@RequestParam("email") String email, @RequestParam("currentWeight")
            int currentWeight) throws EmailNotFoundException {

        User user = userServiceInterface.changeCurrentWeight(email, currentWeight);

        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/find/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) throws EmailNotFoundException {
        User user = userServiceInterface.findUserByEmail(email);
        if(user == null){
            throw new EmailNotFoundException("No user found with the email: " + email);
        }
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userServiceInterface.getAllUsers();
        if(users == null){
            throw new NoResultException("No users found.");
        }
        return new ResponseEntity<>(users, OK);
    }

    @GetMapping("/reset-password/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws EmailNotFoundException, MessagingException {
        userServiceInterface.resetPassword(email);
        return response(OK, EMAIL_SENT + email);
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("email") String email) throws UserNotFoundException {
        userServiceInterface.deleteUser(email);
        return response(OK, USER_DELETED);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message.toUpperCase()), httpStatus);
    }
}