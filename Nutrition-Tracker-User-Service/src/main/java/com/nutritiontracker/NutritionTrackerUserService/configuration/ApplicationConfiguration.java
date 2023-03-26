package com.nutritiontracker.NutritionTrackerUserService.configuration;

import com.nutritiontracker.NutritionTrackerUserService.repository.UserRepository;
import com.nutritiontracker.NutritionTrackerUserService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final UserRepository userRepository;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(){

        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                var user = userRepository.findUserByEmail(username);
                if(user == null){
                    LOGGER.error(UserService.NO_USER_FOUND_BY_EMAIL + username);
                    throw new UsernameNotFoundException(UserService.NO_USER_FOUND_BY_EMAIL + username);
                }else{

                    LOGGER.info("Returning found user by email: " + username);
                    return user;
                }
            }
        };
    }


}
