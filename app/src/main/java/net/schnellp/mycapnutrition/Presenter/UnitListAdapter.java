package net.schnellp.mycapnutrition.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.View.ActivatedLinearLayout;

import java.util.ArrayList;

public class UnitListAdapter extends BaseAdapter {

    private ArrayList<Unit> units = new ArrayList<>();
    private int nChecked = 0;
    private LayoutInflater inflater;

    public UnitListAdapter(Context context, Food food) {
        units.addAll(MyCapNutrition.dataManager.getUnitsForFood(food));
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return units.size();
    }

    @Override
    public Object getItem(int position) {
        return units.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void deleteItem(int position) {
        Unit unit = units.get(position);
        MyCapNutrition.dataManager.deactivateUnit(unit);
        units.remove(position);
        notifyDataSetChanged();
    }

    public void restoreItem(Unit unit) {
        MyCapNutrition.dataManager.restoreUnit(unit);
        units.add(unit);
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
        holder.tvName.setText(units.get(position).name);
        holder.tvDetails.setText(units.get(position).amount_mg.toDoubleOrNA().divide(1000).round()
                + " g");

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UnitListAdapter.this.nChecked > 0) {
                    if (((ActivatedLinearLayout) v).isChecked()) {
                        ((ActivatedLinearLayout) v).setChecked(false);
                        UnitListAdapter.this.nChecked--;
                    } else {
                        ((ActivatedLinearLayout) v).setChecked(true);
                        UnitListAdapter.this.nChecked++;
                    }
                }
            }
        });
        holder.llContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (((ActivatedLinearLayout) v).isChecked()) {
                    ((ActivatedLinearLayout) v).setChecked(false);
                    UnitListAdapter.this.nChecked--;
                } else {
                    ((ActivatedLinearLayout) v).setChecked(true);
                    UnitListAdapter.this.nChecked++;
                }

                return true;
            }
        });

        return convertView;
    }
}
