package com.nutritiontracker.NutritionTrackerUserService.controller;

import com.nutritiontracker.NutritionTrackerUserService.exception.ExceptionHandling;
import com.nutritiontracker.NutritionTrackerUserService.exception.domain.UserNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.model.Entry;
import com.nutritiontracker.NutritionTrackerUserService.service.EntryServiceInterface;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entries")
@CrossOrigin(origins = "http://localhost:4200")
public class EntryController extends ExceptionHandling {
    private EntryServiceInterface entryServiceInterface;

    @PostMapping("/{userId}/add")
    public ResponseEntity<Entry> createEntry(@PathVariable(value = "userId") Long userId, @RequestBody Entry entry)
            throws UserNotFoundException {

        entryServiceInterface.createEntry(userId, entry);

        return new ResponseEntity<>(entry, HttpStatus.CREATED);
    }
}

