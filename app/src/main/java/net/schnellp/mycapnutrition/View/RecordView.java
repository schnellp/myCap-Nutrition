package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.DoubleOrNA;
import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.Ingredient;
import net.schnellp.mycapnutrition.Model.IntOrNA;
import net.schnellp.mycapnutrition.Model.Record;
import net.schnellp.mycapnutrition.Model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.Objective;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.Presenter.UnitSpinnerAdapter;

public class RecordView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int SELECT_FOOD_RESULT = 1;
    private static final int ADD_UNIT_RESULT = 2;

    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();


        if (intent.hasExtra("record_dbid")) {
            Record record = MyCapNutrition.dataManager.getRecord(
                    intent.getIntExtra("record_dbid", -1));

            food = MyCapNutrition.dataManager.getFood(record.foodID);
            TextView tv = (TextView) findViewById(R.id.tvFoodName);
            tv.setText(food.getName());

            Unit unit = MyCapNutrition.dataManager.getUnit(record.unitID);
            UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(this, food);
            Spinner spinner = (Spinner) findViewById(R.id.spUnit);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            spinner.setSelection(((UnitSpinnerAdapter) spinner.getAdapter()).getPosition(unit));

            ((EditText) findViewById(R.id.etRecordServing)).setText(
                    record.quantity_cents.toDoubleOrNA().divide(100).round().toString());
        } else if (intent.hasExtra("ingredient_dbid")) {
            Ingredient ingredient = MyCapNutrition.dataManager.getIngredient(
                    intent.getIntExtra("ingredient_dbid", -1));
            food = MyCapNutrition.dataManager.getFood(ingredient.food_id);
            TextView tv = (TextView) findViewById(R.id.tvFoodName);
            tv.setText(food.getName());

            Unit unit = MyCapNutrition.dataManager.getUnit(ingredient.unit_id);
            UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(this, food);
            Spinner spinner = (Spinner) findViewById(R.id.spUnit);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            spinner.setSelection(((UnitSpinnerAdapter) spinner.getAdapter()).getPosition(unit));

            ((EditText) findViewById(R.id.etRecordServing)).setText(
                    ingredient.quantity_cents.toDoubleOrNA().divide(100).round().toString());

        } else {
            food = MyCapNutrition.dataManager.getFood(intent.getIntExtra("food_dbid", -1));
            TextView tv = (TextView) findViewById(R.id.tvFoodName);
            tv.setText(food.getName());

            UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(this, food);
            Spinner spinner = (Spinner) findViewById(R.id.spUnit);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(this, food);
        Spinner spinner = (Spinner) findViewById(R.id.spUnit);
        spinner.setAdapter(adapter);
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
                food = MyCapNutrition.dataManager.getFood(data.getIntExtra("food_dbid", -1));

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
