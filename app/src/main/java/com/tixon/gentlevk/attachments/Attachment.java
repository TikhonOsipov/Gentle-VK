package com.tixon.gentlevk.attachments;

/**
 * Created by Tixon
 */
public class Attachment {
    public Photo photo;
    public Audio audio;
    public Video video;
    public Document doc;
    public Poll poll;
    public Wall wall;

    public String getAttachment() {
        StringBuilder attachment = new StringBuilder("");
        if(audio != null) {
            attachment.append("Аудио: ");
            attachment.append(audio.artist).append(" - ").append(audio.title);
            attachment.append(" (").append(getTime(audio.duration)).append(")");
        }
        return attachment.toString();
    }

    public static String getTime(int seconds) {
        String time;
        int h, m, s;
        m = seconds / 60;
        h = seconds / 3600;
        s = seconds - m*60 - h*3600;

        if(h > 0) time = String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s);
        else time = String.valueOf(m) + ":" + String.valueOf(s);
        return time;
    }
}
