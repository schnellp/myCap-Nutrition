package net.schnellp.mycapnutrition.Presenter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Record;
import net.schnellp.mycapnutrition.R;

import java.util.ArrayList;

public class ExpandableRecordListAdapter extends BaseExpandableListAdapter {

    public final ArrayList<Record> records;
    public LayoutInflater inflater;
    public Activity activity;

    public ExpandableRecordListAdapter(Activity act, ArrayList<Record> records) {
        activity = act;
        this.records = records;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return "placeholder";
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String children = (String) getChild(groupPosition, childPosition);
        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recordrow_details, null);
        }

        Record record = (Record) getGroup(groupPosition);

        text = (TextView) convertView.findViewById(R.id.tvRecordDetails);
        text.setText(record.carb_mg.toDoubleOrNA().divide(1000) + " g carbs | " +
                record.fat_mg.toDoubleOrNA().divide(1000) + " g fat | " +
                record.protein_mg.toDoubleOrNA().divide(1000) + " g protein");

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return records.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return records.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recordrow_group, null);
        }
        Record record = (Record) getGroup(groupPosition);
        ((TextView) convertView.findViewById(R.id.tvRecordFoodName)).setText(record.foodName);
        ((TextView) convertView.findViewById(R.id.tvRecordKcal)).setText(record.kcal.toString());
        ((TextView) convertView.findViewById(R.id.tvRecordAmount)).setText(
                record.quantity_cents.toDoubleOrNA().divide(100) + " " + record.unitName +
                " (" + record.amount_mg.toDoubleOrNA().divide(1000) + " g)");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void addRecord(Record record) {
        records.add(record);
        notifyDataSetChanged();
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void removeRecord(int position) {
        records.remove(position);
        notifyDataSetChanged();
    }
}
