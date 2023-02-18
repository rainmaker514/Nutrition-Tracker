package com.nutritiontracker.NutritionTrackerUserService.service;

import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailExistException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.EmailNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.UserNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.model.User;

import javax.mail.MessagingException;
import java.util.List;

public interface UserServiceInterface {
    User register(String firstname, String lastname, String email, String password) throws UserNotFoundException, EmailExistException;

    List<User> getAllUsers();

    User findUserByEmail(String email);

    User findUserById(Long id);

    User addNewUser(String firstname, String lastname, String email, String role) throws UserNotFoundException, EmailExistException,
            MessagingException;

    User updateUser(String currentEmail, String newFirstname, String newLastname, String newEmail, String newHeight,
                    int newWeight, int newAge, String newActivityLevel, String newGoal, String role)
            throws UserNotFoundException, EmailExistException;

    void deleteUser(String email);

    void resetPassword(String email) throws EmailNotFoundException, MessagingException;

    void changePassword(String email, String newPassword) throws EmailNotFoundException;

    User changeStartingWeight(String email, int startingWeight) throws EmailNotFoundException;

    User changeCurrentWeight(String email, int currentWeight) throws EmailNotFoundException;
}
