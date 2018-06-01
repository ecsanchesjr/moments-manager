package com.example.ecsanchesjr.appmoments.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ecsanchesjr.appmoments.Class.Utilities;
import com.example.ecsanchesjr.appmoments.R;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.readSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }
}
