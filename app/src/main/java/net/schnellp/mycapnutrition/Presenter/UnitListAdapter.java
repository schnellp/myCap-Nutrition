package net.schnellp.mycapnutrition.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.schnellp.mycapnutrition.model.Food;
import net.schnellp.mycapnutrition.model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.multiselect.ActivatedLinearLayout;
import net.schnellp.mycapnutrition.multiselect.CheckableObject;
import net.schnellp.mycapnutrition.multiselect.MultiSelectAdapter;
import net.schnellp.mycapnutrition.view.AddUnit;
import net.schnellp.mycapnutrition.view.UnitList;

import java.util.ArrayList;
import java.util.Collections;

public class UnitListAdapter extends MultiSelectAdapter<Unit> {

    private UnitList unitList;
    private LayoutInflater inflater;

    public UnitListAdapter(Context context, Food food) {
        addAll(MyCapNutrition.dataManager.getUnitsForFood(food));
        inflater = LayoutInflater.from(context);
        unitList = (UnitList) context;
    }

    public void editItem(int position) {
        UnitList context = unitList;
        Unit unit = getTypedItem(position);
        Intent intent = new Intent(context, AddUnit.class);
        intent.putExtra("CALLED_FOR_RESULT", true);
        intent.putExtra("unit_dbid", unit.DBID);
        unitList.startActivityForResult(intent, 1);
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

        holder.llContainer.setChecked(isItemChecked(position));

        return convertView;
    }
}
