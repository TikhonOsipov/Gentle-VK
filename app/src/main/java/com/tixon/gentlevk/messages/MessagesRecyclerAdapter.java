package com.tixon.gentlevk.messages;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tixon.gentlevk.Data;
import com.tixon.gentlevk.DrawableLoader;
import com.tixon.gentlevk.OnLoadFinishedListener;
import com.tixon.gentlevk.R;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tixon
 */
public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder> {

    ArrayList<Dialog> dialogs;
    Context context;
    Data data;
    Gson gson = new Gson();
    private static final int DRAWABLE_LOADER_ID = 1;

    private OnLoadFinishedListener onLoadFinishedListener;

    public void setOnLoadFinishedListener(OnLoadFinishedListener listener) {
        this.onLoadFinishedListener = listener;
    }

    public MessagesRecyclerAdapter(Context context, ArrayList<Dialog> dialogs) {
        this.context = context;
        this.dialogs = dialogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textViewUnread.setText(String.valueOf(dialogs.get(position).unread));
        //holder.textViewName.setText(String.valueOf(dialogs.get(position).getMessage().user_id));
        holder.textViewMessage.setText(dialogs.get(position).getMessage().body);

        VKRequest.VKRequestListener userInfoListener = new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("myLogs", response.json.toString());
                data = gson.fromJson(response.json.toString(), Data.class);

                holder.textViewName.setText(data.getResponse().get(0).first_name + " " + data.getResponse().get(0).last_name);
                final Handler handler = new Handler();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Drawable d = loadImageFromUrl(data.getResponse().get(0).photo_50);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.imageViewPhoto.setImageDrawable(d);
                            }
                        });
                    }
                });
                t.start();
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

        VKRequest requestUser = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID,
                String.valueOf(dialogs.get(position).getMessage().user_id),
                VKApiConst.FIELDS, "first_name,last_name,photo_50"));
        requestUser.setRequestListener(userInfoListener);
        requestUser.executeWithListener(userInfoListener);
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhoto;
        TextView textViewName, textViewMessage, textViewUnread;
        View frame;
        public ViewHolder(View itemView) {
            super(itemView);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.dialog_photo);
            textViewName = (TextView) itemView.findViewById(R.id.tv_dialog_username);
            textViewMessage = (TextView) itemView.findViewById(R.id.tv_dialog_message);
            textViewUnread = (TextView) itemView.findViewById(R.id.tv_unread_messages);
            frame = itemView.findViewById(R.id.dialog_frame_click);
        }
    }



    public Drawable loadImageFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            Log.d("myLogs", e.toString());
            return null;
        }
    }
}
