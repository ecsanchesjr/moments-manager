package com.example.ecsanchesjr.appmoments.Activities;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.ecsanchesjr.appmoments.Class.Utilities;
import com.example.ecsanchesjr.appmoments.R;

public class AboutActivity extends AppCompatActivity {

    private Switch themeSwitch;
    private int actualThemeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        actualThemeId = Utilities.readSharedPreferences(this);
        changeThemeOnAllActivities(false, actualThemeId);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // Activity title
        setTitle(getString(R.string.about_title));
        // Set Up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        themeSwitch = findViewById(R.id.themeSwitch);

        themeSwitch.setChecked(actualThemeId==R.style.AppThemeDark);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            actualThemeId = isChecked?R.style.AppThemeDark:R.style.AppTheme;
            writePreferencialStyle();
            changeThemeOnAllActivities(true, actualThemeId);
        });
    }

    private void writePreferencialStyle() {
        SharedPreferences shared = getSharedPreferences(Utilities.SHARED_PREFERECES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(Utilities.KEY_THEME, actualThemeId);
        editor.commit();
    }

    private void changeThemeOnAllActivities(boolean onThemeChange, int themeChoosen) {
        setTheme(themeChoosen);

        if(onThemeChange) {
            TaskStackBuilder.create(this)
                    .addNextIntent(new Intent(this, MomentsListActivity.class))
                    .addNextIntent(this.getIntent())
                    .startActivities();
        }
    }
}
