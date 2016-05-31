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

    public final ArrayList<FoodListGroup> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public ExpandableRecordListAdapter(Activity act, ArrayList<FoodListGroup> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
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

        FoodListGroup group = (FoodListGroup) getGroup(groupPosition);

        text = (TextView) convertView.findViewById(R.id.tvRecordDetails);
        Record r = group.record;
        text.setText(r.carb_mg.toDoubleOrNA().divide(1000) + " g carbs | " +
        r.fat_mg.toDoubleOrNA().divide(1000) + " g fat | " +
        r.protein_mg.toDoubleOrNA().divide(1000) + " g protein");

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
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
        FoodListGroup group = (FoodListGroup) getGroup(groupPosition);
        ((TextView) convertView.findViewById(R.id.tvRecordFoodName)).setText(group.record.foodName);
        ((TextView) convertView.findViewById(R.id.tvRecordKcal)).setText(group.record.kcal.toString());
        ((TextView) convertView.findViewById(R.id.tvRecordAmount)).setText(
                group.record.quantity_cents.toDoubleOrNA().divide(100) + " " + group.record.unitName +
                " (" + group.record.amount_mg.toDoubleOrNA().divide(1000) + " g)");

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
        groups.add(new FoodListGroup(record));
        notifyDataSetChanged();
    }

    public void removeRecord(int position) {
        groups.remove(position);
        notifyDataSetChanged();
    }
}