package com.tixon.gentlevk.newsfeed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tixon.gentlevk.R;

/**
 * Created by Tixon
 */
public class NewsFragment extends Fragment {

    RecyclerView newsRecyclerVew;
    NewsRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_fragment_layout, container, false);
        newsRecyclerVew = (RecyclerView) v.findViewById(R.id.news_recycler_view);
        return v;
    }
}
