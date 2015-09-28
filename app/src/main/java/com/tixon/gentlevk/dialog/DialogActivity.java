package com.tixon.gentlevk.dialog;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.tixon.gentlevk.BaseActivity;
import com.tixon.gentlevk.R;
import com.tixon.gentlevk.messages.DialogData;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by Tixon
 */
public class DialogActivity extends BaseActivity {
    Toolbar toolbar;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_layout);

        toolbar = (Toolbar) findViewById(R.id.dialog_activity_app_bar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Dialog activity");
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        VKRequest.VKRequestListener dialogRequestListener = new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("myLogs", "dialog = " + response.json.toString());
                MessagesHistoryData data = gson.fromJson(response.json.toString(), MessagesHistoryData.class);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        };

        int userId = getIntent().getIntExtra("user_id", 0);
        Log.d("myLogs", "user_id = " + userId);

        //VKRequest dialogRequest = VKApi.messages().get(VKParameters.from(VKApiConst.USER_ID, String.valueOf(userId)));
        VKRequest dialogRequest = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, String.valueOf(userId)))
                ;
        dialogRequest.setRequestListener(dialogRequestListener);
        dialogRequest.executeWithListener(dialogRequestListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }
}
