/*
 * Copyright (C) 2018 Himanshu Shankar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.civilmachines.drfapi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * An adapter to handle Shared Preference in a uniform manner
 *
 * @author <a href="https://himanshus.com" target="_blank">Himanshu Shankar</a>
 * @author <a href="https://divyatiwari.me" target="_blank">Divya Tiwari</a>
 */
public class SharedPreferenceAdapter {

    public SharedPreferences main;
    public SharedPreferences.Editor edit;

    /**
     * This function initializes the shared preference with Activity
     * @param act is the current Activity
     */
    SharedPreferenceAdapter(Activity act){
        main = PreferenceManager.getDefaultSharedPreferences(act);
    }

    public SharedPreferenceAdapter(Context cont){
        main = PreferenceManager.getDefaultSharedPreferences(cont);
    }

    public SharedPreferenceAdapter(Activity act, String FileName){
        main = act.getSharedPreferences(FileName, Activity.MODE_PRIVATE);
    }

    public SharedPreferenceAdapter(Context cont, String AdapterName){
        main = cont.getSharedPreferences(AdapterName, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getEditor() {
        return edit;
    }

    public SharedPreferences sharedPreference() {
        return main;
    }

    public int getInt(String key){
        return main.getInt(key, 0);
    }

    public String getString(String key){
        return main.getString(key, null);
    }

    public boolean getBoolean(String key){
        return main.getBoolean(key, false);
    }

    public Set<String> getStringSet(String key){
        return main.getStringSet(key, null);
    }

    public float getFloat(String key){
        return main.getFloat(key, 0);
    }

    public long getLong(String key){
        return main.getLong(key, 0);
    }

    public boolean saveData(String key, String val){
        edit = main.edit();
        edit.putString(key, val);
        return edit.commit();
    }

    public boolean saveData(String key, int val){
        edit = main.edit();
        edit.putInt(key, val);
        return edit.commit();
    }

    public boolean saveData(String key, long val){
        edit = main.edit();
        edit.putLong(key, val);
        return edit.commit();
    }

    public boolean saveData(String key, boolean val){
        edit = main.edit();
        edit.putBoolean(key, val);
        return edit.commit();
    }

    public boolean saveData(String key, Float val){
        edit = main.edit();
        edit.putFloat(key, val);
        return edit.commit();
    }

    public boolean saveData(String key, Set<String> val){
        edit = main.edit();
        edit.putStringSet(key, val);
        return edit.commit();
    }

    public JSONObject getProperty() throws JSONException {
        JSONObject params = new JSONObject();
        Map<String, ?> allEntries = main.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }
        return params;
    }

    public boolean clearData(){
        edit = main.edit();
        edit.clear();
        return edit.commit();
    }

    public boolean remove(String KEY){
        edit = main.edit();
        edit.remove(KEY);
        return edit.commit();
    }
}
