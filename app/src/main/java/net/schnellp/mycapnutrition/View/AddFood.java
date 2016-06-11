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
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.R;

public class AddFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText finalField = (EditText) findViewById(R.id.etProtein);

        finalField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addFood(v);
                    return true;
                }
                return false;
            }
        });

        int dbid = getIntent().getIntExtra("food_dbid", -1);
        if (dbid != -1) {
            Food food = MyCapNutrition.dataManager.getFood(dbid);

            ((EditText) findViewById(R.id.etName)).setText(food.getName());
            ((EditText) findViewById(R.id.etRefServing)).setText(food.getReferenceServing_mg().toDoubleOrNA().divide(1000).toString());
            ((EditText) findViewById(R.id.etKcal)).setText(food.getKcal().toString());
            ((EditText) findViewById(R.id.etCarb)).setText(food.getCarb_mg().toDoubleOrNA().divide(1000).toString());
            ((EditText) findViewById(R.id.etFat)).setText(food.getFat_mg().toDoubleOrNA().divide(1000).toString());
            ((EditText) findViewById(R.id.etProtein)).setText(food.getProtein_mg().toDoubleOrNA().divide(1000).toString());
        }
    }

    private DoubleOrNA getDoubleOrNAFromForm(int id) {
        String string = ((EditText) findViewById(id)).getText().toString();
        if (!string.equals("") && !string.equals("NA")) {
            return new DoubleOrNA(Double.parseDouble(string), false);
        } else {
            return new DoubleOrNA(0, true);
        }
    }

    public void addFood(View view) {


        String name = ((EditText) findViewById(R.id.etName)).getText().toString();

        IntOrNA iRefServing_mg = getDoubleOrNAFromForm(R.id.etRefServing).multiply(1000).round();
        IntOrNA iKcal = getDoubleOrNAFromForm(R.id.etKcal).round();
        IntOrNA iCarb_mg = getDoubleOrNAFromForm(R.id.etCarb).multiply(1000).round();
        IntOrNA iFat_mg = getDoubleOrNAFromForm(R.id.etFat).multiply(1000).round();
        IntOrNA iProtein_mg = getDoubleOrNAFromForm(R.id.etProtein).multiply(1000).round();

        if (getIntent().getBooleanExtra("CALLED_FOR_RESULT", false)) {
            MyCapNutrition.dataManager.updateFood(getIntent().getIntExtra("food_dbid", -1), name, iRefServing_mg,
                    iKcal, iCarb_mg, iFat_mg, iProtein_mg);

            Intent intent = new Intent(this, SelectFood.class);
            setResult(RESULT_OK,intent);
            finish();
        } else {
            Food food = MyCapNutrition.dataManager.createFood(name, iRefServing_mg,
                    iKcal, iCarb_mg, iFat_mg, iProtein_mg);

            Intent intent = new Intent(this, RecordView.class);
            intent.putExtras(getIntent());
            intent.putExtra("food_dbid", food.DBID);
            startActivity(intent);
        }
    }
}
