package com.tixon.gentlevk.profile;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tixon.gentlevk.R;

/**
 * Created by Tixon
 */
public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;

    RecyclerView recyclerView;
    ProfileRecyclerAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_layout);

        toolbar = (Toolbar) findViewById(R.id.profile_anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.profile_collapsing_toolbar_layout);
        toolbarLayout.setTitle("This is title");

        recyclerView = (RecyclerView) findViewById(R.id.profile_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ProfileRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
