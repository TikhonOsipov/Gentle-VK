package com.tixon.gentlevk;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Tixon
 */
public class BaseActivity extends AppCompatActivity {
    public int getStatusBarHeight() {
        int result = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }
}
