package com.nutritiontracker.NutritionTrackerUserService.service;

import com.nutritiontracker.NutritionTrackerUserService.exception.domain.UserNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.model.Entry;

public interface EntryServiceInterface {
    Entry createEntry(Long userId, Entry newEntry) throws UserNotFoundException;
}
