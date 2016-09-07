package com.zalbyeco.albertolaz.moodly.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zalbyeco.albertolaz.moodly.R;

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
        RelativeLayout contactsListContainer = (RelativeLayout) findViewById(R.id.contactsListPanel);
        for (int i = 0; i < contactsListContainer.getChildCount(); i++) {
            Object item = contactsListContainer.getChildAt(i);
            if (item instanceof LinearLayout) {
                //----------------------
                //On touch (change color)
                ((LinearLayout) item).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        return contactsListItemOnTouch(view, event);
                    }
                });

                //--------
                //On click
                ((LinearLayout) item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactsListItemOnClick(v);
                    }
                });
            }
        }

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
