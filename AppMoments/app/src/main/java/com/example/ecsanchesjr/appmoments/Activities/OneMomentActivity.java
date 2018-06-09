package com.example.ecsanchesjr.appmoments.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecsanchesjr.appmoments.Class.Moment;
import com.example.ecsanchesjr.appmoments.Class.RequestCodes;
import com.example.ecsanchesjr.appmoments.Class.Utilities;
import com.example.ecsanchesjr.appmoments.DAO.MomentDatabase;
import com.example.ecsanchesjr.appmoments.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.ecsanchesjr.appmoments.Class.Utilities.dateToString;
import static com.example.ecsanchesjr.appmoments.Class.Utilities.getStringsUri;
import static com.example.ecsanchesjr.appmoments.Class.Utilities.stringToDate;

public class OneMomentActivity extends AppCompatActivity {

    private final Calendar momentDate = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener;
    private Moment moment;
    private String actualImgUri;
    private RequestCodes request;
    private ArrayList<Uri> momentGallery;
    private EditText momentDateEditText;
    private EditText momentNameText;
    private EditText momentLocalText;
    private EditText momentDescriptionText;
    private Button momentGalleryButton;
    private TextView momentGalleryQuantity;
    private TextView momentMainImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.readSharedPreferences(this);
        super.onCreate(savedInstanceState);
        // Set Up button at TopBar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_one_moment);

        momentDateEditText = findViewById(R.id.momentDateEditText);
        momentNameText = findViewById(R.id.momentNameText);
        momentLocalText = findViewById(R.id.momentLocalText);
        momentDescriptionText = findViewById(R.id.momentDescriptionText);
        momentGalleryButton = findViewById(R.id.galleryOpenButton);
        momentGalleryQuantity = findViewById(R.id.momentGalleryQuantity);
        momentMainImg = findViewById(R.id.momentMainImg);

        momentGalleryButton.setOnClickListener(v -> showGalleryActivity());
        setDatePickerListeners();

        moment = new Moment();

