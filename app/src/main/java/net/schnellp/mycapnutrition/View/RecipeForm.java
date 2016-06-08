package net.schnellp.mycapnutrition.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;

import java.util.ArrayList;

public class RecipeForm extends Activity {

    private LinearLayout formGroupContainer;

    private ArrayList<Integer> tvFoodNameIDs = new ArrayList<>();
    private ArrayList<Integer> ibSwitchFoodIDs = new ArrayList<>();
    private ArrayList<Integer> etRecordServingIDs = new ArrayList<>();
    private ArrayList<Integer> spUnitIDs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        formGroupContainer = (LinearLayout) findViewById(R.id.llFormGroupContainer);

        for (int i = 1; i < 4; i++) {
            addInputGroup(MyCapNutrition.dataManager.getFood(i));
        }
    }

    private void addInputGroup(Food food) {
        View group = getLayoutInflater().inflate(R.layout.input_group_food_quantity_unit,
                formGroupContainer);

        // Food name
        TextView tv = ((TextView) group.findViewById(R.id.tvFoodName));
        tv.setId(View.generateViewId());
        tvFoodNameIDs.add(tv.getId());
        tv.setText(food.name);

        // Switch food
        ImageButton ib = ((ImageButton) group.findViewById(R.id.ibSwitchFood));
        ib.setId(View.generateViewId());
        ibSwitchFoodIDs.add(ib.getId());

        // Quantity
        EditText et = ((EditText) group.findViewById(R.id.etRecordServing));
        et.setId(View.generateViewId());
        etRecordServingIDs.add(et.getId());

        // Unit
        Spinner sp = ((Spinner) group.findViewById(R.id.spUnit));
        sp.setId(View.generateViewId());
        spUnitIDs.add(sp.getId());
    }
}
