package com.craptordevelopers.javv;

/**
 * Created by Eric on 3/14/2017.
 */

import java.util.Random;

public class ChatMessage {

    public String messageBody, senderUId, receiverUId, senderUsername, receiverUsername,messageDate, messageTime;

    public ChatMessage() {
    }

    public ChatMessage(String senderUId, String receiverUId, String messageString) {
        this.messageBody = messageString;
        this.senderUId = senderUId;
        this.receiverUId = receiverUId;
    }

}
