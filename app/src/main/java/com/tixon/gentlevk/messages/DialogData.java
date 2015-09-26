package com.tixon.gentlevk.messages;

import java.util.ArrayList;

public class DialogData {
    DialogResponse response;

    public DialogResponse getResponse() {
        return response;
    }

    public class DialogResponse {
        public int count, unread_dialogs;
        ArrayList<Dialog> items;
    }
}
