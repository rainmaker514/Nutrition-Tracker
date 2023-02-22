package com.nutritiontracker.NutritionTrackerUserService.repo;

import com.nutritiontracker.NutritionTrackerUserService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    void deleteUserById(Long id);

    User findUserById(Long id);

    User findUserByEmail(String email);
}