package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.DoubleOrNA;
import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.DataManager;
import net.schnellp.mycapnutrition.Model.IntOrNA;
import net.schnellp.mycapnutrition.Model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.Presenter.UnitSpinnerAdapter;

import java.util.ArrayList;

public class RecordView extends AppCompatActivity {

    private DataManager datasource;
    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("RecordView " + getIntent().getExtras().get("DATE"));

        setContentView(R.layout.activity_record_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        food = MyCapNutrition.dataManager.getFood(intent.getIntExtra("food_dbid", -1));

        TextView tv = (TextView) findViewById(R.id.tvFoodName);
        tv.setText(food.name);

        ArrayList<Unit> units = new ArrayList<>();
        units.add(Unit.G);
        // add from db
        // units.add(Unit.ADD);
        UnitSpinnerAdapter unitSpinnerAdapter = new UnitSpinnerAdapter(this, units);
        Spinner spinner = (Spinner) findViewById(R.id.spUnit);
        spinner.setAdapter(unitSpinnerAdapter);
    }

    public void saveRecord(View v) {
        EditText et = (EditText) findViewById(R.id.etRecordServing);
        String date = getIntent().getStringExtra("DATE");
        IntOrNA quantity_cents = (new DoubleOrNA(et.getText().toString())).multiply(100).round();
        Unit unit = Unit.G;
        MyCapNutrition.dataManager.createRecord(date, food, quantity_cents, unit);

        Intent intent = new Intent(this, JournalDayView.class);
        intent.putExtra("DATE", date);
        startActivity(intent);
    }

    public void switchFood(View v) {
        Intent intent = new Intent(this, SelectFood.class);
        intent.putExtra("CALLED_FOR_RESULT", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                food = MyCapNutrition.dataManager.getFood(data.getIntExtra("food_dbid", -1));

                TextView tv = (TextView) findViewById(R.id.tvFoodName);
                tv.setText(food.name);
            }
        }
    }

}
