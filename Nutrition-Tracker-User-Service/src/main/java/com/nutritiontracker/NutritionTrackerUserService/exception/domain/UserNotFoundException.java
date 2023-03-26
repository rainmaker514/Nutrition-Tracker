package com.nutritiontracker.NutritionTrackerUserService.exception.domain;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message) {
        super(message);
    }
}