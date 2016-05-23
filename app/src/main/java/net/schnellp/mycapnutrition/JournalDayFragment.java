package net.schnellp.mycapnutrition;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Patrick on 5/23/2016.
 */
public class JournalDayFragment extends Fragment {

    SparseArray<FoodListGroup> groups = new SparseArray<FoodListGroup>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journal_day_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("test fragment");

        createData();

        ExpandableRecordListView listView = (ExpandableRecordListView)
                rootView.findViewById(R.id.recordListView);
        ExpandableRecordListAdapter adapter = new ExpandableRecordListAdapter(this.getActivity(), groups);

        listView.setAdapter(adapter);

        return rootView;
    }

    public void createData() {
        for (int j = 0; j < 5; j++) {
            FoodListGroup group = new FoodListGroup("Test " + j);
            for (int i = 0; i < 5; i++) {
                group.children.add("Sub Item" + i);
            }
            groups.append(j, group);
        }
    }
}
