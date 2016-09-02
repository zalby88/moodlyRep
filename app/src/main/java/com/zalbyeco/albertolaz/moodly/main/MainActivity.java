package com.zalbyeco.albertolaz.moodly.main;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zalbyeco.albertolaz.moodly.R;
import com.zalbyeco.albertolaz.moodly.fragments.MoodlesSection;
import com.zalbyeco.albertolaz.moodly.service.LocalBinder;
import com.zalbyeco.albertolaz.moodly.service.MoodleService;
import com.zalbyeco.albertolaz.moodly.users.User;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean mBounded;
    private MoodleService mService;
    private ServiceConnection mConnection;
    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the calling intent, username and password needed
        Intent callingIntent = getIntent();
        String usrName = callingIntent.getStringExtra(MainConstants.INTENT_EXTRA_USERNAME_KEY);
        String usrPassword = callingIntent.getStringExtra(MainConstants.INTENT_EXTRA_PASSWORD_KEY);

        //creating the user
        mCurrentUser = getMoodlyUser(usrName, usrPassword);

        initializeServiceConnection();
        doBindService();
    }

    private User getMoodlyUser(String userName, String password) {
        //TODO call service for User features

        return User.initializeStandardUser(userName, password);
    }

    private void initializeServiceConnection() {
        this.mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(final ComponentName name,
                                           final IBinder service) {
                mService = ((LocalBinder<MoodleService>) service).getService();
                mBounded = true;
                Log.d(TAG, "onServiceConnected");
            }

            @Override
            public void onServiceDisconnected(final ComponentName name) {
                mService = null;
                mBounded = false;
                Log.d(TAG, "onServiceDisconnected");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    void doBindService() {

        Intent bindIntent = new Intent(this, MoodleService.class);
        bindIntent.putExtra(MainConstants.INTENT_EXTRA_USERNAME_KEY, mCurrentUser.getUsername());
        bindIntent.putExtra(MainConstants.INTENT_EXTRA_PASSWORD_KEY, mCurrentUser.getPassword());

        bindService(bindIntent, mConnection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }

    public MoodleService getMoodleService() {
        return mService;
    }


    public User getCurrentUser() {
        return mCurrentUser;
    }
}



