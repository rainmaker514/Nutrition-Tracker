package com.nutritiontracker.NutritionTrackerUserService.repo;

import com.nutritiontracker.NutritionTrackerUserService.model.Entry;
import com.nutritiontracker.NutritionTrackerUserService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    void deleteEntryById(Long id);
    User findEntryById(Long id);
}
