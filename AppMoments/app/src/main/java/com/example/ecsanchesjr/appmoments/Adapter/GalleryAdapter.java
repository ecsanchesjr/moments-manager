package com.example.ecsanchesjr.appmoments.Adapter;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ecsanchesjr.appmoments.Class.Moment;
import com.example.ecsanchesjr.appmoments.Class.Utilities;
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
    private int mainImgPosition;
    private ArrayList<Integer> imagesChecked;
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean nightMode;

    public GalleryAdapter(Context context, ArrayList<Uri> imgsUri, boolean nightMode) {
        this.context = context;
        this.imgsUri = imgsUri;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.imagesChecked = new ArrayList<>();
        this.mainImgPosition = -1;
        this.nightMode = nightMode;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 6;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
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
        if(imgsUri.indexOf(imgUri) == -1) {
            imgsUri.add(imgUri);
        }
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

    private void setBitmap(Bitmap b, ImageView iv) {
        if (iv == null) {
            iv.setVisibility(View.GONE);
            iv.setImageDrawable(null);
        } else {
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageBitmap(b);
        }
    }

    public int getMainImgPosition() {
        return mainImgPosition;
    }

    public void setMainImgPosition(Uri mainUri) {
        this.mainImgPosition = imgsUri.indexOf(mainUri);
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
            if(el.equals(imgsUri.indexOf(el))) {
                mainImgPosition = -1;
            }
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

        if(position == mainImgPosition) {
            imgHolder.view.setPadding(10, 10, 10, 10);
            imgHolder.view.setElevation(1);
            int borderColor = ContextCompat.getColor(context,
                    (nightMode?R.color.darkColorAccent:R.color.colorAccent));
            imgHolder.view.setBackgroundColor(borderColor);
        } else {
            imgHolder.view.setPadding(0, 0, 0, 0);
            imgHolder.view.setElevation(0);
            imgHolder.view.setBackgroundColor(Color.TRANSPARENT);
        }

        if (imagesChecked.contains(position)) {
            //imgHolder.img.setPadding(100, 100, 100, 100);
            imgHolder.img.setColorFilter(Color.argb(50, 0, 0, 0));
        } else {
            //imgHolder.img.setPadding(0, 0, 0, 0);
            imgHolder.img.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        loadBitmap(position, imgHolder.img, imgsUri.get(position));

        return convertView;
    }

    private class ImgHolder {
        ImageView img;
        View view;
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public void loadBitmap(int resId, ImageView imageView, Uri imgUri) {
        final String imageKey = String.valueOf(resId);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            int backgroundLoad = nightMode?R.color.darkPhotoLoadingBackground:R.color.cardview_shadow_start_color;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(backgroundLoad);
            BitmapWorkerTask task = new BitmapWorkerTask(imageView, context, imgUri);
            task.execute(resId);
        }
    }

    private class BitmapWorkerTask extends AsyncTask<Integer, Void, Void> {
        private ImageView imageV;
        private Context ctx;
        private Uri imageUri;
        private Bitmap bitmap;

        public BitmapWorkerTask(ImageView imageV, Context ctx, Uri imageUri) {
            this.imageV = imageV;
            this.ctx = ctx;
            this.imageUri = imageUri;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setBitmap(bitmap, imageV);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            Bitmap bitmap = Utilities.generateResizedBitmap(ctx, imageUri, 350, 350);
            if (bitmap != null) {
                addBitmapToMemoryCache(String.valueOf(integers[0]), bitmap);
            }
            this.bitmap = bitmap;
            return null;
        }

    }
}
