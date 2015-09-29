package com.tixon.gentlevk.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tixon.gentlevk.R;
import com.tixon.gentlevk.attachments.Attachment;
import com.tixon.gentlevk.messages.Message;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tixon
 */
public class DialogRecyclerAdapter extends RecyclerView.Adapter<DialogRecyclerAdapter.ViewHolder> {

    ArrayList<Message> messages;
    Context context;

    public DialogRecyclerAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public DialogRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_card_layout, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(DialogRecyclerAdapter.ViewHolder holder, int position) {
        ArrayList<Attachment> attachments = messages.get(position).getAttachments();
        holder.textViewMessage.setText(messages.get(position).body);

        if(attachments != null) {
            for(int i = 0; i < attachments.size(); i++) {
                if(attachments.get(i).photo != null) {
                    holder.imageURL = attachments.get(i).photo.photo_807;
                    new DrawableLoadAsyncTask().execute(holder);
                    break;
                }
                if(attachments.get(i).audio != null) {
                    //holder.cardRootLayout.addView
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout cardRootLayout;
        TextView textViewMessage;
        ImageView imageView;

        Drawable drawable;
        String imageURL;

        public ViewHolder(CardView cardView) {
            super(cardView);
            cardRootLayout = (RelativeLayout) cardView.findViewById(R.id.message_card_relative_layout);
            textViewMessage = (TextView) cardView.findViewById(R.id.message_body_text_view);
            imageView = (ImageView) cardView.findViewById(R.id.messages_image_view);
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
                viewHolder.imageView.setImageDrawable(viewHolder.drawable);
            }
        }
    }
}
