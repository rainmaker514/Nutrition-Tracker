package com.nutritiontracker.NutritionTrackerUserService.exception.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailExistException extends Exception{
    public EmailExistException(String message) {
        super(message);
    }
}