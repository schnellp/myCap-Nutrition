package net.schnellp.mycapnutrition.Presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.Ingredient;
import net.schnellp.mycapnutrition.Model.Recipe;
import net.schnellp.mycapnutrition.MultiSelectListView.ActivatedLinearLayout;
import net.schnellp.mycapnutrition.MultiSelectListView.CheckableObject;
import net.schnellp.mycapnutrition.MultiSelectListView.MultiSelectAdapter;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.View.RecipeForm;
import net.schnellp.mycapnutrition.View.RecordView;

import java.util.ArrayList;
import java.util.Collections;

public class IngredientListAdapter extends MultiSelectAdapter<Ingredient> {

    private RecipeForm recipeForm;
    private LayoutInflater inflater;

    public IngredientListAdapter(Context context, Food recipe) {
        addAll(MyCapNutrition.dataManager.getIngredientsForRecipe(recipe));
        inflater = LayoutInflater.from(context);
        recipeForm = (RecipeForm) context;
    }

    public void editItem(int position) {
        RecipeForm context = recipeForm;
        Ingredient ingredient = getTypedItem(position);
        Intent intent = new Intent(context, RecordView.class);
        intent.putExtra("CALLED_FOR_RESULT", true);
        intent.putExtra("ingredient_dbid", ingredient.DBID);
        recipeForm.startActivityForResult(intent, 1);
    }

    public void deleteItem(int position) {
        Ingredient ingredient = getTypedItem(position);
        MyCapNutrition.dataManager.deactivateIngredient(ingredient);
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

    public void restoreItem(Ingredient ingredient) {
        MyCapNutrition.dataManager.restoreIngredient(ingredient);
        items.add(new CheckableObject<>(ingredient));
        notifyDataSetChanged();
    }

    public void restoreItems(ArrayList<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            restoreItem(ingredient);
        }
    }

    private class ViewHolder {
        ActivatedLinearLayout llContainer;
        TextView tvName, tvDetails;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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

        Ingredient ingredient = getTypedItem(position);
        holder.tvName.setText(ingredient.foodName);
        holder.tvDetails.setText(ingredient.quantity_cents.toDoubleOrNA().divide(100) +
                " x " + ingredient.unitName);

        holder.llContainer.setChecked(isItemChecked(position));

        return convertView;
    }
}
