package com.zalbyeco.albertolaz.moodly.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlbertoLaz on 17/09/2016.
 */
public class Contact implements Serializable {


    private String username;
    private MoodleCategory ctcCategory;
    private List<String> features;

    /**
     *
     * Private constructor, you need to use static method "initializeStandardContact" instead of the constructor
     *
     * @param username
     * @param contactCategory
     * @param features
     */
    private Contact(String username,
                 MoodleCategory contactCategory,
                 List<String> features) {

        this.username = username;
        this.ctcCategory = contactCategory;
        this.features = features;
    }

    /**
     *
     * create a default Moodle Contact from username
     *
     * @param username
     * @return
     */
    public static Contact initializeStandardContact(String username) {

        Contact newMoodleContact = new Contact(username, null, null);
        newMoodleContact.setCategory(MoodleCategory.STANDARD_FRIEND);

        ArrayList<String> defaultFeatures = new ArrayList<String>();
        //TODO set the features
        newMoodleContact.setFeatures(defaultFeatures);

        return newMoodleContact;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MoodleCategory getCategory() {
        return ctcCategory;
    }

    public void setCategory(MoodleCategory contactCategory) {
        this.ctcCategory = contactCategory;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

}
