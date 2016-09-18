package com.zalbyeco.albertolaz.moodly.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlbertoLaz on 02/09/2016.
 */
public class User implements Serializable {


    private String username;
    private String password;
    private MoodleCategory usrCategory;
    private List<String> features;


    private List<Contact> contactsList;

    /**
     *
     * Private constructor, you need to use static method "initializeStandardUser" instead of the constructor
     *
     * @param username
     * @param password
     * @param userCategory
     * @param features
     * @param contactsList
     */
    private User(String username,
                 String password,
                 MoodleCategory userCategory,
                 List<String> features,
                 List<Contact> contactsList) {

        this.username = username;
        this.password = password;
        this.usrCategory = userCategory;
        this.features = features;
        this.contactsList = contactsList;
    }

    /**
     *
     * create a default Moodle User from username and password
     *
     * @param username
     * @param password
     * @return
     */
    public static User initializeStandardUser(String username, String password) {

        User newMoodleUser = new User(username, password, null, null, null);
        newMoodleUser.setUsrCategory(MoodleCategory.STANDARD_FRIEND);

        ArrayList<String> defaultFeatures = new ArrayList<String>();
        //TODO set the features
        newMoodleUser.setFeatures(defaultFeatures);

        ArrayList<Contact> emptyContactsList = new ArrayList<>();
        newMoodleUser.setContactsList(emptyContactsList);

        return newMoodleUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MoodleCategory getUsrCategory() {
        return usrCategory;
    }

    public void setUsrCategory(MoodleCategory usrCategory) {
        this.usrCategory = usrCategory;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<Contact> getContactsList() {
        return contactsList;
    }

    public void setContactsList(List<Contact> contactsList) {
        this.contactsList = contactsList;
    }
}
