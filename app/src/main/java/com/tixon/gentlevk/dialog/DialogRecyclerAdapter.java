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
import com.tixon.gentlevk.attachments.Audio;
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup insertPoint = (ViewGroup) holder.cardRootLayout.findViewById(R.id.message_card_insert_point);

        if(attachments != null) {
            for(int i = 0; i < attachments.size(); i++) {
                final int attachmentPosition = i;
                if(attachments.get(i).photo != null) {
                    holder.imageURL = attachments.get(i).photo.photo_807;
                    new DrawableLoadAsyncTask().execute(holder);
                    //break;
                }

                if(attachments.get(i).audio != null) {
                    View v = inflater.inflate(R.layout.audio_include, null);

                    final Audio mAudio = attachments.get(i).audio;

                    ImageView playButton = (ImageView) v.findViewById(R.id.audio_include_image);
                    TextView tvAudioName = (TextView) v.findViewById(R.id.audio_include_name);
                    TextView tvAudioLength = (TextView) v.findViewById(R.id.audio_include_length);
                    tvAudioName.setText(attachments.get(i).audio.title);
                    tvAudioLength.setText(Attachment.getTime(attachments.get(i).audio.duration));

                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("myLogs", "play button clicked, i = " + attachmentPosition + ", title = " + mAudio.title + ", url = " + mAudio.url);
                        }
                    });

                    //insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    //break;
                }

                if(attachments.get(i).doc != null) {
                    View doc = inflater.inflate(R.layout.document_include, null);

                    TextView tvDocName = (TextView) doc.findViewById(R.id.document_include_name);
                    TextView tvDocSize = (TextView) doc.findViewById(R.id.document_include_size);
                    tvDocName.setText(attachments.get(i).doc.title);
                    tvDocSize.setText(String.valueOf(attachments.get(i).doc.size));

                    insertPoint.addView(doc, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    //break;
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
            imageView = (ImageView) cardView.findViewById(R.id.message_image_view);
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
