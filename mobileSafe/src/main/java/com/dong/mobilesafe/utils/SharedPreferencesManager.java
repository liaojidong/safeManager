/**
 *
 */
package com.dong.mobilesafe.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.dong.mobilesafe.application.SecurityApplication;

import java.util.Set;

/**
 * SharedPreferences存储管理器
 *
 * @author chends
 */
public class SharedPreferencesManager {

    private SharedPreferences sp;
    private static SharedPreferencesManager instance;


    public static SharedPreferencesManager getInstance() {
        if (instance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (instance == null) {
                    instance = new SharedPreferencesManager();
                }
            }
        }
        return instance;
    }

    private SharedPreferencesManager() {
        // 获取sp管理器
        sp = PreferenceManager.getDefaultSharedPreferences(SecurityApplication.getInstance());
    }

    public void putString(String key, String value) {
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void putStringSet(String key, Set<String> value) {
        Editor editor = sp.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }


    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return sp.getStringSet(key, defValue);
    }

    public void commit() {
        sp.edit().commit();
    }

}
