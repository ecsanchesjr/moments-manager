package com.example.ecsanchesjr.appmoments.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

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

    public static ArrayList<Uri> getMomentsUri(ArrayList<String> stringUris) {
        ArrayList<Uri> uris = new ArrayList<>();
        for (String uri : stringUris) {
            uris.add(Uri.parse(uri));
        }
        return uris;
    }

    public static ArrayList<String> getStringsUri(ArrayList<Uri> uris) {
        ArrayList<String> stringUris = new ArrayList<>();
        for (Uri uri : uris) {
            stringUris.add(uri.toString());
        }
        return stringUris;
    }
}
