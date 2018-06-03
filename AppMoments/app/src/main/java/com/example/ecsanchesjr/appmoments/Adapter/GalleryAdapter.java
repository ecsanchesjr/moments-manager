package com.example.ecsanchesjr.appmoments.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ecsanchesjr.appmoments.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GalleryAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private ArrayList<Uri> imgsUri;
    private ArrayList<Integer> imagesChecked;

    public GalleryAdapter(Context context, ArrayList<Uri> imgsUri) {
        this.context = context;
        this.imgsUri = imgsUri;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.imagesChecked = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return imgsUri.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addImgs(ArrayList<Uri> imgs) {
        imgsUri.addAll(imgs);
    }

    public void addImg(Uri imgUri) {
        imgsUri.add(imgUri);
    }

    public Uri getImageUri(int position) {
        return imgsUri.get(position);
    }

    public void toggleChecked(int position) {
        if (imagesChecked.contains(new Integer(position))) {
            imagesChecked.remove(new Integer(position));
        } else {
            imagesChecked.add(position);
        }
    }

    public void clearImagesChecked() {
        imagesChecked.clear();
    }

    public ArrayList<Integer> getImagesChecked() {
        return imagesChecked;
    }

    public void removeImagesChecked(ArrayList<Integer> checkedToRemove) {
        checkedToRemove.sort((o1, o2) -> o1 - o2);
        Collections.reverse(checkedToRemove);
        checkedToRemove.forEach(el -> {
            imgsUri.remove(imgsUri.get(el));
        });
        imagesChecked.clear();
    }

    public ArrayList<Uri> getImgsUri() {
        return imgsUri;
    }

    // create a new ImageView for each item in ArrayList<Uri>
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImgHolder imgHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gallery_photo, null);
        }

        imgHolder = new ImgHolder();
        imgHolder.img = convertView.findViewById(R.id.photoImageView);
        imgHolder.view = convertView.findViewById(R.id.backgroundLayout);

        if (imagesChecked.contains(position)) {
            imgHolder.img.setPadding(10, 10, 10, 10);
        } else {
            imgHolder.img.setPadding(0, 0, 0, 0);
        }

        try {
            Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgsUri.get(position));
            imgBitmap = Bitmap.createScaledBitmap(imgBitmap, 350, 350, true);

            imgHolder.img.setImageBitmap(imgBitmap);
        } catch (IOException e) {
            System.out.println("nothing happen");
        }

        return convertView;
    }

    private class ImgHolder {
        ImageView img;
        View view;
    }
//
//
//        if(convertView == null) {
//            // if not recycled
//            imgView = new ImageView(context);
//            imgView.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
//            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imgView.setPadding(5, 5, 5, 5);
//        } else {
//            imgView = (ImageView) convertView;
//        }
//
//        imgView.setImageURI(imgsUri.get(position));
//        return imgView;
//    }
}
