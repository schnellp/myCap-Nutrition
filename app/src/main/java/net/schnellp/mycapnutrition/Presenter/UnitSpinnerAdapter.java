package net.schnellp.mycapnutrition.Presenter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Unit;
import net.schnellp.mycapnutrition.R;

import java.util.ArrayList;

public class UnitSpinnerAdapter extends BaseAdapter {

    public final ArrayList<Unit> units;
    public LayoutInflater inflater;
    public Activity activity;

    public UnitSpinnerAdapter(Activity act, ArrayList<Unit> units) {
        activity = act;
        this.units = units;
        inflater = act.getLayoutInflater();
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.spinnerText);
        tv.setText(units.get(position).name);

        System.out.println(convertView);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.spinnerText);
        tv.setText(units.get(position).name);

        System.out.println(convertView);

        return convertView;
    }
}
