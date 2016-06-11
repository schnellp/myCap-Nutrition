package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import net.schnellp.mycapnutrition.Model.DoubleOrNA;
import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.IntOrNA;
import net.schnellp.mycapnutrition.Model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;

public class AddUnit extends AppCompatActivity {

    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int dbid = intent.getIntExtra("unit_dbid", -1);
        if (dbid != -1) {
            Unit unit = MyCapNutrition.dataManager.getUnit(dbid);
            food = MyCapNutrition.dataManager.foodManager.get(unit.foodID);

            ((EditText) findViewById(R.id.etUnitName)).setText(unit.name);
            ((EditText) findViewById(R.id.etUnitAmount)).setText(unit.amount_mg.toDoubleOrNA().divide(1000).toString());
        } else {
            food = MyCapNutrition.dataManager.foodManager.get(intent.getIntExtra("food_dbid", -1));
        }

        TextView tv = (TextView) findViewById(R.id.tvUnitFoodName);
        tv.setText(food.getName());

        EditText finalField = (EditText) findViewById(R.id.etUnitAmount);
        finalField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveUnit(v);
                    return true;
                }
                return false;
            }
        });
    }

    private DoubleOrNA getDoubleOrNAFromForm(int id) {
        String string = ((EditText) findViewById(id)).getText().toString();
        if (!string.equals("")) {
            return new DoubleOrNA(Double.parseDouble(string), false);
        } else {
            return new DoubleOrNA(0, true);
        }
    }

    public void saveUnit(View v) {
        String name = ((EditText) findViewById(R.id.etUnitName)).getText().toString();

        IntOrNA iAmount_mg = getDoubleOrNAFromForm(R.id.etUnitAmount).multiply(1000).round();

        if (getIntent().getBooleanExtra("CALLED_FOR_RESULT", false)) {
            MyCapNutrition.dataManager.updateUnit(getIntent().getIntExtra("unit_dbid", -1),
                    food, name, iAmount_mg);

            Intent intent = new Intent(this, UnitList.class);
            setResult(RESULT_OK,intent);
            finish();
        } else {
            Unit unit = MyCapNutrition.dataManager.createUnit(food, name, iAmount_mg);
            Intent intent = new Intent(this, RecordView.class);
            intent.putExtra("unit_dbid", unit.DBID);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void switchFood(View v) {

    }

}
