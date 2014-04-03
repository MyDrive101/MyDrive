package com.example.MyDrive;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SaveSharedPreference 
{
    static final String PREF_USER_NAME= "username";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx,boolean userName) 
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static boolean getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "") != null;
    }
}