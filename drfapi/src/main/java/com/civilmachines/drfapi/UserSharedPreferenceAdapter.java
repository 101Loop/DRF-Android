package com.civilmachines.drfapi;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

public class UserSharedPreferenceAdapter extends SharedPreferenceAdapter {
    public static String userPrivate = "user_private";
    public static String keyToken = "token";

    public UserSharedPreferenceAdapter(Context cont) {
        super(cont, userPrivate);
    }

    public UserSharedPreferenceAdapter(Activity act) {
        super(act, userPrivate);
    }

    public boolean isLoggedIn() {
        return main.contains(keyToken) && super.getString(keyToken).length() > 10;
    }

    public String getToken() {
        return getString(keyToken);
    }

    public boolean saveToken(@NonNull String token) {
        return saveData(keyToken, token);
    }

    public void logOut() {
        super.clearData();
    }
}
