package com.tixon.gentlevk;

import android.content.Context;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Tixon
 */
public class DrawableLoader extends Loader<Drawable> {
    public String URL;
    public final static String KEY_URL = "key_url";
    DrawableAsyncGetter drawableGetter;

    public DrawableLoader(Context context, Bundle args) {
        super(context);
        if(args != null) URL = args.getString(KEY_URL);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if(drawableGetter != null)
            drawableGetter.cancel(true);
        drawableGetter = new DrawableAsyncGetter();
        drawableGetter.execute(URL);
    }

    class DrawableAsyncGetter extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {
            return loadImageFromUrl(params[0]);
        }
    }

    public Drawable loadImageFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            Log.d("myLogs", e.toString());
            return null;
        }
    }
}
