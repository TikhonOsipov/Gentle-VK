package com.tixon.gentlevk.messages;

import com.tixon.gentlevk.attachments.Attachment;

import java.util.ArrayList;

/**
 * Created by Tixon
 */
public class Message {
    public int id, out, user_id, read_state;
    public long date;
    public String title, body;
    private ArrayList<Attachment> attachments;

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }
}
