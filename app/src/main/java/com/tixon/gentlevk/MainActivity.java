package com.tixon.gentlevk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;
import com.tixon.gentlevk.messages.MessagesFragment;
import com.tixon.gentlevk.navigation.DrawerAdapter;
import com.tixon.gentlevk.navigation.OnDrawerItemClickListener;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    DrawerAdapter drawerAdapter;
    RecyclerView drawerRecyclerView;

    Gson gson = new Gson();
    Data data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKRequest.VKRequestListener profileListener = new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                data = gson.fromJson(response.json.toString(), Data.class);
                Log.d("myLogs", "response = " + response.json.toString());

                drawerAdapter =
                        new DrawerAdapter(getResources().getStringArray(R.array.views_array), data != null ? data.getResponse().get(0).first_name + " " + data.getResponse().get(0).last_name : "",
                        data != null ? data.getResponse().get(0).id : 0);
                drawerRecyclerView.setAdapter(drawerAdapter);
                drawerRecyclerView.setHasFixedSize(true);
                drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                drawerAdapter.setOnDrawerItemClickListener(new OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        selectView(position);
                        Log.d("myLogs", "position clicked = " + position);
                    }
                });
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

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerRecyclerView = (RecyclerView) findViewById(R.id.drawer_recyclerView);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        VKRequest requestProfile = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                "id,first_name,last_name,sex,bdate,city,country,photo_50,photo_100," +
                        "photo_200_orig,photo_200,photo_400_orig,photo_max,photo_max_orig,online," +
                        "online_mobile,lists,domain,has_mobile,contacts,connections,site,education," +
                        "universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message," +
                        "status,last_seen,common_count,relation,relatives,counters"));

        requestProfile.setRequestListener(profileListener);
        requestProfile.executeWithListener(profileListener);
    }

    public void selectView(int position) {
        Fragment fragment = null;

        switch(position) {
            case 2:
                fragment = MessagesFragment.newInstance();
                break;
            default: break;
        }

        if(fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, fragment).commit();
            drawerLayout.closeDrawers();
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
