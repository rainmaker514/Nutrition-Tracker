package com.nutritiontracker.NutritionTrackerUserService.exception.domain;

public class EmailExistException extends Exception{
    public EmailExistException(String message) {
        super(message);
    }
}