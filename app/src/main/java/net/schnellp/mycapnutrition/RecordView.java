package net.schnellp.mycapnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class RecordView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        Intent intent = getIntent();
        int foodDBID = intent.getIntExtra("food_dbid", -1);
        TextView tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        tvFoodName.setText("" + foodDBID);
    }

}
