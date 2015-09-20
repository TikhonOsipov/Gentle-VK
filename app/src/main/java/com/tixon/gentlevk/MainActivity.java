package com.tixon.gentlevk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.main_activity_text);

        VKRequest requestProfile = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                "id,first_name,last_name,sex,bdate,city,country,photo_50,photo_100," +
                        "photo_200_orig,photo_200,photo_400_orig,photo_max,photo_max_orig,online," +
                        "online_mobile,lists,domain,has_mobile,contacts,connections,site,education," +
                        "universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message," +
                        "status,last_seen,common_count,relation,relatives,counters"));

        requestProfile.setRequestListener(profileListener);
        requestProfile.executeWithListener(profileListener);
    }

    VKRequest.VKRequestListener profileListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            Data data = gson.fromJson(response.json.toString(), Data.class);
            Log.d("myLogs", "response = " + response.json.toString());
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
}
