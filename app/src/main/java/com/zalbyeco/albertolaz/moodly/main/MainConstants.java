package com.zalbyeco.albertolaz.moodly.main;

/**
 * Created by AlbertoLaz on 02/09/2016.
 */
public class MainConstants {

    public static final String INTENT_EXTRA_USERNAME_KEY = "moodle_username";
    public static final String INTENT_EXTRA_PASSWORD_KEY = "moodle_password";

    public static String getUsernameFromMailAddr(String mailAddr) {
        if (mailAddr == null || !mailAddr.contains("@")) {
            return null;
        } else {
            return mailAddr.substring(0, mailAddr.indexOf("@"));
        }
    }
}
