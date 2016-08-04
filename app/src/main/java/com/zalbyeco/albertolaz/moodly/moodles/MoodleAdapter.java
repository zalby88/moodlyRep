package com.zalbyeco.albertolaz.moodly.moodles;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zalbyeco.albertolaz.moodly.R;

/**
 * Created by Alberto Lazzarin on 01/08/2016.
 */
public class MoodleAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<MoodleMsg> mChatMsgList;

    public MoodleAdapter(Activity activity, ArrayList<MoodleMsg> msgList) {
        mChatMsgList = msgList;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mChatMsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoodleMsg message = (MoodleMsg) mChatMsgList.get(position);
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.moodly_bubbly, null);

        TextView msg = (TextView) vi.findViewById(R.id.bubblyText);
        msg.setText(message.body);
        LinearLayout layout = (LinearLayout) vi
                .findViewById(R.id.bubblyLyt);
        LinearLayout containerLayout = (LinearLayout) vi
                .findViewById(R.id.moodlyBubblyContainer);

        // if message is mine then align to right
        if (message.isMine) {
            layout.setBackgroundResource(R.drawable.bubble2);
            containerLayout.setGravity(Gravity.RIGHT);
        }
        // If not mine then align to left
        else {
            layout.setBackgroundResource(R.drawable.bubble1);
            containerLayout.setGravity(Gravity.LEFT);
        }
        msg.setTextColor(Color.BLACK);
        return vi;
    }

    public void add(MoodleMsg object) {
        mChatMsgList.add(object);
    }
}