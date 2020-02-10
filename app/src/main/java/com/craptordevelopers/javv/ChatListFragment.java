package com.craptordevelopers.javv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Eric on 3/4/2017.
 */

public class ChatListFragment extends Fragment {

    private FirebaseApp app =null;
    private FirebaseDatabase database = null;
    private FirebaseAuth auth = null;
    private FirebaseStorage storage = null ;
    private FirebaseUser user = null;

    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    private View mView;
    private ListView chat_list ;
    public static ArrayList<JavvUser> javvUsersList;
    public static ChatListAdapter chatListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        init();
    }

    private void init (){
        chat_list = (ListView) mView.findViewById(R.id.lv_chat_list);

        javvUsersList = new ArrayList<JavvUser>();
        chatListAdapter = new ChatListAdapter(getActivity(), javvUsersList);
        chat_list.setAdapter(chatListAdapter);
        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("selected_user_email", javvUsersList.get(position).userEmail);
                intent.putExtra("selected_user_fullname", javvUsersList.get(position).userFullName);
                intent.putExtra("selected_user_id", javvUsersList.get(position).userId);
                intent.putExtra("selected_username", javvUsersList.get(position).username);
                startActivity(intent);
            }
        });

        //Get the firebase app and all primitives we'll use

        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);

        //Get reference to our chat in the database
        databaseRef = database.getReference("users");
        databaseRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot snapshot, String s) {
                // Get the chat message from the snapshot and add it to the UI
                JavvUser user = snapshot.getValue(JavvUser.class);
                chatListAdapter.add(user);
                chatListAdapter.notifyDataSetChanged();
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            public void onCancelled(DatabaseError databaseError) { }
        });

    }



}
