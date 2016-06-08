package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.IntOrNA;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.Presenter.IngredientListAdapter;
import net.schnellp.mycapnutrition.R;

public class RecipeForm extends AppCompatActivity {

    private ListView lvIngredients;
    private IngredientListAdapter adapter;
    private Food recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        IntOrNA na = new IntOrNA();
        recipe = MyCapNutrition.dataManager.createFood("New Recipe", na,
                na, na, na, na,
                false);

        lvIngredients = (ListView) findViewById(R.id.lvIngredients);
        Button footer = new Button(this);
        footer.setText("Add Ingredient");
        footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectFood.class);
                intent.putExtra(SelectFood.Purpose.INTENT_EXTRA_NAME,
                        SelectFood.Purpose.CREATE_RECORD);
                startActivity(intent);
            }
        });
        lvIngredients.addFooterView(footer);

        adapter = new IngredientListAdapter(this, recipe);
        lvIngredients.setAdapter(adapter);
    }
}
