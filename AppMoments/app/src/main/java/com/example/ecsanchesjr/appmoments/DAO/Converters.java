package com.example.ecsanchesjr.appmoments.DAO;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static com.example.ecsanchesjr.appmoments.Class.Utilities.dateToString;
import static com.example.ecsanchesjr.appmoments.Class.Utilities.stringToDate;

public class Converters {
    @TypeConverter
    public static ArrayList<String> fromStringToUris(String value) {
        ArrayList<String> uris = new ArrayList<>();
        if (!value.isEmpty()) {
            String[] splitedUris = value.split(",");

            for (String uri : splitedUris) {
                uris.add(uri);
            }
        }
        return uris;
    }

    @TypeConverter
    public static String fromUrisToString(ArrayList<String> uris) {
        String result = "";
        for (String uri : uris) {
            result += uri + ",";
        }
        return result;
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return (date==null?null:date.getTime());
    }

    @TypeConverter
    public static Date timestampToDate(Long date) {
        return (date==null?null:new Date(date));
    }
}
