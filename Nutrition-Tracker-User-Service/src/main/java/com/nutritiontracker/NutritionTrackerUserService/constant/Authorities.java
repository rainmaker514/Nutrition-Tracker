package com.nutritiontracker.NutritionTrackerUserService.constant;

public class Authorities {
    public static final String[] USER_AUTHORITIES = { "user:read" };
    public static final String[] ADMIN_AUTHORITIES = { "user:read", "user:create", "user:update", "user:delete" };
}