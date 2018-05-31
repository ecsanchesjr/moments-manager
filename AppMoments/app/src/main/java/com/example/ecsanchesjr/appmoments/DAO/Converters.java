package com.example.ecsanchesjr.appmoments.DAO;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.example.ecsanchesjr.appmoments.Class.Utilities.dateToString;
import static com.example.ecsanchesjr.appmoments.Class.Utilities.stringToDate;

public class Converters {
    @TypeConverter
    public static ArrayList<String> fromStringsToPaths(String value) {
        String[] splited = value.split(",");
        return new ArrayList<>(Arrays.asList(splited));
    }

    @TypeConverter
    public static String fromArrayListPaths(ArrayList<String> paths) {
        String result = "";
        for (String s : paths) {
            result += s + ",";
        }
        return result.substring(0, result.length() - 1);
    }

    @TypeConverter
    public static Date fromStringToDate(String date) {
        try {
            return stringToDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    @TypeConverter
    public static String fromDateToString(Date date) {
        return dateToString(date);
    }
}
