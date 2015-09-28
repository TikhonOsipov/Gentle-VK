package com.tixon.gentlevk.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tixon.gentlevk.R;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by Tixon
 */
public class DialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_layout);

        VKRequest.VKRequestListener dialogRequestListener = new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("myLogs", "dialog = " + response.json.toString());
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

        VKRequest dialogRequest = VKApi.messages().get(VKParameters.from(VKApiConst.USER_ID, userId));
        dialogRequest.setRequestListener(dialogRequestListener);
        dialogRequest.executeWithListener(dialogRequestListener);
    }
}
