package net.schnellp.mycapnutrition.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.MultiSelectListView.ActivatedLinearLayout;
import net.schnellp.mycapnutrition.MultiSelectListView.CheckableObject;
import net.schnellp.mycapnutrition.MultiSelectListView.MultiSelectAdapter;
import net.schnellp.mycapnutrition.MultiSelectListView.MultiSelectInputListener;

import java.util.ArrayList;
import java.util.Collections;

public class UnitListAdapter extends MultiSelectAdapter<Unit> {

    private LayoutInflater inflater;

    public UnitListAdapter(Context context, Food food) {
        addAll(MyCapNutrition.dataManager.getUnitsForFood(food));
        inflater = LayoutInflater.from(context);
    }

    public void deleteItem(int position) {
        Unit unit = getTypedItem(position);
        MyCapNutrition.dataManager.deactivateUnit(unit);
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

    public void restoreItem(Unit unit) {
        MyCapNutrition.dataManager.restoreUnit(unit);
        items.add(new CheckableObject<>(unit));
        notifyDataSetChanged();
    }

    public void restoreItems(ArrayList<Unit> units) {
        for (Unit unit : units) {
            restoreItem(unit);
        }
    }

    private class ViewHolder {
        ActivatedLinearLayout llContainer;
        TextView tvName, tvDetails;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.foodrow, null);
            holder.llContainer = (ActivatedLinearLayout) convertView.findViewById(R.id.selectfoodrow);
            holder.tvName = (TextView) convertView.findViewById(R.id.foodrowName);
            holder.tvDetails = (TextView) convertView.findViewById(R.id.foodrowDetails);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(getTypedItem(position).name);
        holder.tvDetails.setText(getTypedItem(position).amount_mg.toDoubleOrNA().divide(1000).round()
                + " g");

        MultiSelectInputListener listener = new MultiSelectInputListener(this, position);
        holder.llContainer.setOnClickListener(listener);
        holder.llContainer.setOnLongClickListener(listener);
        holder.llContainer.setChecked(isItemChecked(position));

        return convertView;
    }
}
