package com.tixon.gentlevk.newsfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tixon.gentlevk.messages.MessagesRecyclerAdapter;

/**
 * Created by Tixon
 */
public class NewsRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder> {

    private Context context;

    public NewsRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MessagesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MessagesRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
