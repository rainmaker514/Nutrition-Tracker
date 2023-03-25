package com.nutritiontracker.NutritionTrackerUserService.service;

import com.nutritiontracker.NutritionTrackerUserService.enumeration.Role;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailExistException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.UserNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.model.AuthenticationRequest;
import com.nutritiontracker.NutritionTrackerUserService.model.AuthenticationResponse;
import com.nutritiontracker.NutritionTrackerUserService.model.RegisterRequest;
import com.nutritiontracker.NutritionTrackerUserService.model.User;

import jakarta.mail.MessagingException;
import java.util.List;

public interface UserServiceInterface {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    //User register(String firstname, String lastname, String email, String password) throws UserNotFoundException, EmailExistException;
    List<User> getAllUsers();
    User findUserByEmail(String email);
    User findUserById(Long id);
    User addNewUser(String firstname, String lastname, String email, Role role) throws EmailExistException;
    User updateUser(String currentEmail, String newFirstname, String newLastname, String newEmail, String newHeight,
                    int newWeight, int newAge, String newActivityLevel, String newGoal, Role newRole)
            throws EmailNotFoundException;
    void deleteUser(String email) throws UserNotFoundException;
    void resetPassword(String email) throws EmailNotFoundException, MessagingException;
    void changePassword(String email, String newPassword) throws EmailNotFoundException;
    User changeStartingWeight(String email, int startingWeight) throws EmailNotFoundException;
    User changeCurrentWeight(String email, int currentWeight) throws EmailNotFoundException;
}