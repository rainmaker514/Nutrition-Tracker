package com.nutritiontracker.NutritionTrackerUserService.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000; //5 days in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String NT_TD = "Nutrition Tracker, Teriq Douglas";
    public static final String NT_TD_ADMINISTRATION = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page!";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTION_HTTP_METHOD = "OPTIONS";
    //public static final String[] PUBLIC_URLS = { "**" };

    public static final String[] PUBLIC_URLS = { "/users/login", "/users/signup", "/users/reset-password/**" };
}
