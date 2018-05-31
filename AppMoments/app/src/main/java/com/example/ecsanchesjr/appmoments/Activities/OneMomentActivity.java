package com.example.ecsanchesjr.appmoments.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ecsanchesjr.appmoments.Class.Moment;
import com.example.ecsanchesjr.appmoments.Class.RequestCodes;
import com.example.ecsanchesjr.appmoments.DAO.MomentDatabase;
import com.example.ecsanchesjr.appmoments.R;

import java.text.ParseException;
import java.util.Calendar;

import static com.example.ecsanchesjr.appmoments.Class.Utilities.dateToString;
import static com.example.ecsanchesjr.appmoments.Class.Utilities.stringToDate;

public class OneMomentActivity extends AppCompatActivity {

    private final Calendar momentDate = Calendar.getInstance();
    private EditText momentDateEditText;
    private EditText momentNameText;
    private EditText momentLocalText;
    private EditText momentDescriptionText;
    private ImageButton momentAddPhotoButton;
    private Moment moment;
    private RequestCodes request;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            momentDate.set(Calendar.YEAR, year);
            momentDate.set(Calendar.MONTH, month);
            momentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            momentDateEditText.setText(dateToString(momentDate.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_moment);

        momentDateEditText = findViewById(R.id.momentDateEditText);
        momentNameText = findViewById(R.id.momentNameText);
        momentLocalText = findViewById(R.id.momentLocalText);
        momentDescriptionText = findViewById(R.id.momentDescriptionText);
        momentAddPhotoButton = findViewById(R.id.imageButton);

        momentDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        momentAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGalleryPicker();
            }
        });

        if (getIntent().getSerializableExtra("moment") != null) {
            moment = (Moment) getIntent().getSerializableExtra("moment");
            setMomentData();
            request = RequestCodes.CHANGE_ITEM;
            // Title of activity
            if(getIntent().getExtras().getInt("isChange") == RequestCodes.CHANGE_ITEM.ordinal()) {
                setTitle(getString(R.string.change_moment_title));
            } else {
                setTitle(getString(R.string.show_moment_title));
            }
        } else {
            // Title of activity
            setTitle(getString(R.string.add_moment_title));
        }
    }

    private void setMomentData() {
        momentDateEditText.setText(dateToString(moment.getDate()));
        momentNameText.setText(moment.getName());
        momentDescriptionText.setText(
                (moment.getDescription().isEmpty() ?
                        getString(R.string.one_moment_no_description) : moment.getDescription()));
        momentLocalText.setText(moment.getLocal());
    }

    public void showDatePicker() {
        new DatePickerDialog(this, dateSetListener,
                momentDate.get(Calendar.YEAR),
                momentDate.get(Calendar.MONTH),
                momentDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void showGalleryPicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();

                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    System.out.println(imageUri.toString());

                }
            } else if (data.getData() != null) {
                String imagePath = data.getData().getPath();
                System.out.println(imagePath);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.one_moment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.oneMomentSaveMenuItem:
                saveMoment();
                return true;

            case R.id.oneMomentDeleteMenuItem:
                showAlertConfirm(moment);
                return true;

            default:
                return false;
        }
    }

    private void saveMoment() {
        String name = momentNameText.getText().toString();
        String date = momentDateEditText.getText().toString();
        String local = momentLocalText.getText().toString();
        String description = momentDescriptionText.getText().toString();
        String path = "";

        try {
            Moment m = new Moment(name, local, stringToDate(date), path, description);
            if(request == RequestCodes.CHANGE_ITEM) {
                m.setId(moment.getId());
                MomentDatabase.getDatabase(this).momentDao().update(m);
                Toast.makeText(this, R.string.moment_change_success, Toast.LENGTH_SHORT).show();
            } else {
                MomentDatabase.getDatabase(this).momentDao().insert(m);
                Toast.makeText(this, R.string.moment_insert_success, Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showAlertConfirm(final Moment moment) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_confirm_delete_title)
                .setMessage(getString(R.string.alert_confirm_delete_message) + "\n" + moment.getName())
                .setPositiveButton(R.string.alert_confirm_delete_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEventOK(moment);
                    }
                })
                .setNegativeButton(R.string.alert_confirm_delete_no, null).show();
    }

    private void deleteEventOK(Moment moment) {
        MomentDatabase.getDatabase(this).momentDao().delete(moment);
        Toast.makeText(this, R.string.alert_confirm_delete_success, Toast.LENGTH_SHORT).show();
        finish();
    }
}
