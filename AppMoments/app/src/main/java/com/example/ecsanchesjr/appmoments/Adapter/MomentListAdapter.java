package com.example.ecsanchesjr.appmoments.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecsanchesjr.appmoments.Activities.MomentsListActivity;
import com.example.ecsanchesjr.appmoments.Class.Moment;
import com.example.ecsanchesjr.appmoments.Class.Utilities;
import com.example.ecsanchesjr.appmoments.R;

import java.util.ArrayList;
import java.util.Random;

import static com.example.ecsanchesjr.appmoments.Class.Utilities.dateToString;

public class MomentListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private ArrayList<Moment> moments;
    private ArrayList<Integer> momentsChecked;
    private boolean nightMode;
    private LruCache<String, Bitmap> mMemoryCache;

    // Adapter to list View
    public MomentListAdapter(Context context, ArrayList<Moment> moments, boolean nightMode) {
        this.context = context;
        this.moments = moments;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        momentsChecked = new ArrayList<>();
        this.nightMode = nightMode;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 2;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public Moment getMoment(int index) {
        return moments.get(index);
    }

    public ArrayList<Moment> getMoments() {
        return moments;
    }

    public void toggleChecked(int position) {
        if (momentsChecked.contains(new Integer(position))) {
            momentsChecked.remove(new Integer(position));
        } else {
            momentsChecked.add(position);
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

    public void clearMomentsChecked() {
        momentsChecked.clear();
    }

    public ArrayList<Integer> getMomentsChecked() {
        return momentsChecked;
    }

    @Override
    public int getCount() {
        return moments.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Inflate the data of XML moment Style and load informations to each card
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.moment_style, null);
        }
        MomentHolder moment = new MomentHolder();

        moment.momentImage = convertView.findViewById(R.id.momentImage);
        moment.momentTitle = convertView.findViewById(R.id.momentTitle);
        moment.momentLocal = convertView.findViewById(R.id.momentLocal);
        moment.momentDate = convertView.findViewById(R.id.momentDate);
        moment.momentView = convertView.findViewById(R.id.card_view);

        if (momentsChecked.contains(position)) {
            int color = ContextCompat.getColor(context,
                    nightMode ? R.color.darkCardBackgroundSelected : R.color.defaultCardBackgroundSelected);

            moment.momentView.setBackgroundColor(color);
        } else {
            int color = ContextCompat.getColor(context,
                    nightMode ? R.color.darkCardBackground : R.color.defaultCardBackground);

            moment.momentView.setBackgroundColor(color);
        }

        Moment current = moments.get(position);

        moment.momentTitle.setText(current.getName());
        moment.momentLocal.setText(current.getLocal());
        moment.momentDate.setText(dateToString(current.getDate()));
        if (current.getMainImgUri() != null) {
            //moment.momentImage.setImageURI(Uri.parse(moments.get(position).getMainImgUri()));
            loadBitmap(current.getId(), moment.momentImage, Uri.parse(current.getMainImgUri()));
            moment.momentImage.setVisibility(View.VISIBLE);
        } else {
            moment.momentImage.setVisibility(View.GONE);
            moment.momentImage.setImageDrawable(null);
        }

        return convertView;
    }

    // Class holder to show data
    private class MomentHolder {
        ImageView momentImage;
        TextView momentTitle;
        TextView momentLocal;
        TextView momentDate;
        View momentView;
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
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.ic_launcher_background);
            MomentListAdapter.BitmapWorkerTask task = new MomentListAdapter.BitmapWorkerTask(imageView, context, imgUri);
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
            Bitmap bitmap = Utilities.generateResizedBitmap(ctx, imageUri, 400, 400);
            if (bitmap != null) {
                addBitmapToMemoryCache(String.valueOf(integers[0]), bitmap);
            }
            this.bitmap = bitmap;
            return null;
        }

    }
}
