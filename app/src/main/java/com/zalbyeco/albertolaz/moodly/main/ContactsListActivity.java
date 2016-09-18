package com.zalbyeco.albertolaz.moodly.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zalbyeco.albertolaz.moodly.R;
import com.zalbyeco.albertolaz.moodly.service.mock.ContactsListServiceMock;
import com.zalbyeco.albertolaz.moodly.users.Contact;

import java.util.List;

public class ContactsListActivity extends AppCompatActivity {

    private String selectedUsername;
    private String typedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        this.setTitle("Lista Contatti");

        //User data retrieval
        //getting the calling intent, username and password needed for the main messaging application
        Intent callingIntent = getIntent();
        setSelectedUsername(callingIntent.getStringExtra(MainConstants.INTENT_EXTRA_USERNAME_KEY));
        setTypedPassword(callingIntent.getStringExtra(MainConstants.INTENT_EXTRA_PASSWORD_KEY));

        //Toolbar settings
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Floating ActionButton settings
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //****************************
        //Contacts List Items settings
        populateContactsList();

    }

    private void populateContactsList() {

        //Call the Service
        List<Contact> retrievedContacts = retrieveContactsList();

        //Contacts -> Layout Items
        RelativeLayout contactsListContainer = (RelativeLayout) findViewById(R.id.contactsListPanel);

        LayoutInflater inflater =
                (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /**
         * Dynamically creates the list of contacts
         */
        int lastLlayoutId = -1;

        for (Contact currentContact: retrievedContacts) {
            LinearLayout llayoutToAdd = (LinearLayout) inflater.inflate(R.layout.contacts_item, null);

            //generate ID
            llayoutToAdd.setId(View.generateViewId());

            for (int i = 0; i < llayoutToAdd.getChildCount(); i++) {
                Object item = llayoutToAdd.getChildAt(i);
                if (item instanceof TextView) {
                    TextView txtView = (TextView) item;

                    //sets the username as text
                    txtView.setText(currentContact.getUsername());

                } else if (item instanceof ImageView) {
                    //TODO
                }
            }

            //----------------------
            //On touch (change color)
            llayoutToAdd.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    return contactsListItemOnTouch(view, event);
                }
            });

            //--------
            //On click
            llayoutToAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactsListItemOnClick(v);
                }
            });

            //fix the position below the previous item
            if (lastLlayoutId != -1) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llayoutToAdd.getLayoutParams();
                if (params == null) {
                    params = new RelativeLayout.LayoutParams
                            (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }

                params.addRule(RelativeLayout.BELOW, lastLlayoutId);
                llayoutToAdd.setLayoutParams(params);
            }
            //update the last layout id
            lastLlayoutId = llayoutToAdd.getId();

            //add the new view to the container
            contactsListContainer.addView(llayoutToAdd);

        }//endfor
    }//populateContactsList private method

    /**
     * Calls the "Contacts List" service to retrieve all contacts for a selected user
     */
    private List<Contact> retrieveContactsList() {
        ContactsListServiceMock contactsService = ContactsListServiceMock.getContactsListServiceMock();
        return contactsService.retrieveContacts(selectedUsername);
    }

    //*****************************
    //Contacts List Items functions

    public void contactsListItemOnClick(View v) {
        goToMainMessagingActivity();
    }

    private void goToMainMessagingActivity() {
        Intent msgActivityIntnt = new Intent();
        msgActivityIntnt.setClass(getApplicationContext(),MainActivity.class);

        msgActivityIntnt.putExtra(MainConstants.INTENT_EXTRA_USERNAME_KEY, getSelectedUsername());
        msgActivityIntnt.putExtra(MainConstants.INTENT_EXTRA_PASSWORD_KEY, getTypedPassword());

        startActivity(msgActivityIntnt);
    }

    public boolean contactsListItemOnTouch(View view, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.setBackgroundColor(
                        ContextCompat.getColor(getApplicationContext(), R.color.colorContactsItemSelected));
                break;
            case MotionEvent.ACTION_UP:
                view.setBackgroundColor(Color.WHITE);
                break;
        }

        return false;
    }
    //*****************************

    public String getSelectedUsername() {
        return selectedUsername;
    }

    public void setSelectedUsername(String selectedUsername) {
        this.selectedUsername = selectedUsername;
    }

    public String getTypedPassword() {
        return typedPassword;
    }

    public void setTypedPassword(String typedPassword) {
        this.typedPassword = typedPassword;
    }

}
