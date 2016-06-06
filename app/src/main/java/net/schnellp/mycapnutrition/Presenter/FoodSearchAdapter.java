package net.schnellp.mycapnutrition.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.MultiSelectListView.ActivatedLinearLayout;
import net.schnellp.mycapnutrition.View.AddFood;
import net.schnellp.mycapnutrition.MultiSelectListView.CheckableObject;
import net.schnellp.mycapnutrition.MultiSelectListView.MultiSelectAdapter;
import net.schnellp.mycapnutrition.MultiSelectListView.MultiSelectInputListener;
import net.schnellp.mycapnutrition.View.RecordView;
import net.schnellp.mycapnutrition.View.SelectFood;
import net.schnellp.mycapnutrition.View.UnitList;

import java.util.ArrayList;
import java.util.Collections;

public class FoodSearchAdapter extends MultiSelectAdapter<Food> implements Filterable {

    private SelectFood selectFood;
    private ArrayList<CheckableObject<Food>> originalItems = new ArrayList<>();
    LayoutInflater inflater;

    public FoodSearchAdapter(SelectFood selectFood, Context context) {
        this.selectFood = selectFood;
        addAll(MyCapNutrition.dataManager.getAllFoods());
        originalItems.addAll(items);
        inflater = LayoutInflater.from(context);
    }

    public void editItem(int position) {
        SelectFood context = selectFood;
        Food food = getTypedItem(position);
        Intent intent = new Intent(context, AddFood.class);
        intent.putExtra("CALLED_FOR_RESULT", true);
        intent.putExtra("food_dbid", food.DBID);
        selectFood.startActivityForResult(intent, 1);
    }

    public void deleteItem(int position) {
        Food food = getTypedItem(position);
        MyCapNutrition.dataManager.deactivateFood(food);
        originalItems.remove(position);
        getFilter().filter("");
        notifyDataSetChanged();
    }

    public void deleteCheckedItems() {
        ArrayList<Integer> checkedPositions = getCheckedPositions();
        Collections.sort(checkedPositions, Collections.<Integer>reverseOrder());
        for (Integer i : checkedPositions) {
            deleteItem(i);
        }
    }

    public void restoreItem(Food food, int position) {
        MyCapNutrition.dataManager.restoreFood(food);
        originalItems.add(position, new CheckableObject<>(food));
        getFilter().filter("");
        notifyDataSetChanged();
    }

    public void restoreItems(ArrayList<Food> foods, ArrayList<Integer> positions) {
        for (int i = 0; i < foods.size(); i++) {
            restoreItem(foods.get(i), positions.get(i));
        }
    }

    private class ViewHolder {
        ActivatedLinearLayout llContainer;
        TextView tvName, tvDetails;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

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
        holder.tvDetails.setText(getTypedItem(position).kcal + " kcal / " +
        getTypedItem(position).referenceServing_mg.toDoubleOrNA().divide(1000).round() +
        " g");

        MultiSelectFoodSearchInputListener listener =
                new MultiSelectFoodSearchInputListener(this, position);
        holder.llContainer.setOnClickListener(listener);
        holder.llContainer.setOnLongClickListener(listener);
        holder.llContainer.setChecked(isItemChecked(position));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                items = (ArrayList<CheckableObject<Food>>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CheckableObject<Food>> FilteredArrList = new ArrayList<>();

                if (originalItems == null) {
                    originalItems = new ArrayList<>(items); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = originalItems.size();
                    results.values = originalItems;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < originalItems.size(); i++) {
                        String data = (originalItems.get(i).object).name;
                        if (matches(data, constraint.toString())) {
                            FilteredArrList.add(originalItems.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

            protected boolean matches(String target, String query) {
                return target.toLowerCase().contains(query);
            }
        };
        return filter;
    }

    private class MultiSelectFoodSearchInputListener extends MultiSelectInputListener {

        public MultiSelectFoodSearchInputListener(MultiSelectAdapter ms, int position) {
            super(ms, position);
        }

        @Override
        public void onClick(View v) {
            if (ms.getNumChecked() > 0) {
                super.onClick(v);
            } else {
                Intent foundingIntent = ((SelectFood) v.getContext()).getIntent();
                Intent intent;

                switch (foundingIntent.getStringExtra(SelectFood.Purpose.INTENT_EXTRA_NAME)) {
                    case SelectFood.Purpose.FILTER_UNITS:
                        intent = new Intent(selectFood, UnitList.class);
                        break;
                    case SelectFood.Purpose.SWITCH_RECORD_FOOD:
                        intent = new Intent(selectFood, RecordView.class);
                        break;
                    case SelectFood.Purpose.CREATE_RECORD:
                        intent = new Intent(selectFood, RecordView.class);
                        break;
                    default:
                        return;
                }

                Food food = getTypedItem(position);
                intent.putExtras(((SelectFood) v.getContext()).getIntent());
                intent.putExtra("food_dbid", food.DBID);

                if (foundingIntent.getStringExtra(SelectFood.Purpose.INTENT_EXTRA_NAME)
                        .equals(SelectFood.Purpose.SWITCH_RECORD_FOOD)) {
                    selectFood.setResult(Activity.RESULT_OK, intent);
                    selectFood.finish();
                } else if (!foundingIntent.getStringExtra(SelectFood.Purpose.INTENT_EXTRA_NAME)
                        .equals(SelectFood.Purpose.LIST)) {
                    selectFood.startActivity(intent);
                }
            }
        }
    }

}
