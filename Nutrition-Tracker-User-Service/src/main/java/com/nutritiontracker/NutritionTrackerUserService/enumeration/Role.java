package com.nutritiontracker.NutritionTrackerUserService.enumeration;

import static com.nutritiontracker.NutritionTrackerUserService.constant.Authorities.*;

public enum Role {

    USER,
    ADMIN

    /*ROLE_USER(USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }*/
}