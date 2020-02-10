package com.craptordevelopers.javv;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import java.util.ArrayList;
import java.util.Random;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.UploadTask;

public class ChatActivity extends AppCompatActivity implements OnClickListener {

    private EditText msg_edittext;
    private String user1 = "khushi", user2 = "Reeiver";
    private Random random;
    public static ArrayList<ChatMessage>chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;


    static final int RC_PHOTO_PICKER = 1;

    private FirebaseApp app =null;
    private FirebaseDatabase database = null;
    private FirebaseAuth auth = null;
    private FirebaseStorage storage = null ;

    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    private String userId;
    private String username;
    private ImageButton sendButton , sendImageButton;
    JavvUser selectedUser ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selected_username= this.getIntent().getExtras().getString("selected_username");
        String selected_user_id= this.getIntent().getExtras().getString("selected_user_id");
        String selected_user_fullname= this.getIntent().getExtras().getString("selected_user_fullname");
        String selected_user_email= this.getIntent().getExtras().getString("selected_user_email");

        selectedUser = new JavvUser(selected_user_id,selected_user_email,selected_user_fullname,selected_username);


        setContentView(R.layout.activity_chat_layout);
        msg_edittext = (EditText) findViewById(R.id.messageEditText);
        msgListView = (ListView) findViewById(R.id.msgListView);
        sendButton = (ImageButton)findViewById(R.id.sendMessageButton);
        sendImageButton = (ImageButton)findViewById(R.id.sendImageButton);
        sendButton.setOnClickListener(this);

        //Get the firebase app and all primitives we'll use

        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);

        //Get reference to our chat in the database
        databaseRef = database.getReference("chat");
        databaseRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot snapshot, String s) {
                // Get the chat message from the snapshot and add it to the UI
                ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                chatAdapter.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            public void onCancelled(DatabaseError databaseError) { }
        });

        // get current user details
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // User signed in, set their email address as the user name
                    setUId(firebaseAuth.getCurrentUser().getUid());
                    setUsername(firebaseAuth.getCurrentUser().getEmail());
                }
                else {
                    // User signed out, set a default username
                    setUId("Android");
                }
            }
        });

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(this, chatlist);
        msgListView.setAdapter(chatAdapter);
    }

    private void setUId(String userId) {
        Log.d("ChatActivity", "setUsername("+String.valueOf(userId)+")");
        if (userId == null) {
            userId = "Android";
        }
        boolean isLoggedIn = !userId.equals("Android");
        this.userId = userId;
    }
    private void setUsername(String username) {
        Log.d("ChatActivity", "setUsername("+String.valueOf(username)+")");
        if (username == null) {
            username = "Android";
        }
        boolean isLoggedIn = !username.equals("Android");
        this.username = username;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Get a reference to the location where we'll store our photos
            storageRef = storage.getReference("chat_photos");
            // Get a reference to store file at chat_photos/<FILENAME>
            final StorageReference photoRef = storageRef.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess( UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            @SuppressWarnings("VisibleForTests")
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            // Set the download URL to the message box, so that the user can send it to the database
                            msg_edittext.setText(downloadUrl.toString());
                            sendTextMessage(sendImageButton);
                        }
                    });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    public void sendTextMessage(View v) {
        random = new Random();
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage(userId, selectedUser.userId,message);
            chatMessage.messageBody = message;
            chatMessage.receiverUsername = selectedUser.userEmail;
            chatMessage.senderUsername = username;
            chatMessage.messageDate = CommonMethods.getCurrentDate();
            chatMessage.messageTime = CommonMethods.getCurrentTime();


            databaseRef.push().setValue(chatMessage);

            msg_edittext.setText("");
//            chatAdapter.add(chatMessage);
//            chatAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                sendTextMessage(v);
                break;
            case R.id.sendImageButton:
                break;

        }
    }

}
