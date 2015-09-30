package com.tixon.gentlevk.navigation;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tixon.gentlevk.R;
import com.tixon.gentlevk.Response;
import com.tixon.gentlevk.views.CircleImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Tixon
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static final int HEADER_TYPE = 0;
    private static final int ROW_TYPE = 1;

    private String[] rows;
    private Response response;

    private OnDrawerItemClickListener listener;

    public void setOnDrawerItemClickListener(OnDrawerItemClickListener listener) {
        this.listener = listener;
    }

    public DrawerAdapter(String[] rows, Response response) {
        this.rows = rows;
        this.response = response;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == HEADER_TYPE) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_header, viewGroup, false);
            return new ViewHolder(view, i);
        } else if(i == ROW_TYPE) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_row, viewGroup, false);
            return new ViewHolder(view, i);
        }
        return null;
    }

    private void onTouchEvent(MotionEvent event, ViewHolder viewHolder, int i) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:case MotionEvent.ACTION_MOVE:
                viewHolder.frame.setBackgroundColor(Color.parseColor("#cceeeeee"));
                break;
            case MotionEvent.ACTION_UP:case MotionEvent.ACTION_CANCEL:
                viewHolder.frame.setBackgroundColor(Color.parseColor("#00ffffff"));
                listener.onItemClick(i);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if(viewHolder.viewType == ROW_TYPE) {
            String rowText = rows[i-1];
            viewHolder.text.setText(rowText);

            viewHolder.icon.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchEvent(event, viewHolder, i);
                    return true;
                }
            });
            viewHolder.text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchEvent(event, viewHolder, i);
                    return true;
                }
            });
            viewHolder.frame.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchEvent(event, viewHolder, i);
                    return true;
                }
            });
        }
        if(viewHolder.viewType == HEADER_TYPE) {
            viewHolder.tv_name.setText(response.first_name + " " + response.last_name);
            viewHolder.tv_id.setText(String.valueOf(response.id));
            viewHolder.profilePhotoURL = response.photo_100;
            new DrawableLoadAsyncTask().execute(viewHolder);

        }
    }

    @Override
    public int getItemCount() {
        return rows.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return HEADER_TYPE;
        }
        return ROW_TYPE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected int viewType;
        RelativeLayout frame;
        ImageView icon;
        CircleImageView profilePhoto;
        TextView text;

        TextView tv_name, tv_id; //test

        String profilePhotoURL;
        Drawable profilePhotoDrawable;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            if(viewType == HEADER_TYPE) {
                profilePhoto = (CircleImageView) itemView.findViewById(R.id.profile_photo);
                tv_name = (TextView) itemView.findViewById(R.id.person_name);
                tv_id = (TextView) itemView.findViewById(R.id.person_id);
            }

            if(viewType == ROW_TYPE) {
                frame = (RelativeLayout) itemView.findViewById(R.id.drawer_row_frame);
                icon = (ImageView) itemView.findViewById(R.id.drawer_row_image_view);
                text = (TextView) itemView.findViewById(R.id.drawer_row_text_view);
            }
        }
    }

    public class DrawableLoadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {
        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            ViewHolder mViewHolder = params[0];

            try {
                InputStream is = (InputStream) new URL(mViewHolder.profilePhotoURL).getContent();
                mViewHolder.profilePhotoDrawable = Drawable.createFromStream(is, "src_name");
            } catch (Exception e) {
                Log.e("myLogs", "Downloading image failed:");
                e.printStackTrace();
                mViewHolder.profilePhotoDrawable = null;
            }
            return mViewHolder;
        }

        @Override
        protected void onPostExecute(ViewHolder viewHolder) {
            super.onPostExecute(viewHolder);
            if(viewHolder.profilePhotoDrawable != null) {
                viewHolder.profilePhoto.setImageDrawable(viewHolder.profilePhotoDrawable);
            }
        }
    }
}
