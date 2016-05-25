package net.schnellp.mycapnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.schnellp.mycapnutrition.data.Food;
import net.schnellp.mycapnutrition.data.FoodDataSource;

public class AddFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int dbid = getIntent().getIntExtra("food_dbid", -1);
        if (dbid != -1) {
            FoodDataSource datasource = new FoodDataSource(this);
            datasource.open();
            Food food = datasource.getFood(dbid);
            datasource.close();

            ((EditText) findViewById(R.id.etName)).setText(food.name);
            ((EditText) findViewById(R.id.etRefServing)).setText(food.referenceServing_mg.toDoubleOrNA().divide(1000).toString());
            ((EditText) findViewById(R.id.etKcal)).setText(food.kcal.toString());
            ((EditText) findViewById(R.id.etCarb)).setText(food.carb_mg.toDoubleOrNA().divide(1000).toString());
            ((EditText) findViewById(R.id.etFat)).setText(food.fat_mg.toDoubleOrNA().divide(1000).toString());
            ((EditText) findViewById(R.id.etProtein)).setText(food.protein_mg.toDoubleOrNA().divide(1000).toString());
        }
    }

    private String getNumericStringFromForm(int id) {
        String string = ((EditText) findViewById(id)).getText().toString();
        if (!string.equals("")) {
            return "" + Math.round(Double.parseDouble(string) * 1000);
        } else {
            return "null";
        }
    }

    public void addFood(View view) {


        String name = ((EditText) findViewById(R.id.etName)).getText().toString();

        String sRefServing_mg = getNumericStringFromForm(R.id.etRefServing);

        String sKcal = ((EditText) findViewById(R.id.etKcal)).getText().toString();
        if (!sKcal.equals("")) {
            sKcal = "" + Math.round(Double.parseDouble(sKcal));
        } else {
            sKcal = "null";
        }

        String sCarb_mg = getNumericStringFromForm(R.id.etCarb);
        String sFat_mg = getNumericStringFromForm(R.id.etFat);
        String sProtein_mg = getNumericStringFromForm(R.id.etProtein);

        if (getIntent().getBooleanExtra("CALLED_FOR_RESULT", false)) {
            FoodDataSource datasource = new FoodDataSource(this);
            datasource.open();
            datasource.updateFood(getIntent().getIntExtra("food_dbid", -1), name, sRefServing_mg, sKcal, sCarb_mg, sFat_mg, sProtein_mg);
            datasource.close();

            Intent intent = new Intent(this, SelectFood.class);
            setResult(RESULT_OK,intent);
            finish();
        } else {
            FoodDataSource datasource = new FoodDataSource(this);
            datasource.open();
            Food food = datasource.createFood(name, sRefServing_mg, sKcal, sCarb_mg, sFat_mg, sProtein_mg);
            datasource.close();

            Intent intent = new Intent(this, RecordView.class);
            intent.putExtra("food_dbid", food.DBID);
            startActivity(intent);
        }
    }
}
