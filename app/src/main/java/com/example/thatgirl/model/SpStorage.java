package com.example.thatgirl.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SpStorage{
    private SharedPreferences mSp;
    private Context mContext;
    private static SpStorage mSpStorage = null;
    private SharedPreferences.Editor mEditor;
    private static final String SP_NAME = "music_preference";
    public static synchronized void init(Context context) {
        if (mSpStorage == null) {
            mSpStorage = new SpStorage(context);
        }
    }
    public static SpStorage getInstance() {
        return mSpStorage;
    }
    public SpStorage(Context context) {
        mContext = context;
        mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mEditor = mSp.edit();
    }

    public void set(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void set(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }
    public boolean get(String key, boolean defaultValue) {
        return mSp.getBoolean(key, defaultValue);
    }

    private static final String SP_KEY_DEFAULT_COVER_PATH = "default_cover_path";
    public String getPhotoPath() {
        return mSp.getString(SP_KEY_DEFAULT_COVER_PATH, null);
    }

    public void setPhotoPath(String path) {
        set(SP_KEY_DEFAULT_COVER_PATH, path);
    }

    private static final String SP_KEY_CHARGESET_LIGHT_OPEN="sp_key_chargeset_light_open";

    public boolean isChargesetLightOpen() {
        return mSp.getBoolean(SP_KEY_CHARGESET_LIGHT_OPEN,true);
    }

    public void setSpKeyChargesetLightOpen(boolean value){
        set(SP_KEY_CHARGESET_LIGHT_OPEN,value);
    }
}
