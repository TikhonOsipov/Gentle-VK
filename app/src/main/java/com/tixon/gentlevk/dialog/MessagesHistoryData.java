package com.tixon.gentlevk.dialog;

import com.tixon.gentlevk.messages.Message;

import java.util.ArrayList;

/**
 * Created by Tixon
 */
public class MessagesHistoryData {
    MessageResponse response;

    public MessageResponse getMessageResponse() {
        return response;
    }

    public class MessageResponse {
        int count;
        private ArrayList<Message> items;

        public ArrayList<Message> getItems() {
            return items;
        }
    }
}
