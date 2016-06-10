package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.Ingredient;
import net.schnellp.mycapnutrition.Model.IntOrNA;
import net.schnellp.mycapnutrition.MultiSelectListView.MultiSelectActivity;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.Objective;
import net.schnellp.mycapnutrition.Presenter.IngredientListAdapter;
import net.schnellp.mycapnutrition.R;

import java.util.ArrayList;

public class RecipeForm extends AppCompatActivity implements MultiSelectActivity {

    private ListView lvIngredients;
    private IngredientListAdapter adapter;
    private Menu optionsMenu;

    private ArrayList<Ingredient> tempIngredients;
    private Food recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        lvIngredients = (ListView) findViewById(R.id.lvIngredients);

        ViewGroup header = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.recipe_form_header, null);
        lvIngredients.addHeaderView(header);

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

        int objective = getIntent().getIntExtra(Objective.INTENT_EXTRA_NAME, -1);
        switch (objective) {
            case Objective.CREATE_RECIPE:
                recipe = MyCapNutrition.dataManager.createBlankRecipe();
                break;
            case Objective.EDIT_RECIPE:
                recipe = MyCapNutrition.dataManager.getFood(
                        getIntent().getIntExtra("recipe_dbid", -1));
                ((EditText) findViewById(R.id.recipe_name)).setText(recipe.name);
                ((EditText) findViewById(R.id.recipe_servings)).setText(recipe.servings.toString());
                break;
            default:
                throw new RuntimeException("Invalid objective: " + objective);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new IngredientListAdapter(this, recipe);
        adapter.headerCount = 1;
        lvIngredients.setAdapter(adapter);
        lvIngredients.setOnItemClickListener(adapter);
        lvIngredients.setOnItemLongClickListener(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_multi_select, menu);
        getMenuInflater().inflate(R.menu.menu_options_submit, menu);
        this.optionsMenu = menu;
        setSingleSelectOptionsMenuVisible(false);
        setMultiSelectOptionsMenuVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_edit:
                adapter.editItem(adapter.getCheckedPositions().get(0));
                return true;
            case R.id.action_delete:
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.clRecipeForm), "Ingredient(s) deleted.",
                                Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar
                                        .make(findViewById(R.id.clRecipeForm),
                                                "Ingredient(s) restored!",
                                                Snackbar.LENGTH_SHORT);
                                RecipeForm.this.adapter.restoreItems(tempIngredients);
                                snackbar1.show();
                            }
                        });
                tempIngredients = adapter.getCheckedItems();
                adapter.deleteCheckedItems();
                snackbar.show();
                return true;
            case R.id.action_submit:
                String name = ((EditText) findViewById(R.id.recipe_name)).getText().toString();
                IntOrNA servings = new IntOrNA(((EditText) findViewById(R.id.recipe_servings))
                        .getText().toString());
                MyCapNutrition.dataManager.compileRecipe(recipe.DBID, name, servings);
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

    @Override
    public void setSingleSelectOptionsMenuVisible(boolean visible) {
        optionsMenu.setGroupVisible(R.id.menu_options_single_select_group, visible);
    }

    @Override
    public void setMultiSelectOptionsMenuVisible(boolean visible) {
        optionsMenu.setGroupVisible(R.id.menu_options_multi_select_group, visible);
    }
}
