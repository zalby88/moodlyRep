package com.zalbyeco.albertolaz.moodly.moodles;

import java.util.Random;

/**
 * Created by Alberto Lazzarin on 01/08/2016.
 */


public class MoodleMsg {

    public String body, sender, receiver, senderName;
    public String Date, Time;
    public String msgid;
    public boolean isMine;// Did I send the message.

    public MoodleMsg(String moodleSender, String moodleReceiver, String messageString,
                       String ID, boolean isMINE) {
        body = messageString;
        isMine = isMINE;
        sender = moodleSender;
        msgid = ID;
        receiver = moodleReceiver;
        senderName = sender;
    }

    public void generateMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));
        ;
    }
}
