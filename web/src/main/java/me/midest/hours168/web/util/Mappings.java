package me.midest.hours168.web.util;

public final class Mappings {

    private static final String REDIRECT = "redirect:";

    public static final String HOME = "/";
    public static final String NEW_DAY = "/days/new";
    public static final String SHOW_DATES = "/days/show";
    public static final String DELETE_DAY = "/days/delete";
    public static final String UPDATE_TIMESEGMENT = "/segments/update";
    public static final String TIMETABLE = "/timetable";
    public static final String NEW_CATEGORY = "/categories/new";
    public static final String DELETE_CATEGORY = "/categories/delete";
    public static final String CATEGORIES = "/categories";
    public static final String ABOUT = "/about";

    public static final String REDIRECT_TIMETABLE = REDIRECT + TIMETABLE;
    public static final String REDIRECT_CATEGORIES = REDIRECT + CATEGORIES;

    private Mappings(){}

}
