package com.example.ecsanchesjr.appmoments.Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.ecsanchesjr.appmoments.Activities.MomentsListActivity;
import com.example.ecsanchesjr.appmoments.DAO.MomentDatabase;
import com.example.ecsanchesjr.appmoments.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utilities {

    public final static String SHARED_PREFERECES_FILE = "com.example.ecsanchesjr.appmoments.shred_preferences_file";
    public final static String KEY_THEME = "APP_THEME";

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(date);
    }

    public static int readSharedPreferences(Context context) {
        SharedPreferences shared = context.getSharedPreferences(SHARED_PREFERECES_FILE, context.MODE_PRIVATE);
        int savedTheme = shared.getInt(KEY_THEME, R.style.AppTheme);
        context.setTheme(savedTheme);
        return savedTheme;
    }
}
