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

public class ChatListAdapter extends BaseAdapter {
    private final Activity activity;

    private static LayoutInflater inflater = null;
    ArrayList<JavvUser> javvUsersList;

    public ChatListAdapter(Activity activity, ArrayList<JavvUser> list) {
        this.activity = activity;
        javvUsersList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return javvUsersList.size();
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
        JavvUser javvUser = (JavvUser) javvUsersList.get(position);
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.chat_list_item, null);

        TextView tv_username = (TextView) vi.findViewById(R.id.tv_username);
        TextView tv_user_email = (TextView) vi.findViewById(R.id.tv_user_email);
        TextView tv_user_id = (TextView) vi.findViewById(R.id.tv_user_id);
        tv_username.setText(javvUser.username);
        tv_user_email.setText(javvUser.userEmail);
        tv_user_id.setText(javvUser.userId);
        return vi;
    }

    public void add(JavvUser object) {
        javvUsersList.add(object);
    }
}
