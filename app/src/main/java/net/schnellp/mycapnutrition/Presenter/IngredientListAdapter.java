package net.schnellp.mycapnutrition.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.Ingredient;
import net.schnellp.mycapnutrition.MultiSelectListView.ActivatedLinearLayout;
import net.schnellp.mycapnutrition.MultiSelectListView.MultiSelectAdapter;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;

public class IngredientListAdapter extends MultiSelectAdapter<Ingredient> {

    private LayoutInflater inflater;

    public IngredientListAdapter(Context context, Food recipe) {
        addAll(MyCapNutrition.dataManager.getIngredientsForRecipe(recipe));
        inflater = LayoutInflater.from(context);
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
