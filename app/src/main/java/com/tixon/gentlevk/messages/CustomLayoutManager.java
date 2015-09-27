package com.tixon.gentlevk.messages;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by Tixon
 */
public class CustomLayoutManager extends LinearLayoutManager {
    public CustomLayoutManager(Context context) {
        super(context);
    }

    private OnOverScrollListener onOverScrollListener;

    public void setOnOverScrollListener(OnOverScrollListener listener) {
        this.onOverScrollListener = listener;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        int scrollRange = super.scrollVerticallyBy(dy, recycler, state);
        int overscroll = dy - scrollRange;
        if (overscroll > 0) {
            // bottom overscroll
            Log.d("myLogs", "bottomOverscroll");
        } else if (overscroll < 0) {
            // top overscroll
            Log.d("myLogs", "topOverscroll");
            onOverScrollListener.onOverScroll(overscroll);
        }
        return scrollRange;
    }
}
