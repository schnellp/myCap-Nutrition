package net.schnellp.mycapnutrition.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.schnellp.mycapnutrition.model.DoubleOrNA;
import net.schnellp.mycapnutrition.model.Food;
import net.schnellp.mycapnutrition.model.Ingredient;
import net.schnellp.mycapnutrition.model.IntOrNA;
import net.schnellp.mycapnutrition.model.Record;
import net.schnellp.mycapnutrition.model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.Objective;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.presenter.UnitSpinnerAdapter;
import net.schnellp.mycapnutrition.view.util.OptionsMenuUtil;

public class RecordView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int SELECT_FOOD_RESULT = 1;
    private static final int ADD_UNIT_RESULT = 2;

    private Food food;
    private Unit unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText finalField = (EditText) findViewById(R.id.etRecordServing);

        finalField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveRecord(v);
                    return true;
                }
                return false;
            }
        });

        Intent intent = getIntent();

        if (intent.hasExtra("record_dbid")) {
            Record record = MyCapNutrition.dataManager.getRecord(
                    intent.getIntExtra("record_dbid", -1));

            food = MyCapNutrition.dataManager.foodManager.get(record.foodID);
            TextView tv = (TextView) findViewById(R.id.tvFoodName);
            tv.setText(food.getName());

            unit = MyCapNutrition.dataManager.getUnit(record.unitID);

            ((EditText) findViewById(R.id.etRecordServing)).setText(
                    record.quantity_cents.toDoubleOrNA().divide(100).toString());
        } else if (intent.hasExtra("ingredient_dbid")) {
            Ingredient ingredient = MyCapNutrition.dataManager.getIngredient(
                    intent.getIntExtra("ingredient_dbid", -1));
            food = MyCapNutrition.dataManager.foodManager.get(ingredient.food_id);
            TextView tv = (TextView) findViewById(R.id.tvFoodName);
            tv.setText(food.getName());

            unit = MyCapNutrition.dataManager.getUnit(ingredient.unit_id);

            ((EditText) findViewById(R.id.etRecordServing)).setText(
                    ingredient.quantity_cents.toDoubleOrNA().divide(100).toString());

        } else {
            food = MyCapNutrition.dataManager.foodManager.get(intent.getIntExtra("food_dbid", -1));
            TextView tv = (TextView) findViewById(R.id.tvFoodName);
            tv.setText(food.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(this, food);
        Spinner spinner = (Spinner) findViewById(R.id.spUnit);
        spinner.setAdapter(adapter);

        if (unit != null) {
            int targetIndex = ((UnitSpinnerAdapter) spinner.getAdapter()).getPosition(unit);
            spinner.setSelection(targetIndex, true);
        }
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_submit, menu);
        OptionsMenuUtil.tintMenuItems(this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_submit:
                saveRecord(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveRecord(View v) {

        Intent foundingIntent = getIntent();

        EditText et = (EditText) findViewById(R.id.etRecordServing);
        IntOrNA quantity_cents = (new DoubleOrNA(et.getText().toString())).multiply(100).round();

        Spinner unitSpinner = (Spinner) findViewById(R.id.spUnit);
        Unit unit = ((Unit) unitSpinner.getSelectedItem());

        switch (foundingIntent.getIntExtra(Objective.INTENT_EXTRA_NAME, -1)) {
            case Objective.CREATE_RECORD:
                String date = getIntent().getStringExtra("DATE");
                MyCapNutrition.dataManager.createRecord(date, food, quantity_cents, unit);

                Intent intent = new Intent(this, JournalDayView.class);
                intent.putExtra("DATE", date);
                startActivity(intent);
                break;
            case Objective.EDIT_RECORD:
                MyCapNutrition.dataManager.updateRecord(getIntent().getIntExtra("record_dbid", -1),
                        food, quantity_cents, unit);
                finish();
                break;
            case Objective.CREATE_INGREDIENT:
                int recipeID = foundingIntent.getIntExtra("recipe_dbid", -1);
                Ingredient ing = MyCapNutrition.dataManager.createIngredient(recipeID, food.DBID, unit.DBID,
                        quantity_cents);
                System.out.println("ing.recipe_id: " + ing.recipe_id);
                System.out.println("ing.DBID: " + ing.DBID);
                setResult(RecipeForm.RESULT_OK, null);
                finish();
                break;
            case Objective.EDIT_INGREDIENT:
                MyCapNutrition.dataManager.updateIngredient(
                        getIntent().getIntExtra("ingredient_dbid", -1),
                        food, quantity_cents, unit);
                finish();
                break;
            default:
                throw new RuntimeException("Unexpected objective: " +
                        foundingIntent.getIntExtra(Objective.INTENT_EXTRA_NAME, -1));
        }
    }

    public void switchFood(View v) {
        Intent intent = new Intent(this, SelectFood.class);
        intent.putExtra(Objective.INTENT_EXTRA_NAME,
                Objective.SWITCH_RECORD_FOOD);
        startActivityForResult(intent, SELECT_FOOD_RESULT);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Unit unit = (Unit) parent.getItemAtPosition(pos);
        if (unit == Unit.ADD) {
            Intent intent = new Intent(this, AddUnit.class);
            intent.putExtras(getIntent());
            intent.putExtra("food_dbid", food.DBID);
            startActivityForResult(intent, ADD_UNIT_RESULT);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_FOOD_RESULT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                food = MyCapNutrition.dataManager.foodManager.get(data.getIntExtra("food_dbid", -1));

                TextView tv = (TextView) findViewById(R.id.tvFoodName);
                tv.setText(food.getName());
            }
        } else if (requestCode == ADD_UNIT_RESULT &&
                data != null &&
                data.hasExtra("unit_dbid")) {
            Unit unit = MyCapNutrition.dataManager.getUnit(data.getIntExtra("unit_dbid", -1));
            Spinner unitSpinner = (Spinner) findViewById(R.id.spUnit);
            int spinnerPosition = ((UnitSpinnerAdapter) unitSpinner.getAdapter()).getPosition(unit);
            unitSpinner.setSelection(spinnerPosition);
        }
    }

}
