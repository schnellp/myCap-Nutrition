package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.IntOrNA;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.Objective;
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
        recipe = MyCapNutrition.dataManager.createBlankRecipe();

        lvIngredients = (ListView) findViewById(R.id.lvIngredients);
        Button footer = new Button(this);
        footer.setText("Add Ingredient");
        footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectFood.class);
                intent.putExtra(Objective.INTENT_EXTRA_NAME,
                        Objective.CREATE_INGREDIENT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("recipe_dbid", recipe.DBID);
                startActivityForResult(intent, Objective.CREATE_INGREDIENT);
            }
        });
        lvIngredients.addFooterView(footer);

        adapter = new IngredientListAdapter(this, recipe);
        lvIngredients.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_submit:
                MyCapNutrition.dataManager.compileRecipe(recipe.DBID, new IntOrNA(1));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Objective.CREATE_INGREDIENT) {
            // Make sure the request was successful
            adapter = new IngredientListAdapter(this, recipe);
            lvIngredients.setAdapter(adapter);
        }
    }
}
