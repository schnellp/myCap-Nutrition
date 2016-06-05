package net.schnellp.mycapnutrition.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.View.MultiSelectListView.ActivatedLinearLayout;
import net.schnellp.mycapnutrition.View.MultiSelectListView.MultiSelectAdapter;
import net.schnellp.mycapnutrition.View.MultiSelectListView.MultiSelectInputListener;

import java.util.ArrayList;

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

    public void restoreItem(Unit unit) {
        MyCapNutrition.dataManager.restoreUnit(unit);
        items.add(new CheckableObject<>(unit));
        notifyDataSetChanged();
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

        return convertView;
    }
}
