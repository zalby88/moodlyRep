package com.zalbyeco.albertolaz.moodly.fragments;

import java.util.ArrayList;
import java.util.Random;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.zalbyeco.albertolaz.moodly.main.MainActivity;
import com.zalbyeco.albertolaz.moodly.moodles.MoodleAdapter;
import com.zalbyeco.albertolaz.moodly.moodles.MoodleMsg;

import com.zalbyeco.albertolaz.moodly.R;
import com.zalbyeco.albertolaz.moodly.util.CommonUtils;

/**
 * Created by Alberto Lazzarin  on 01/08/2016. Basically the chats object
 */

public class Moodles extends Fragment implements OnClickListener {

    private EditText msgTextEdit;
    private String user1 = "khushi", user2 = "khushi1";
    private Random random;
    public static ArrayList<MoodleMsg> msgslist;
    public static MoodleAdapter chatAdapter;
    ListView moodlesMsgList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cht_lyt, container, false);
        random = new Random();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                "Moodles");
        msgTextEdit = (EditText) view.findViewById(R.id.msgTextEdit);
        moodlesMsgList = (ListView) view.findViewById(R.id.moodlyMsgList);
        ImageButton sendButton = (ImageButton) view
                .findViewById(R.id.sendBtn);
        sendButton.setOnClickListener(this);

        // ----Set autoscroll of listview when a new message arrives----//
        moodlesMsgList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        moodlesMsgList.setStackFromBottom(true);

        msgslist = new ArrayList<MoodleMsg>();
        chatAdapter = new MoodleAdapter(getActivity(), msgslist);
        moodlesMsgList.setAdapter(chatAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    public void sendTextMoodly(View v) {
        String message = msgTextEdit.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final MoodleMsg chatMessage = new MoodleMsg(user1, user1,
                    message, "" + random.nextInt(1000), true);
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = CommonUtils.getCurrentDate();
            chatMessage.Time = CommonUtils.getCurrentTime();
            msgTextEdit.setText("");
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
            MainActivity activity = ((MainActivity) getActivity());
            activity.getMoodleService().xmppManager.sendMessage(chatMessage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBtn:
                sendTextMoodly(v);

        }
    }

}