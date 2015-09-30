package com.tixon.gentlevk.attachments;

import com.tixon.gentlevk.attachments.service_fields.Comments;
import com.tixon.gentlevk.attachments.service_fields.Likes;
import com.tixon.gentlevk.attachments.service_fields.Reposts;

import java.util.ArrayList;

/**
 * Created by Tixon
 */
public class Wall {
    public int id, owner_id, from_id, date, reply_owner_id, reply_post_id, friends_only, signer_id;
    public int can_pin, is_pinned;
    public String text, post_type;
    public PostSource post_source;
    public ArrayList<Wall> copy_history;

    public ArrayList<Attachment> attachments;

    public Comments comments;
    public Likes likes;
    public Reposts reposts;

    public class PostSource {
        String type, platform, data, url;
    }
}
