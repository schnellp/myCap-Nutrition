package net.schnellp.mycapnutrition.view;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.schnellp.mycapnutrition.model.IntOrNA;
import net.schnellp.mycapnutrition.model.ObjectMath;
import net.schnellp.mycapnutrition.presenter.ExpandableRecordListAdapter;
import net.schnellp.mycapnutrition.R;


public class JournalDayFragment extends Fragment {

    public static final String DATE = "day_number";
    public String date;
    public ExpandableRecordListAdapter adapter;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_journal_day_view, container, false);

        date = getArguments().getString(DATE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ExpandableListView listView = (ExpandableListView)
                rootView.findViewById(R.id.recordListView);
        adapter = new ExpandableRecordListAdapter(this.getActivity(), date);

        listView.setAdapter(adapter);
        listView.setOnGroupClickListener(adapter);
        listView.setOnItemLongClickListener(adapter);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int goalKcal = Integer.parseInt(sharedPref.getString("goal_kcal", "2000"));
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        TextView progressText = (TextView) rootView.findViewById(R.id.kcal_label);
        IntOrNA kcal = ObjectMath.kcalSum(adapter.getRecords());
        progressBar.setMax(goalKcal);

        Resources resources = getContext().getResources();
        String progressString = kcal + " / " + goalKcal + " " + resources.getString(R.string.kcal);
        progressBar.setProgress((kcal.isNA) ? 0 : kcal.val);
        progressText.setText(progressString);
    }
}