        if (getIntent().getSerializableExtra("moment") != null) {
            moment = (Moment) getIntent().getSerializableExtra("moment");
            request = RequestCodes.CHANGE_ITEM;
            setMomentData();

            // Title of activity
            if (getIntent().getExtras().getInt("isChange") == RequestCodes.CHANGE_ITEM.ordinal()) {
                setTitle(getString(R.string.change_moment_title));
            } else {
                setTitle(getString(R.string.show_moment_title));
            }
        } else {
            // Title of activity
            setTitle(getString(R.string.add_moment_title));
            request = RequestCodes.ADD_MOMENT;
            momentGallery = new ArrayList<>();
            momentMainImg.setText(
                    getResources().getString(R.string.one_moment_main_img_not_defined));
            momentGalleryQuantity.setText(
                    getResources().getString(R.string.one_moment_gallery_quantity_empty));
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (request == RequestCodes.ADD_MOMENT) {
            menu.getItem(1).setVisible(false);
        }
        return true;
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
                validateFields();
                return true;

            case R.id.oneMomentDeleteMenuItem:
                showAlertConfirm(moment);
                return true;

            default:
                return false;
        }
    }

    private void setMomentData() {
        momentDateEditText.setText(dateToString(moment.getDate()));
        momentNameText.setText(moment.getName());
        momentDescriptionText.setText(
                (moment.getDescription().isEmpty() ?
                        getString(R.string.one_moment_no_description) : moment.getDescription()));
        momentLocalText.setText(moment.getLocal());
        actualImgUri = moment.getMainImgUri();
        momentGallery = Utilities.getMomentsUri(moment.getGallery());

        if(actualImgUri == null) {
            momentMainImg.setText(getResources().getString(R.string.one_moment_main_img_not_defined));
        } else {
            momentMainImg.setText(getResources().getString(R.string.one_moment_main_img_defined));
        }

        int gallerySize = momentGallery.size();
        if(gallerySize == 0) {
            momentGalleryQuantity.setText(
                    getResources().getString(R.string.one_moment_gallery_quantity_empty));
        } else {
            momentGalleryQuantity.setText(
                    getResources().getQuantityString(R.plurals.one_moment_gallery_quantity, gallerySize, gallerySize));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.GALERY_REQUEST.ordinal() && resultCode == Activity.RESULT_OK) {
            Bundle urisData = data.getExtras();
            if (urisData != null) {
                momentGallery = Utilities.getMomentsUri((ArrayList<String>) urisData.
                        getSerializable(RequestCodes.GalleryCodes.GALLERY_URIS.name()));

                if (urisData.getString(RequestCodes.GalleryCodes.MAIN_IMG_URI.name()) != null) {
                    actualImgUri = urisData.getString(RequestCodes.GalleryCodes.MAIN_IMG_URI.name());

                    momentMainImg.setText(getResources().getString(R.string.one_moment_main_img_defined));
                }

                if(momentGallery.size() == 0) {
                    momentGalleryQuantity.setText(
                            getResources().getString(R.string.one_moment_gallery_quantity_empty));
                } else {
                    momentGalleryQuantity.setText(
                            getResources().getQuantityString(R.plurals.one_moment_gallery_quantity,
                                    momentGallery.size(),
                                    momentGallery.size()));
                }
            }
        }
        momentGallery.forEach(el -> System.out.println(el.toString()));
    }

    private void saveMoment(Moment m) {
        if (request == RequestCodes.CHANGE_ITEM) {
            m.setId(moment.getId());
            MomentDatabase.getDatabase(this).momentDao().update(m);
            Toast.makeText(this, R.string.moment_change_success, Toast.LENGTH_SHORT).show();
        } else {
            MomentDatabase.getDatabase(this).momentDao().insert(m);
            Toast.makeText(this, R.string.moment_insert_success, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showAlertConfirm(final Moment moment) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_confirm_delete_title)
                .setMessage(getString(R.string.alert_confirm_delete_message) + "\n" + moment.getName())
                .setPositiveButton(R.string.alert_confirm_delete_yes, (dialog, which) -> deleteEventOK(moment))
                .setNegativeButton(R.string.alert_confirm_delete_no, null).show();
    }

    private void deleteEventOK(Moment moment) {
        MomentDatabase.getDatabase(this).momentDao().delete(moment);
        Toast.makeText(this, R.string.alert_confirm_delete_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setDatePickerListeners() {
        dateSetListener = (view, year, month, dayOfMonth) -> {
            momentDate.set(Calendar.YEAR, year);
            momentDate.set(Calendar.MONTH, month);
            momentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            momentDateEditText.setText(dateToString(momentDate.getTime()));
        };
        momentDateEditText.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        new DatePickerDialog(this, dateSetListener,
                momentDate.get(Calendar.YEAR),
                momentDate.get(Calendar.MONTH),
                momentDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showGalleryActivity() {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(RequestCodes.GalleryCodes.GALLERY_URIS.name(),
                getStringsUri(momentGallery));
        intent.putExtra(RequestCodes.GalleryCodes.MAIN_IMG_URI.name(),
                    moment.getMainImgUri());
        startActivityForResult(intent, RequestCodes.GALERY_REQUEST.ordinal());
    }

    private void validateFields() {
        String name = momentNameText.getText().toString();
        String date = momentDateEditText.getText().toString();
        String local = momentLocalText.getText().toString();
        String description = momentDescriptionText.getText().toString();

        if (name.isEmpty() || date.isEmpty() || local.isEmpty()) {
            Toast.makeText(this, R.string.empty_fields_toast, Toast.LENGTH_SHORT).show();
        } else {
            try {
                Moment m;
                if (momentGallery != null) {
                    m = new Moment(
                            name,
                            local,
                            stringToDate(date),
                            actualImgUri,
                            description,
                            Utilities.getStringsUri(momentGallery));
                } else {
                    m = new Moment(
                            name,
                            local,
                            stringToDate(date),
                            actualImgUri,
                            description);
                }
                saveMoment(m);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
