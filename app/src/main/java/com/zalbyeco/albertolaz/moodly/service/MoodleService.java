package com.zalbyeco.albertolaz.moodly.service;

import org.jivesoftware.smack.chat.Chat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.zalbyeco.albertolaz.moodly.util.XMPPManager;

/**
 * Created by Alberto Lazzarin on 04/08/2016.
 */
public class MoodleService extends Service {
    private static final String DOMAIN = "xmpp.jp";
    private static final String USERNAME = "khushi";
    private static final String PASSWORD = "password";
    public static ConnectivityManager connectivityManager;
    public static XMPPManager xmppManager;
    public static boolean serverChatCreated = false;
    String text = "";


    @Override
    public IBinder onBind(final Intent intent) {
        return new LocalBinder<MoodleService>(this);
    }

    public Chat chat;

    @Override
    public void onCreate() {
        super.onCreate();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        xmppManager = XMPPManager.getInstance(MoodleService.this, DOMAIN, USERNAME, PASSWORD);
        xmppManager.connect("onCreate");
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
}