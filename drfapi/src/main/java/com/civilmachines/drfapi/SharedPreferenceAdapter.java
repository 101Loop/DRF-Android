package com.civilmachines.drfapi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SharedPreferenceAdapter {

    static private SharedPreferences main;
    static private SharedPreferences.Editor edit;

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
        main =  act.getSharedPreferences(FileName, Activity.MODE_PRIVATE);
    }

    public SharedPreferenceAdapter(Context cont, String AdapterName){
        main =  cont.getSharedPreferences(AdapterName, Context.MODE_PRIVATE);
    }

   /* public SharedPreferenceAdapter(BusinessRecyclerView_Adapter businessRecyclerView_adapter) {
        main = businessRecyclerView_adapter.onClick(View v);
    }
*/

    public boolean onLoggedIn(String val){
        edit = main.edit();
        edit.putString("login", val);
        return edit.commit();
    }

    public int getInt(String key){
        return main.getInt(key, 0);
    }

    public String getString(String key){
        return main.getString(key, null);
    }

    public String isLoggedIn(){
        return main.getString("token", null);
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
