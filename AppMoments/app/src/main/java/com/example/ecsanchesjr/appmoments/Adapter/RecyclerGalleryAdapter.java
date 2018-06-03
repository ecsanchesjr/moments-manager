package com.example.ecsanchesjr.appmoments.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ecsanchesjr.appmoments.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerGalleryAdapter extends Adapter<RecyclerGalleryAdapter.PhotoHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<Uri> imagesUri;
    private ArrayList<Integer> imagesSelected;

    public RecyclerGalleryAdapter(Context context, ArrayList<Uri> imagesUri) {
        mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.imagesUri = imagesUri;
        this.context = context;
        this.imagesSelected = new ArrayList<>();
    }

    // Inflates the cell layout from xml when needed
    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gallery_photo, parent, false);
        PhotoHolder photoHolder = new PhotoHolder(view);
        return photoHolder;
    }

    // Binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        holder.photo.setImageURI(imagesUri.get(position));

        //holder.layout.setBackgroundColor(isChecked(position)? Color.LTGRAY : Color.WHITE);

        holder.photo.setOnClickListener(v -> {
            toggleImageSelected(position);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return imagesUri.size();
    }

    public void addImg(Uri imgUri) {
        imagesUri.add(imgUri);
    }

    public void addImgs(ArrayList<Uri> imagesUri) {
        this.imagesUri.addAll(imagesUri);
    }

    public ArrayList<Uri> getImagesUri() {
        return imagesUri;
    }

    public void setImagesUri(ArrayList<Uri> imagesUri) {
        this.imagesUri = imagesUri;
    }

    public Uri getImageUri(int position) {
        return imagesUri.get(position);
    }

    public void toggleImageSelected(int position) {
        if(imagesSelected.contains(new Integer(position))) {
            imagesSelected.remove(new Integer(position));
        } else {
            imagesSelected.add(new Integer(position));
        }
    }

    public boolean isChecked(int position) {
        System.out.println("IS CHECKED");
        System.out.println(position);
        return (imagesSelected.contains(new Integer(position)));
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {

        public ImageView photo;
        public View layout;

        public PhotoHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photoImageView);
            layout = itemView.findViewById(R.id.photoLayout);
        }
    }

}
