package com.craptordevelopers.javv;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Eric on 3/16/2017.
 */

public class User2 {

    private FirebaseUser user = null ;

    public User2 (FirebaseUser user){
        this.user = user ;
    }

    public String getUserId (){
        return user.getUid();
    }
    public String getDisplayName (){
        return user.getDisplayName();
    }
    public String getEmail (){
        return user.getEmail();
    }
    public Uri getUserPhotoUri (){
        return user.getPhotoUrl();
    }

}
