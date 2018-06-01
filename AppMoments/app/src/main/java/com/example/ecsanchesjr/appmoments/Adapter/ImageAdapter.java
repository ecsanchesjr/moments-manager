package com.example.ecsanchesjr.appmoments.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Uri> imgsUri;
    private static LayoutInflater inflater = null;

    public ImageAdapter(Context context, ArrayList<Uri> imgsUri) {
        this.context = context;
        this.imgsUri = imgsUri;
    }

    @Override
    public int getCount() { return imgsUri.size(); }

    @Override
    public Object getItem(int position) { return null; }

    @Override
    public long getItemId(int position) { return 0; }

    // create a new ImageView for each item in ArrayList<Uri>
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgView;

        if(convertView == null) {
            // if not recycled
            imgView = new ImageView(context);
            imgView.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgView.setPadding(5, 5, 5, 5);
        } else {
            imgView = (ImageView) convertView;
        }

        imgView.setImageURI(imgsUri.get(position));
        return imgView;
    }
}
