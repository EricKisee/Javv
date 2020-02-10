package com.craptordevelopers.javv;

/**
 * Created by Eric on 3/14/2017.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ChatAdapter extends BaseAdapter {
    private final Activity activity;

    private static LayoutInflater inflater = null;
    ArrayList<ChatMessage> chatMessageList;

    public ChatAdapter(Activity activity, ArrayList<ChatMessage> list) {
        this.activity = activity;
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatMessageList.size();
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
        ChatMessage message = (ChatMessage) chatMessageList.get(position);
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.chat_bubble, null);

        TextView msg = (TextView) vi.findViewById(R.id.message_text);
        ImageButton imageView = (ImageButton) vi.findViewById(R.id.imageButtonView);
        LinearLayout bubble_layout = (LinearLayout) vi.findViewById(R.id.bubble_layout);

        if (message.messageBody.startsWith("https://firebasestorage.googleapis.com/") ||message.messageBody.startsWith("content://")) {
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            bubble_layout.setLayoutParams(param);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(activity)
                    .load(message.messageBody)
                    .into(imageView);
            msg.setText(message.messageBody);
        }else {
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            bubble_layout.setLayoutParams(param);
            imageView.setVisibility(View.GONE);
            msg.setText(message.messageBody);
        }
        LinearLayout layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout);
        LinearLayout parent_layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout_parent);

        // if message is mine then align to right
        if (message.senderUId ==  FirebaseAuth.getInstance(FirebaseApp.getInstance()).getCurrentUser().getUid()) {
            layout.setBackgroundResource(R.drawable.bubble2);
            parent_layout.setGravity(Gravity.RIGHT);
        }
        // If not mine then align to left
        else {
            layout.setBackgroundResource(R.drawable.bubble1);
            parent_layout.setGravity(Gravity.LEFT);
        }
        msg.setTextColor(Color.BLACK);
        return vi;
    }

    public void add(ChatMessage object) {
        chatMessageList.add(object);
    }
}
