package com.craptordevelopers.javv;

/**
 * Created by Eric on 3/17/2017.
 */

public class JavvUser {

    public String userFullName;
    public String username;
    public String userId;
    public String userEmail;

    public JavvUser(){

    }

    public JavvUser(String userId , String userEmail) {
        // ...
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public JavvUser(String userId, String userEmail, String userFullName) {
        // ...
        this.userId = userId;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
    }

    public JavvUser(String userId, String userEmail, String userFullName, String username) {
        // ...
        this.userId = userId;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.username = username;
    }
}
