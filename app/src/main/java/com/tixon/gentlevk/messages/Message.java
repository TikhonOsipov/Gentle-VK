package com.tixon.gentlevk.messages;

import com.tixon.gentlevk.attachments.Attachment;

import java.util.ArrayList;

/**
 * Created by Tixon
 */
public class Message {
    int id, out, user_id, read_state;
    long date;
    String title, body;
    private ArrayList<Attachment> attachments;

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }
}
