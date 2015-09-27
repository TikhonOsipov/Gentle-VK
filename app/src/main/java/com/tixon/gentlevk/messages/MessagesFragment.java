package com.tixon.gentlevk.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.tixon.gentlevk.R;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by Tixon
 */
public class MessagesFragment extends Fragment {

    RecyclerView recyclerView;
    MessagesRecyclerAdapter adapter;
    CustomLayoutManager layoutManager;
    Gson gson = new Gson();

    public static Fragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.messages_fragment_layout, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.dialogs_recycler_view);

        VKRequest.VKRequestListener messagesRequestListener = new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                DialogData data = gson.fromJson(response.json.toString(), DialogData.class);
                adapter = new MessagesRecyclerAdapter(getActivity(), data.getResponse().items);
                recyclerView.setAdapter(adapter);
                layoutManager = new CustomLayoutManager(getActivity());

                layoutManager.setOnOverScrollListener(new OnOverScrollListener() {
                    @Override
                    public void onOverScroll(int overScroll) {
                        Log.d("myLogs", "overScroll = " + overScroll);
                    }
                });

                recyclerView.setLayoutManager(layoutManager);

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

        //VKRequest messagesRequest = VKApi.messages().get();
        VKRequest messagesRequest = VKApi.messages().getDialogs();
        messagesRequest.setRequestListener(messagesRequestListener);
        messagesRequest.executeWithListener(messagesRequestListener);
        return v;
    }


}
