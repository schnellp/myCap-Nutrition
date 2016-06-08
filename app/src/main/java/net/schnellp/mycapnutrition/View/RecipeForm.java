package net.schnellp.mycapnutrition.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import net.schnellp.mycapnutrition.R;

public class RecipeForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        ListView lvIngredients = (ListView) findViewById(R.id.lvIngredients);
        TextView footer = new TextView(this);
        footer.setText("test");
        lvIngredients.addFooterView(footer);
    }
}
