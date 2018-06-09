package com.example.ecsanchesjr.appmoments.Activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.ecsanchesjr.appmoments.Adapter.GalleryAdapter;
import com.example.ecsanchesjr.appmoments.Class.RequestCodes;
import com.example.ecsanchesjr.appmoments.Class.Utilities;
import com.example.ecsanchesjr.appmoments.R;

import java.util.ArrayList;


public class GalleryActivity extends AppCompatActivity {

    private GalleryAdapter galleryAdapter;
    private GridView photoGrid;
    private Uri momentMainImgUri;
    private boolean nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nightMode = (Utilities.readSharedPreferences(this) == R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setTitle(getString(R.string.gallery_activity_title));

        photoGrid = findViewById(R.id.photoGrid);
        galleryAdapter = new GalleryAdapter(this, new ArrayList<>(), nightMode);
        setGridViewListeners();

        if (getIntent().getSerializableExtra(RequestCodes.GalleryCodes.GALLERY_URIS.name()) != null) {
            ArrayList<String> stringUris = (ArrayList<String>)
                    getIntent().getSerializableExtra(RequestCodes.GalleryCodes.GALLERY_URIS.name());

            galleryAdapter.addImgs(Utilities.getMomentsUri(stringUris));
            photoGrid.setAdapter(galleryAdapter);
            if (getIntent().getStringExtra(RequestCodes.GalleryCodes.MAIN_IMG_URI.name()) != null) {
                momentMainImgUri = Uri.parse(getIntent().getStringExtra(
                        RequestCodes.GalleryCodes.MAIN_IMG_URI.name()));
                galleryAdapter.setMainImgPosition(momentMainImgUri);
            }
        }
    }

    private void finishGalleryActivity() {
        Intent galleryIntent = new Intent();

        galleryIntent.putExtra(RequestCodes.GalleryCodes.GALLERY_URIS.name(),
                Utilities.getStringsUri(galleryAdapter.getImgsUri()));

        if (momentMainImgUri != null)
            galleryIntent.putExtra(RequestCodes.GalleryCodes.MAIN_IMG_URI.name(),
                    momentMainImgUri.toString());

        galleryIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        setResult(Activity.RESULT_OK, galleryIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent addPhotosIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        addPhotosIntent.setType("image/*");
        //addPhotosIntent.setAction(Intent.ACTION_GET_CONTENT);
        addPhotosIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        addPhotosIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        addPhotosIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(addPhotosIntent, "Select Picture"), 1);
        return true;
    }

    private void setMomentMainImgUri(Uri imgUri) {
        momentMainImgUri = imgUri;
        galleryAdapter.setMainImgPosition(momentMainImgUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
            ContentResolver resolver = this.getContentResolver();
            if (data.getClipData() != null) {
                // return on this way
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    resolver.takePersistableUriPermission(uri, takeFlags);
                    galleryAdapter.addImg(uri);
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                resolver.takePersistableUriPermission(uri, takeFlags);
                // return in this way
                galleryAdapter.addImg(uri);
            }
            photoGrid.setAdapter(galleryAdapter);
        }
    }

    private void setGridViewListeners() {
        photoGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        photoGrid.setOnItemClickListener((parent, view, position, id) -> {
            Uri imgUri = galleryAdapter.getImageUri(position);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(imgUri, "image/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        });
        photoGrid.setMultiChoiceModeListener(new MultiChoiceModeListener());
    }

    private class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            galleryAdapter.toggleChecked(position);
            galleryAdapter.notifyDataSetChanged();

            int selectedCount = galleryAdapter.getImagesChecked().size();
            if (selectedCount > 0) {
                mode.setTitle(getResources().getQuantityString(R.plurals.moments_selecteds, selectedCount, selectedCount));
            }

            mode.invalidate();
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_gallery_selected, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.getItem(0).setVisible(!(galleryAdapter.getImagesChecked().size() > 1));
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.deleteGalleryMenuItem:
                    ArrayList<Integer> checkedsToRemove = galleryAdapter.getImagesChecked();
                    galleryAdapter.removeImagesChecked(checkedsToRemove);
                    galleryAdapter.notifyDataSetChanged();
                    break;

                case R.id.setMainImgMenuItem:
                    int position = galleryAdapter.getImagesChecked().get(0);
                    setMomentMainImgUri(galleryAdapter.getImageUri(position));
                    break;
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            galleryAdapter.clearImagesChecked();
        }
    }

    @Override
    public void onBackPressed() {
        finishGalleryActivity();
    }
}
