package net.schnellp.mycapnutrition.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.schnellp.mycapnutrition.model.DBContract;
import net.schnellp.mycapnutrition.model.Food;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.Objective;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.model.FoodManager;
import net.schnellp.mycapnutrition.multiselect.ActivatedLinearLayout;
import net.schnellp.mycapnutrition.view.AddFood;
import net.schnellp.mycapnutrition.multiselect.CheckableObject;
import net.schnellp.mycapnutrition.multiselect.MultiSelectAdapter;
import net.schnellp.mycapnutrition.view.FoodListFragment;
import net.schnellp.mycapnutrition.view.RecipeForm;
import net.schnellp.mycapnutrition.view.RecordView;
import net.schnellp.mycapnutrition.view.SelectFood;
import net.schnellp.mycapnutrition.view.UnitList;

import java.util.ArrayList;
import java.util.Collections;

public class FoodSearchAdapter extends MultiSelectAdapter<Food> {

    private FoodListFragment selectFood;
    LayoutInflater inflater;
    private int foodType;

    public FoodSearchAdapter(FoodListFragment selectFood, int foodType) {
        this.selectFood = selectFood;
        // addAll(MyCapNutrition.dataManager.foodManager.getAllFoodsOfType(foodType));
        inflater = LayoutInflater.from(selectFood.getContext());
        this.foodType = foodType;
    }

    public void editItem(int position) {
        FoodListFragment context = selectFood;
        Food food = getTypedItem(position);

        Intent intent;
        switch (foodType) {
            case DBContract.FoodEntry.TYPE_FOOD:
                intent = new Intent(context.getActivity(), AddFood.class);
                intent.putExtra("food_dbid", food.DBID);
                break;
            case DBContract.FoodEntry.TYPE_RECIPE:
                intent = new Intent(context.getActivity(), RecipeForm.class);
                intent.putExtra("recipe_dbid", food.DBID);
                intent.putExtra(Objective.INTENT_EXTRA_NAME, Objective.EDIT_RECIPE);
                break;
            default:
                return;
        }

        intent.putExtra("CALLED_FOR_RESULT", true);

        selectFood.startActivityForResult(intent, 1);
    }

    public void deleteItem(int position) {
        Food food = getTypedItem(position);
        MyCapNutrition.dataManager.foodManager.setActive(food.DBID, false);
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
        MyCapNutrition.dataManager.foodManager.setActive(food.DBID, true);
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
        holder.tvName.setText(getTypedItem(position).getName());
        holder.tvDetails.setText(getTypedItem(position).getKcal() + " kcal / " +
        getTypedItem(position).getReferenceServing_mg().toDoubleOrNA().divide(1000).round() +
        " g");

        holder.llContainer.setChecked(isItemChecked(position));

        return convertView;
    }

    public void search(String constraint) {
        clear();
        addAll(MyCapNutrition.dataManager.foodManager.getFoodMatches(constraint));
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getNumChecked() > 0) {
            super.onItemClick(parent, view, position, id);
        } else {
            Intent foundingIntent = ((SelectFood) view.getContext()).getIntent();
            Intent intent;

            switch (foundingIntent.getIntExtra(Objective.INTENT_EXTRA_NAME, -1)) {
                case Objective.LIST_UNITS:
                    intent = new Intent(selectFood.getActivity(), UnitList.class);
                    break;

                case Objective.SWITCH_RECORD_FOOD:
                case Objective.CREATE_RECORD:
                case Objective.CREATE_INGREDIENT:
                    intent = new Intent(selectFood.getActivity(), RecordView.class);
                    break;

                case Objective.LIST_FOODS:
                    return;

                default:
                    throw new RuntimeException("Unexpected objective: " +
                            foundingIntent.getIntExtra(Objective.INTENT_EXTRA_NAME, -1));
            }

            Food food = getTypedItem(position);
            intent.putExtras(((SelectFood) view.getContext()).getIntent());
            intent.putExtra("food_dbid", food.DBID);

            switch (foundingIntent.getIntExtra(Objective.INTENT_EXTRA_NAME, -1)) {

                // return a result
                case Objective.SWITCH_RECORD_FOOD:
                    selectFood.getActivity().setResult(Activity.RESULT_OK, intent);
                    selectFood.getActivity().finish();
                    break;

                // continue to next activity in flow
                case Objective.CREATE_RECORD:
                case Objective.LIST_UNITS:
                case Objective.CREATE_INGREDIENT:
                    selectFood.startActivity(intent);
                    break;

                // do nothing
                case Objective.LIST_FOODS:
                    break;
                default:
                    throw new RuntimeException("Unexpected objective: " +
                            foundingIntent.getIntExtra(Objective.INTENT_EXTRA_NAME, -1));
            }
        }
    }

}
