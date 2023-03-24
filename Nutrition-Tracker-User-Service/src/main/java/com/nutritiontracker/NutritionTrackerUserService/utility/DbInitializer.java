package com.nutritiontracker.NutritionTrackerUserService.utility;

import com.nutritiontracker.NutritionTrackerUserService.controller.UserController;
import com.nutritiontracker.NutritionTrackerUserService.enumeration.Role;
import com.nutritiontracker.NutritionTrackerUserService.model.User;
import com.nutritiontracker.NutritionTrackerUserService.repository.UserRepository;
import com.nutritiontracker.NutritionTrackerUserService.service.UserServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class DbInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DbInitializer.class);
    private AuthenticationManager authenticationManager;
    private UserServiceInterface userServiceInterface;

    public DbInitializer(UserServiceInterface userServiceInterface){
        this.userServiceInterface = userServiceInterface;
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception{
        /*logger.info("Initializing DB...");
        userRepository.deleteAll();
        userServiceInterface.addNewUser("admin", "admin", "admin", Role.ROLE_ADMIN);
        logger.info("DB initialized!");*/
    }
}