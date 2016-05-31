package net.schnellp.mycapnutrition.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.View.AddFood;
import net.schnellp.mycapnutrition.View.RecordView;
import net.schnellp.mycapnutrition.View.SelectFood;

import java.util.ArrayList;

public class FoodSearchAdapter extends BaseAdapter implements Filterable {

    private SelectFood selectFood;
    private ArrayList<Food> foodsOriginalValues = new ArrayList<>();
    private ArrayList<Food> foodsDisplayedValues = new ArrayList<>();
    LayoutInflater inflater;

    public FoodSearchAdapter(SelectFood selectFood, Context context) {
        this.selectFood = selectFood;
        foodsOriginalValues.addAll(MyCapNutrition.dataManager.getAllFoods());
        foodsDisplayedValues.addAll(MyCapNutrition.dataManager.getAllFoods());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return foodsDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return foodsDisplayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void editItem(int position) {
        SelectFood context = selectFood;
        Food food = foodsDisplayedValues.get(position);
        Intent intent = new Intent(context, AddFood.class);
        intent.putExtra("CALLED_FOR_RESULT", true);
        intent.putExtra("food_dbid", food.DBID);
        selectFood.startActivityForResult(intent, 1);
    }

    public void addItem(Food food) {
        MyCapNutrition.dataManager.restoreFood(food);
        foodsOriginalValues.add(food);
        getFilter().filter("");
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        Food food = foodsDisplayedValues.get(position);
        MyCapNutrition.dataManager.deleteFood(food);
        foodsOriginalValues.remove(position);
        getFilter().filter("");
        notifyDataSetChanged();
    }

    private class ViewHolder {
        LinearLayout llContainer;
        TextView tvName, tvDetails;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.foodrow, null);
            holder.llContainer = (LinearLayout) convertView.findViewById(R.id.selectfoodrow);
            holder.tvName = (TextView) convertView.findViewById(R.id.foodrowName);
            holder.tvDetails = (TextView) convertView.findViewById(R.id.foodrowDetails);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(foodsDisplayedValues.get(position).name);
        holder.tvDetails.setText(foodsDisplayedValues.get(position).kcal + "");


        holder.llContainer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(selectFood, RecordView.class);
                Food food = foodsDisplayedValues.get(position);
                intent.putExtra("food_dbid", food.DBID);
                intent.putExtras(((SelectFood) v.getContext()).getIntent());

                if (((SelectFood) v.getContext()).getIntent().getBooleanExtra("CALLED_FOR_RESULT", false)) {
                    selectFood.setResult(Activity.RESULT_OK, intent);
                    selectFood.finish();
                } else if (intent.getExtras().containsKey("DATE")) {
                    selectFood.startActivity(intent);
                }
            }
        });

        /*
        holder.llContainer.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                v.setBackgroundColor(ContextCompat.getColor(v.getContext(), android.R.color.holo_purple));

                return true;
            }
        });
        */

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                foodsDisplayedValues = (ArrayList<Food>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Food> FilteredArrList = new ArrayList<>();

                if (foodsOriginalValues == null) {
                    foodsOriginalValues = new ArrayList<>(foodsDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = foodsOriginalValues.size();
                    results.values = foodsOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < foodsOriginalValues.size(); i++) {
                        String data = foodsOriginalValues.get(i).name;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(foodsOriginalValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

}
