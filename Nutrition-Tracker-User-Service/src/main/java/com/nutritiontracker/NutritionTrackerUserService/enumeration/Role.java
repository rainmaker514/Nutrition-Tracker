package com.nutritiontracker.NutritionTrackerUserService.enumeration;

import static com.nutritiontracker.NutritionTrackerUserService.constant.Authorities.*;

public enum Role {

    /*USER,
    ADMIN*/

    USER(USER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}