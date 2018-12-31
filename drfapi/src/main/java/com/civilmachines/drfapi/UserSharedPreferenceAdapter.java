package com.civilmachines.drfapi;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * An adapter that handles a Shared Preference associated with a user.
 * This adapter creates and accesses a Shared Preference that contains only
 * user related data.
 *
 * Programmer can change Shared Preference filename or token keyword. To do same
 * add a similar code in main activity:
 * <pre>
 *     UserSharedPreferenceAdapter.userPrivate = "user";
 *     UserSharedPreferenceAdapter.keyToken = "userToken";
 * </pre>
 *
 * @author <a href="https://himanshus.com" target="_blank">Himanshu Shankar</a>
 * @author <a href="https://divyatiwari.me" target="_blank">Divya Tiwari</a>
 */
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
