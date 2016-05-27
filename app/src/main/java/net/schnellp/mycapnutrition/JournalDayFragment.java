package net.schnellp.mycapnutrition;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import net.schnellp.mycapnutrition.data.FoodDataSource;
import net.schnellp.mycapnutrition.data.Record;

import java.util.ArrayList;

public class JournalDayFragment extends Fragment {

    public FoodDataSource datasource;
    public static final String DATE = "day_number";
    private String date;
    ExpandableRecordListAdapter adapter;
    Record tempRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journal_day_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        date = getArguments().getString(DATE);
        textView.setText(date);

        datasource = new FoodDataSource(this.getContext());
        datasource.open();

        ArrayList<Record> records = new ArrayList<>(datasource.getRecordsFromDate(date));
        ArrayList<FoodListGroup> groups = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            groups.add(i, new FoodListGroup(records.get(i)));
        }

        ExpandableRecordListView listView = (ExpandableRecordListView)
                rootView.findViewById(R.id.recordListView);
        adapter = new ExpandableRecordListAdapter(this.getActivity(), groups);

        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        return rootView;
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
                tempRecord = ((FoodListGroup) adapter.getGroup((int) info.packedPosition))
                        .record;
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
        Record record = ((FoodListGroup) adapter.getGroup(position)).record;
        datasource.open();
        datasource.deleteRecord(record);
        datasource.close();
        adapter.removeRecord(position);
    }

    public boolean addRecord(Record record) {
        datasource.open();
        datasource.restoreRecord(record);
        datasource.close();
        adapter.addRecord(record);
        return true;
    }
}
