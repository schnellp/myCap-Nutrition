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


        FoodDataSource datasource = new FoodDataSource(this);
        datasource.open();
        Food food = datasource.createFood(name, sRefServing_mg, sKcal, sCarb_mg, sFat_mg, sProtein_mg);
        datasource.close();

        Intent intent = new Intent(this, RecordView.class);
        intent.putExtra("food_dbid", food.DBID);
        startActivity(intent);
    }
}
