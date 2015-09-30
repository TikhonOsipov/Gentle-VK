package com.tixon.gentlevk.attachments;

import com.vk.sdk.api.model.VKApiPoll;

import java.util.ArrayList;

/**
 * Created by Tixon
 */
public class Poll {
    public int id, owner_id, created, votes, answer_id;
    public String question;
    public ArrayList<Answer> answers;

    public class Answer {
        public int id, votes, rate;
        public String text;
    }
}
