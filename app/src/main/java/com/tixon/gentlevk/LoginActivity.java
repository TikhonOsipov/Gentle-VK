package com.tixon.gentlevk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class LoginActivity extends AppCompatActivity {

    private boolean isResumed = false;

    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {
                if (isResumed) {
                    switch (res) {
                        case LoggedOut:
                            showLogin();
                            break;
                        case LoggedIn:
                            showLogout();
                            break;
                        case Pending:
                            break;
                        case Unknown:
                            break;
                    }
                }
            }

            @Override
            public void onError(VKError error) {

            }
        });
        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //Log.d("myLogs", "fingerprint = " + fingerprints[0]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (VKSdk.isLoggedIn()) {
            showLogout();
        } else {
            showLogin();
        }

    }

    @Override
    protected void onPause() {
        isResumed = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showLogout() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LogoutFragment())
                .commit();
    }

    private void showLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // User passed Authorization
                startMainActivity();
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback) ) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public static class LoginFragment extends android.support.v4.app.Fragment {
        public LoginFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_login, container, false);
            v.findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.login(getActivity(), sMyScope);
                }
            });
            return v;
        }

    }

    public static class LogoutFragment extends android.support.v4.app.Fragment {
        public LogoutFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_logout, container, false);
            v.findViewById(R.id.button_continue).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((LoginActivity) getActivity()).startMainActivity();
                }
            });

            v.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.logout();
                    if (!VKSdk.isLoggedIn()) {
                        ((LoginActivity) getActivity()).showLogin();
                    }
                }
            });
            return v;
        }
    }
}
