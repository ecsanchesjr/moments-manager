package com.example.ecsanchesjr.appmoments.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecsanchesjr.appmoments.Class.Moment;
import com.example.ecsanchesjr.appmoments.R;

import java.util.ArrayList;

import static com.example.ecsanchesjr.appmoments.Class.Utilities.dateToString;

public class MomentListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Moment> moments;
    private static LayoutInflater inflater = null;
    private ArrayList<Integer> momentsChecked;
    private boolean nightMode;

    // Adapter to list View
    public MomentListAdapter(Context context, ArrayList<Moment> moments, boolean nightMode) {
        this.context = context;
        this.moments = moments;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        momentsChecked = new ArrayList<>();
        this.nightMode = nightMode;
    }

    public Moment getMoment(int index) {
            return moments.get(index);
    }

    public void toggleChecked(int position) {
        if(momentsChecked.contains(new Integer(position))) {
            momentsChecked.remove(new Integer(position));
        } else {
            momentsChecked.add(position);
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

    // Class holder to show data
    private class MomentHolder {
        ImageView momentImage;
        TextView momentTitle;
        TextView momentLocal;
        TextView momentDate;
        View momentView;
    }

    // Inflate the data of XML moment Style and load informations to each card
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.moment_style, null);
        }
        MomentHolder moment = new MomentHolder();

        moment.momentImage = convertView.findViewById(R.id.momentImage);
        moment.momentTitle = convertView.findViewById(R.id.momentTitle);
        moment.momentLocal = convertView.findViewById(R.id.momentLocal);
        moment.momentDate = convertView.findViewById(R.id.momentDate);
        moment.momentView = convertView.findViewById(R.id.card_view);

        if(momentsChecked.contains(position)) {
            int color = ContextCompat.getColor(context,
                    nightMode? R.color.darkCardBackgroundSelected:R.color.defaultCardBackgroundSelected);

            moment.momentView.setBackgroundColor(color);
        } else {
            int color = ContextCompat.getColor(context,
                    nightMode? R.color.darkCardBackground:R.color.defaultCardBackground);

            moment.momentView.setBackgroundColor(color);
        }

        moment.momentTitle.setText(moments.get(position).getName());
        moment.momentLocal.setText(moments.get(position).getLocal());
        moment.momentDate.setText(dateToString(moments.get(position).getDate()));
        moment.momentImage.setImageURI(Uri.parse(moments.get(position).getMainImgUri()));

        return convertView;
    }


}
