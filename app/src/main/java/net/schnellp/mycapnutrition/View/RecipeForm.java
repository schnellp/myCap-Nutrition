package net.schnellp.mycapnutrition.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.schnellp.mycapnutrition.R;

import java.util.ArrayList;

public class RecipeForm extends Activity {

    private LinearLayout formGroupContainer;

    private ArrayList<Integer> etFoodNameIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        formGroupContainer = (LinearLayout) findViewById(R.id.llFormGroupContainer);

        for (int i = 0; i < 3; i++) {
            View group = getLayoutInflater().inflate(R.layout.form_group_food_quantity_unit,
                    formGroupContainer);
            TextView tv = ((TextView) group.findViewById(R.id.tvFoodName));
            tv.setId(View.generateViewId());
            etFoodNameIDs.add(tv.getId());
            tv.setText("" + i);
        }
    }
}
