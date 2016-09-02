package com.zalbyeco.albertolaz.moodly.util;

/**
 * Created by Alberto Lazzarin on 04/08/2016. The XMPP Connection manager
 */
import java.io.IOException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zalbyeco.albertolaz.moodly.R;
import com.zalbyeco.albertolaz.moodly.fragments.MoodlesSection;
import com.zalbyeco.albertolaz.moodly.moodles.MoodleMsg;
import com.zalbyeco.albertolaz.moodly.service.MoodleService;

public class XMPPManager {



    private static boolean connecting = false;
    private static boolean toasted = true;
    private static boolean instanceCreated = false;
    private static XMPPTCPConnection tcpConnection = null;
    private static boolean connected = false;

    private boolean isLoggedIn = false;
    private boolean isMoodleCreated = false;
    private String serverAddress;

    private static String loginUser;
    private static String passwordUser;
    private Gson gson;
    private MoodleService context;
    private static XMPPManager instance;


    public org.jivesoftware.smack.chat.Chat myChat;

    ChatManagerListenerImpl mChatManagerListener;
    MMessageListener mMessageListener;

    String text = "";
    String mMessage = "";
    String mReceiver = "";
    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (ClassNotFoundException ex) {
            // problem loading reconnection manager
        }
    }

    private XMPPManager(MoodleService context, String serverAdress, String loginUser,
                  String passwordser) {
        this.serverAddress = serverAdress;
        this.loginUser = loginUser;
        this.passwordUser = passwordser;
        this.context = context;
        init();
    }

    public static XMPPManager getInstance(MoodleService context, String server,
                                     String user, String pass) {

        if (instance == null) {
            instance = new XMPPManager(context, server, user, pass);
            instanceCreated = true;
        }
        return instance;

    }

    public void init() {
        gson = new Gson();
        mMessageListener = new MMessageListener(context);
        mChatManagerListener = new ChatManagerListenerImpl();
        initialiseConnection();

    }

    private void initialiseConnection() {

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                .builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(serverAddress);
        config.setHost(serverAddress);
        config.setPort(5222);
        config.setDebuggerEnabled(true);
        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);
        tcpConnection = new XMPPTCPConnection(config.build());
        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        tcpConnection.addConnectionListener(connectionListener);
    }

    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tcpConnection.disconnect();
            }
        }).start();
    }

    public void connect(final String caller) {

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {
                if (tcpConnection.isConnected())
                    return false;
                setConnecting(true);
                if (isToasted())
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {

                            Toast.makeText(context,
                                    caller + "=>connecting....",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                Log.d("Connect() Function", caller + "=>connecting....");

                try {
                    tcpConnection.connect();
                    DeliveryReceiptManager dm = DeliveryReceiptManager
                            .getInstanceFor(tcpConnection);
                    dm.setAutoReceiptMode(AutoReceiptMode.always);
                    dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                        @Override
                        public void onReceiptReceived(final String fromid,
                                                      final String toid, final String msgid,
                                                      final Stanza packet) {

                        }
                    });
                    setConnected(true);

                } catch (IOException e) {
                    if (isToasted())
                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {

                                    @Override
                                    public void run() {

                                        Toast.makeText(
                                                context,
                                                "(" + caller + ")"
                                                        + "IOException: ",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                    Log.e("(" + caller + ")", "IOException: " + e.getMessage());
                } catch (SmackException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(context,
                                    "(" + caller + ")" + "SMACKException: ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("(" + caller + ")",
                            "SMACKException: " + e.getMessage());
                } catch (XMPPException e) {
                    if (isToasted())

                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {

                                    @Override
                                    public void run() {

                                        Toast.makeText(
                                                context,
                                                "(" + caller + ")"
                                                        + "XMPPException: ",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    Log.e("connect(" + caller + ")",
                            "XMPPException: " + e.getMessage());

                }
                setConnecting(false);
                return isConnecting();
            }
        };
        connectionThread.execute();
    }

    public void login() {

        try {
            tcpConnection.login(loginUser, passwordUser);
            Log.i("LOGIN", "Yey! We're connected to the Xmpp server!");

        } catch (XMPPException | SmackException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

    }

    private class ChatManagerListenerImpl implements ChatManagerListener {
        @Override
        public void chatCreated(final org.jivesoftware.smack.chat.Chat chat,
                                final boolean createdLocally) {
            if (!createdLocally)
                chat.addMessageListener(mMessageListener);

        }

    }

    public void sendMessage(MoodleMsg chatMessage) {
        String body = gson.toJson(chatMessage);

        if (!isMoodleCreated) {
            myChat = ChatManager.getInstanceFor(tcpConnection).createChat(
                    chatMessage.receiver + "@"
                            + context.getString(R.string.server),
                    mMessageListener);
            isMoodleCreated = true;
        }
        final Message message = new Message();
        message.setBody(body);

        message.setStanzaId(chatMessage.msgid);
        message.setType(Message.Type.chat);

        try {
            if (tcpConnection.isAuthenticated()) {

                myChat.sendMessage(message);

            } else {

                login();
            }
        } catch (NotConnectedException e) {
            Log.e("xmpp.SendMessage()", "msg Not sent!-Not Connected!");

        } catch (Exception e) {
            Log.e("xmpp.SendMessage()",
                    "msg Not sent!" + e.getMessage());
        }

    }

    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {

            Log.d("xmpp", "Connected!");
            setConnected(true);
            if (!connection.isAuthenticated()) {
                login();
            }
        }

        @Override
        public void connectionClosed() {
            if (isToasted())

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        Toast.makeText(context, "ConnectionCLosed!",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ConnectionCLosed!");
            setConnected(false);
            isMoodleCreated = false;
            isLoggedIn = false;
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            if (isToasted())

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(context, "ConnectionClosedOn Error!!",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ConnectionClosedOn Error!");
            setConnected(false);

            isMoodleCreated = false;
            isLoggedIn = false;
        }

        @Override
        public void reconnectingIn(int arg0) {
            Log.d("xmpp", "Reconnectingin " + arg0);
            isLoggedIn = false;
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            if (isToasted())
                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(context, "ReconnectionFailed!",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ReconnectionFailed!");
            setConnected(false);

            isMoodleCreated = false;
            isLoggedIn = false;
        }

        @Override
        public void reconnectionSuccessful() {
            if (isToasted())

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        Toast.makeText(context, "REConnected!",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ReconnectionSuccessful");
            setConnected(true);

            isMoodleCreated = false;
            isLoggedIn = false;
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d("xmpp", "Authenticated!");
            isLoggedIn = true;

            ChatManager.getInstanceFor(tcpConnection).addChatListener(
                    mChatManagerListener);

            isMoodleCreated = false;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();
            if (isToasted())

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        Toast.makeText(context, "Connected!",
                                Toast.LENGTH_SHORT).show();

                    }
                });
        }
    }

    private class MMessageListener implements ChatMessageListener {

        public MMessageListener(Context contxt) {
        }

        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat,
                                   final Message message) {
            Log.i("MyXMPP_MESSAGE_LISTENER", "Xmpp message received: '"
                    + message);

            if (message.getType() == Message.Type.chat
                    && message.getBody() != null) {
                final MoodleMsg chatMessage = gson.fromJson(
                        message.getBody(), MoodleMsg.class);

                processMessage(chatMessage);
            }
        }

        private void processMessage(final MoodleMsg chatMessage) {

            chatMessage.isMine = false;
            MoodlesSection.msgslist.add(chatMessage);
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    MoodlesSection.chatAdapter.notifyDataSetChanged();

                }
            });
        }

    }

    //GETTERS AND SETTERS


    public static XMPPTCPConnection getTcpConnection() {
        return tcpConnection;
    }

    public static boolean isConnected() {
        return connected;
    }

    public static void setConnected(boolean connected) {
        XMPPManager.connected = connected;
    }

    public static boolean isInstanceCreated() {
        return instanceCreated;
    }

    public static void setInstanceCreated(boolean instanceCreated) {
        XMPPManager.instanceCreated = instanceCreated;
    }

    public static boolean isConnecting() {
        return connecting;
    }

    public static void setConnecting(boolean connecting) {
        XMPPManager.connecting = connecting;
    }

    public static boolean isToasted() {
        return toasted;
    }

    public static void setToasted(boolean toasted) {
        XMPPManager.toasted = toasted;
    }


}//class XMPPManager