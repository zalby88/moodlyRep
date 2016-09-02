package com.zalbyeco.albertolaz.moodly.service;

import org.jivesoftware.smack.chat.Chat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.zalbyeco.albertolaz.moodly.main.MainConstants;
import com.zalbyeco.albertolaz.moodly.util.XMPPManager;
import com.zalbyeco.albertolaz.moodly.users.User;

/**
 * Created by Alberto Lazzarin on 04/08/2016.
 */
public class MoodleService extends Service {

    //STATIC FIELDS
    private static final String DOMAIN = "xmpp.jp";
    public static ConnectivityManager connectivityManager;
    public static XMPPManager xmppManager;
    public static boolean serverChatCreated = false;

    //NON-STATIC FIELDS
    private String mUsername;
    private String mPassword;
    private String mText = "";


    @Override
    public IBinder onBind(final Intent intent) {

        this.mUsername = intent.getStringExtra(MainConstants.INTENT_EXTRA_USERNAME_KEY);
        this.mPassword = intent.getStringExtra(MainConstants.INTENT_EXTRA_PASSWORD_KEY);

        xmppManager = XMPPManager.getInstance(MoodleService.this, DOMAIN, this.mUsername, this.mPassword);
        xmppManager.connect("onCreate");

        return new LocalBinder<MoodleService>(this);
    }

    public Chat chat;

    @Override
    public void onCreate() {
        super.onCreate();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xmppManager.getTcpConnection().disconnect();
    }

    public static boolean isNetworkConnected() {
        return connectivityManager.getActiveNetworkInfo() != null;
    }


    public String getCurrentUsername() {
        return mUsername;
    }

    public void setCurrentUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getCurrentUserPassword() {
        return mPassword;
    }

    public void setCurrentUserPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getMessageText() {
        return mText;
    }

    public void setMessageText(String mText) {
        this.mText = mText;
    }
}