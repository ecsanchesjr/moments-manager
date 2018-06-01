package com.example.ecsanchesjr.appmoments.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ecsanchesjr.appmoments.Adapter.MomentListAdapter;
import com.example.ecsanchesjr.appmoments.Class.Moment;
import com.example.ecsanchesjr.appmoments.Class.RequestCodes;
import com.example.ecsanchesjr.appmoments.Class.Utilities;
import com.example.ecsanchesjr.appmoments.DAO.MomentDatabase;
import com.example.ecsanchesjr.appmoments.R;

import java.util.ArrayList;

public class MomentsListActivity extends AppCompatActivity {

    private ListView momentsList;
    private MomentListAdapter momentListAdapter;
    private int appTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appTheme = Utilities.readSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments_list);
        momentsList = findViewById(R.id.momentsList);

        // Set all listeners to execute on uses related a momentsList ListView (organize Code)
        setMomentsListListeners();
        populateMomentsList();
    }

    private void populateMomentsList() {
        System.out.println("MAS PQ ISSO");
        momentListAdapter = new MomentListAdapter(this,
                                                    loadMoments(),
                                                    (appTheme == R.style.AppThemeDark));
        momentsList.setAdapter(momentListAdapter);
    }

    // Load moments to list
    private ArrayList<Moment> loadMoments() {
        return (ArrayList<Moment>) MomentDatabase.getDatabase(this).momentDao().getAllMoments();
    }

    // Show menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    // To override and execute actions on click in menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMenuItem:
                showAddActivity();
                break;

            case R.id.aboutMenuItem:
                showAboutActivity();
                break;

            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showAddActivity() {
        Intent intent = new Intent(this, OneMomentActivity.class);
        startActivityForResult(intent, RequestCodes.ADD_MOMENT.ordinal());
    }

    private void showAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    // Show Activity with a Moment Details
    public void showOneMomentWithItem(Moment moment) {
        Intent intent = new Intent(this, OneMomentActivity.class);
        intent.putExtra("moment", moment);
        startActivityForResult(intent, 1);
    }
    // Used for same action, but in addiction with a change in ActivityTitle
    public void showOneMomentWithItem(Moment moment, RequestCodes request) {
        Intent intent = new Intent(this, OneMomentActivity.class);
        intent.putExtra("moment", moment);
        intent.putExtra("isChange", request.ordinal());
        startActivityForResult(intent, 1);
    }

    private ArrayList<Moment> getCheckedItems() {
        ArrayList<Moment> moments = new ArrayList<>();
        for(int i : momentListAdapter.getMomentsChecked()) {
            moments.add(momentListAdapter.getMoment(i));
        }
        return moments;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            populateMomentsList();
        }
    }

    private void showAlertConfirm(final ArrayList<Moment> moments) {
        String momentsNames = "";
        for (Moment m : moments) {
            momentsNames += m.getName() + "\n";
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_confirm_delete_title)
                .setMessage(getString(R.string.alert_confirm_delete_message)+ "\n" +momentsNames)
                .setPositiveButton(R.string.alert_confirm_delete_yes, (dialog, which) -> deleteEventOk(moments))
                .setNegativeButton(R.string.alert_confirm_delete_no, null).show();
    }

    private void deleteEventOk(ArrayList<Moment> moments) {
        for (Moment m : moments) {
            MomentDatabase.getDatabase(this).momentDao().delete(m);
        }
        Toast.makeText(this, R.string.alert_confirm_delete_success, Toast.LENGTH_SHORT).show();
        populateMomentsList();
    }

    private void setMomentsListListeners() {
        momentsList.setOnItemClickListener((parent, view, position, id) -> {
            Moment clicked = momentListAdapter.getMoment(position);
            showOneMomentWithItem(clicked);
        });

        // Set the mode of selection to use a ContextMenu
        momentsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        momentsList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            // When a item is checked
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                momentListAdapter.toggleChecked(position);
                momentListAdapter.notifyDataSetChanged();

                int selectedCount = momentListAdapter.getMomentsChecked().size();
                if (selectedCount > 0) mode.setTitle(
                        getResources().getQuantityString(R.plurals.moments_selecteds, selectedCount, selectedCount));

                mode.invalidate();
            }

            // Inflate the menu on create
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.moments_list_selected, menu);
                return true;
            }

            // To change the visible in change button
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                menu.getItem(0).setVisible(!(momentsList.getCheckedItemCount() > 1));
                return true;
            }

            // Control click actions of menu
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.selectedMenuChangeItem:
                        showOneMomentWithItem(getCheckedItems().get(0),
                                RequestCodes.CHANGE_ITEM);
                        break;

                    case R.id.selectedMenuDeleteItem:
                        showAlertConfirm(getCheckedItems());
                        break;

                    default:
                        return false;
                }
                mode.finish();
                return true;
            }

            // Will run on destroy menu, setting all selected to not selected
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                momentListAdapter.clearMomentsChecked();
            }
        });
    }
}
