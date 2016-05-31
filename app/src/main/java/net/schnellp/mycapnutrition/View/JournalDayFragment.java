package net.schnellp.mycapnutrition.View;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.DataManager;
import net.schnellp.mycapnutrition.Model.IntOrNA;
import net.schnellp.mycapnutrition.Model.ObjectMath;
import net.schnellp.mycapnutrition.Model.Record;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.Presenter.ExpandableRecordListAdapter;
import net.schnellp.mycapnutrition.R;

import java.util.ArrayList;

public class JournalDayFragment extends Fragment {

    public DataManager datasource;
    public static final String DATE = "day_number";
    private String date;
    ExpandableRecordListAdapter adapter;
    Record tempRecord;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_journal_day_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        date = getArguments().getString(DATE);
        textView.setText(date);

        ArrayList<Record> records = new ArrayList<>(MyCapNutrition.dataManager.getRecordsFromDate(date));

        ExpandableRecordListView listView = (ExpandableRecordListView)
                rootView.findViewById(R.id.recordListView);
        adapter = new ExpandableRecordListAdapter(this.getActivity(), records);

        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        buildOrRebuild();

        return rootView;
    }

    @Override
    public void onResume() {
        buildOrRebuild();
        super.onResume();
    }

    private void buildOrRebuild() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int goalKcal = Integer.parseInt(sharedPref.getString("goal_kcal", "2000"));
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        TextView progressText = (TextView) rootView.findViewById(R.id.kcal_label);
        IntOrNA kcal = ObjectMath.kcalSum(adapter.getRecords());
        progressBar.setMax(goalKcal);
        progressBar.setProgress((kcal.isNA) ? 0 : kcal.val);
        progressText.setText(kcal + " / " + goalKcal + " kcal");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.recordListView) {
            MenuInflater inflater = this.getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_context_recordrow_group, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                // edit stuff here
                return true;
            case R.id.delete:
                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(R.id.main_content), "Record deleted.",
                                Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(getActivity()
                                        .findViewById(R.id.main_content),
                                        "Food is restored!", Snackbar.LENGTH_SHORT);
                                JournalDayFragment.this.addRecord(tempRecord);
                                snackbar1.show();
                            }
                        });
                snackbar.show();
                tempRecord = (Record) adapter.getGroup((int) info.packedPosition);
                deleteRecord(ExpandableListView.getPackedPositionGroup(info.packedPosition));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public String getDate() {
        return date;
    }

    public void deleteRecord(int position) {
        Record record = (Record) adapter.getGroup(position);
        MyCapNutrition.dataManager.deleteRecord(record);
        adapter.removeRecord(position);
    }

    public boolean addRecord(Record record) {
        MyCapNutrition.dataManager.restoreRecord(record);
        adapter.addRecord(record);
        return true;
    }
}
