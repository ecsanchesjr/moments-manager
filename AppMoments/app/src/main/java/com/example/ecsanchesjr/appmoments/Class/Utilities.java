package com.example.ecsanchesjr.appmoments.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.ecsanchesjr.appmoments.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

    public static Bitmap generateResizedBitmap(Context context, Uri imgUri, int width, int height) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream inputStream = context.getContentResolver().openInputStream(imgUri);
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream = context.getContentResolver().openInputStream(imgUri);

            options.inSampleSize = getSample(options, width, height);

            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeStream(inputStream, null, options);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 60, stream);

            byte[] byteArray = stream.toByteArray();

            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getSample(BitmapFactory.Options options, int width, int height) {
        final int imgHeight = options.outHeight;
        final int imgWidth = options.outWidth;
        int inSampleSize = 1;

        if (imgHeight > height || imgWidth > width) {
            final int halfHeight = imgHeight / 2;
            final int halfWidth = imgWidth / 2;

            while ((halfHeight / inSampleSize) >= height && (halfWidth / inSampleSize) >= width) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
