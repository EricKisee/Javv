package com.craptordevelopers.javv;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Eric on 3/16/2017.
 */

public class User {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean user_is_signed_in = false;
    private boolean signin_result = false;
    private boolean create_user_result = false;

    private String TAG = "JAVV Firebase";
    private String user_email = null;
    private String user_name = null;
    private String user_id = null;
    private Uri user_photo_uri = null;

    private Context context = null;
    private Activity activity = null;

    public User (Context context , Activity activity){
        this.context = context;
        this.activity = activity;
        init();

    }

    private void init (){
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    setUser(user);
                } else {
                    // User is signed out
                    destroyUser(user);
                }
            }
        };
    }

    private void setUser (FirebaseUser user){
        set_user_is_signed_in(true);
        setUserName(user.getDisplayName());
        setUserEmail (user.getEmail());
        setPhotoUri(user.getPhotoUrl());
        setUserId(user.getUid());
        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getToken() instead.
    }
    private void destroyUser (FirebaseUser user){
        set_user_is_signed_in(false);
        setUserName(null);
        setUserEmail (null);
        setPhotoUri(null);
        setUserId(null);
    }

    public void removeAuthStateListener(){
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void addAuthStateListener (){
        mAuth.addAuthStateListener(mAuthListener);
    }

    public boolean createUser (){
        return false;
    }

    public boolean signinUser (String email , String password){
        signin_result = false;
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            signin_result = false;
                            init();
                        }else {
                            signin_result = true;
                            init();
                        }

                        // ...
                    }
                });
        return signin_result;
    }

    public void changeEmail (){

    }

    public void changePassword(){

    }

    public void resetPassword (){

    }

    public  void deleteAcount (){

    }

    public String getUserName (){
        return this.user_name;
    }

    public String getEmail (){
        return this.user_email;
    }

    public String getUserId (){
        return this.user_id;
    }

    public Uri getPhotoUri (){
        return this.user_photo_uri;
    }

    public boolean isUser_is_signed_in(){
        return user_is_signed_in;
    }

    private void set_user_is_signed_in (boolean user_is_signed_in){
        this.user_is_signed_in = user_is_signed_in;
    }
    private void setUserName (String user_name){
        this.user_name = user_name;
    }

    private void setUserEmail (String user_email){
        this.user_email = user_email;
    }

    private void setUserId (String user_id){
        this.user_id = user_id;
    }

    private void setPhotoUri (Uri user_photo_uri){
        this.user_photo_uri = user_photo_uri;
    }

}
