package com.nutritiontracker.NutritionTrackerUserService.service;

import com.nutritiontracker.NutritionTrackerUserService.exception.domain.UserNotFoundException;
import com.nutritiontracker.NutritionTrackerUserService.model.Entry;
import com.nutritiontracker.NutritionTrackerUserService.model.User;
import com.nutritiontracker.NutritionTrackerUserService.repo.EntryRepository;
import com.nutritiontracker.NutritionTrackerUserService.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class EntryService implements EntryServiceInterface{

    private final UserRepository userRepository;
    private final EntryRepository entryRepository;

    @Autowired
    public EntryService(UserRepository userRepository, EntryRepository entryRepository) {
        this.userRepository = userRepository;
        this.entryRepository = entryRepository;
    }

    @Override
    public Entry createEntry(Long userId, Entry entry) throws UserNotFoundException {
        Entry newEntry = userRepository.findById(userId).map(user -> {
            entry.setUsers(user);
            return entryRepository.save(entry);
        }).orElseThrow(() -> new UserNotFoundException("User ID not found."));

        /*List<Entry> entries = new ArrayList<>();
        User user = new User();
        Entry entry = new Entry(date, weight);

        entry.setDate(date);
        entry.setWeight(weight);
        entries.add(entry);

        entryRepository.saveAll(entries);
        LOGGER.info(entry+"");*/

        return entry;
    }
}
