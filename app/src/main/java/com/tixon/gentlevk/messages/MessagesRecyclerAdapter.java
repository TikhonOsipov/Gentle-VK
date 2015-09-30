package com.tixon.gentlevk.messages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tixon.gentlevk.Data;
import com.tixon.gentlevk.R;
import com.tixon.gentlevk.attachments.Attachment;
import com.tixon.gentlevk.dialog.DialogActivity;
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
    Activity activity;
    Data data;
    Gson gson = new Gson();

    public MessagesRecyclerAdapter(Activity activity, Context context, ArrayList<Dialog> dialogs) {
        this.context = context;
        this.dialogs = dialogs;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialogs_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textViewUnread.setText(String.valueOf(dialogs.get(position).unread));

        ArrayList<Attachment> attachments = dialogs.get(position).getMessage().getAttachments();

        String attachmentsList = "";

        if(attachments != null)
            for(int i = 0; i < attachments.size(); i++) {
                attachmentsList += attachments.get(i).getAttachment() + "; ";
            }

        holder.textViewMessage.setText(dialogs.get(position).getMessage().body + attachmentsList);

        holder.frame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event, holder, position);
                return true;
            }
        });

        VKRequest.VKRequestListener userInfoListener = new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("myLogs", response.json.toString());
                data = gson.fromJson(response.json.toString(), Data.class);

                holder.textViewName.setText(data.getResponse().get(0).first_name + " " + data.getResponse().get(0).last_name);
                holder.imageURL = data.getResponse().get(0).photo_50;
                holder.userId = data.getResponse().get(0).id;

                new DrawableLoadAsyncTask().execute(holder);
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
        RelativeLayout relativeLayout;
        View frame;
        public Drawable drawable;
        String imageURL;
        int userId;

        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.dialog_frame);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.dialog_photo);
            textViewName = (TextView) itemView.findViewById(R.id.tv_dialog_username);
            textViewMessage = (TextView) itemView.findViewById(R.id.tv_dialog_message);
            textViewUnread = (TextView) itemView.findViewById(R.id.tv_unread_messages);
            frame = itemView.findViewById(R.id.dialog_frame_click);
        }
    }

    private void onTouchEvent(MotionEvent event, ViewHolder viewHolder, int i) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:case MotionEvent.ACTION_MOVE:
                viewHolder.frame.setBackgroundColor(Color.parseColor("#33bbdefb"));
                break;
            case MotionEvent.ACTION_UP:
                viewHolder.frame.setBackgroundColor(Color.parseColor("#00ffffff"));
                //onDrawerItemClickListener.onDrawerItemClick(i);
                Intent startDialogActivityIntent = new Intent(context, DialogActivity.class);
                startDialogActivityIntent.putExtra("user_id", viewHolder.userId);
                context.startActivity(startDialogActivityIntent);
                //activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_in);

                break;
            case MotionEvent.ACTION_SCROLL:
                viewHolder.frame.setBackgroundColor(Color.parseColor("#33bbdefb"));
            case MotionEvent.ACTION_CANCEL:
                viewHolder.frame.setBackgroundColor(Color.parseColor("#00ffffff"));
                break;
            default:
                break;
        }
    }

    public class DrawableLoadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {
        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            ViewHolder mViewHolder = params[0];

            try {
                InputStream is = (InputStream) new URL(mViewHolder.imageURL).getContent();
                mViewHolder.drawable = Drawable.createFromStream(is, "src_name");
            } catch (Exception e) {
                Log.e("myLogs", "Downloading image failed:");
                e.printStackTrace();
                mViewHolder.drawable = null;
            }
            return mViewHolder;
        }

        @Override
        protected void onPostExecute(ViewHolder viewHolder) {
            super.onPostExecute(viewHolder);
            if(viewHolder.drawable != null) {
                viewHolder.imageViewPhoto.setImageDrawable(viewHolder.drawable);
            }
        }
    }
}
