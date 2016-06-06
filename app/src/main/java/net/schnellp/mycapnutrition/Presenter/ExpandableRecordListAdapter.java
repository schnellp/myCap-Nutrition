package net.schnellp.mycapnutrition.Presenter;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Record;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.View.MultiSelectListView.ActivatedLinearLayout;
import net.schnellp.mycapnutrition.View.MultiSelectListView.CheckableObject;
import net.schnellp.mycapnutrition.View.MultiSelectListView.ExpandableMultiSelectAdapter;
import net.schnellp.mycapnutrition.View.MultiSelectListView.ExpandableMultiSelectInputListener;

import java.util.ArrayList;
import java.util.Collections;

public class ExpandableRecordListAdapter extends ExpandableMultiSelectAdapter<Record> {

    public LayoutInflater inflater;
    public Activity activity;

    public ExpandableRecordListAdapter(Activity act, String date) {
        activity = act;
        addAll(MyCapNutrition.dataManager.getRecordsFromDate(date));
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
        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recordrow_details, null);
        }

        Record record = (Record) getGroup(groupPosition);

        text = (TextView) convertView.findViewById(R.id.tvRecordDetails);
        text.setText(record.carb_mg.toDoubleOrNA().divide(1000).round() + " g carbs | " +
                record.fat_mg.toDoubleOrNA().divide(1000).round() + " g fat | " +
                record.protein_mg.toDoubleOrNA().divide(1000).round() + " g protein");

        return convertView;
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
                record.quantity_cents.toDoubleOrNA().divide(100) + " x " + record.unitName +
                " (" + record.amount_mg.toDoubleOrNA().divide(1000).round() + " g)");

        ExpandableMultiSelectInputListener listener = new ExpandableMultiSelectInputListener(this, groupPosition);

        ActivatedLinearLayout llContainer = (ActivatedLinearLayout)
                convertView.findViewById(R.id.ll_record_row_group);

        llContainer.setOnClickListener(listener);
        llContainer.setOnLongClickListener(listener);
        llContainer.setChecked(isItemChecked(groupPosition));

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

    public void deleteItem(int position) {
        MyCapNutrition.dataManager.deactivateRecord(getTypedGroup(position));
        items.remove(position);
        notifyDataSetChanged();
    }

    public void deleteCheckedItems() {
        ArrayList<Integer> checkedPositions = getCheckedPositions();
        Collections.sort(checkedPositions, Collections.<Integer>reverseOrder());
        for (Integer i : checkedPositions) {
            deleteItem(i);
        }
    }

    public void restoreItem(Record record) {
        MyCapNutrition.dataManager.restoreRecord(record);
        items.add(new CheckableObject<>(record));
        notifyDataSetChanged();
    }

    public void restoreItems(ArrayList<Record> records) {
        for (Record record : records) {
            restoreItem(record);
        }
    }

    public ArrayList<Record> getRecords() {
        ArrayList<Record> records = new ArrayList<>();
        for (CheckableObject<Record> o : items) {
            records.add(o.object);
        }
        return records;
    }
}
