package net.schnellp.mycapnutrition;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.schnellp.mycapnutrition.data.Food;
import net.schnellp.mycapnutrition.data.FoodDataSource;
import net.schnellp.mycapnutrition.data.Record;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class JournalDayFragment extends Fragment {

    SparseArray<FoodListGroup> groups = new SparseArray<>();
    public FoodDataSource datasource;
    public static final String DAY_NUMBER = "day_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journal_day_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getArguments().getString(DAY_NUMBER));

        datasource = new FoodDataSource(this.getContext());
        datasource.open();

        ArrayList<Record> records = new ArrayList<>(datasource.getAllRecords());

        for (int i = 0; i < records.size(); i++) {
            groups.append(i, new FoodListGroup(records.get(i)));
        }

        ExpandableRecordListView listView = (ExpandableRecordListView)
                rootView.findViewById(R.id.recordListView);
        ExpandableRecordListAdapter adapter = new ExpandableRecordListAdapter(this.getActivity(), groups);

        listView.setAdapter(adapter);

        return rootView;
    }
}
