package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Presenter.FoodSearchAdapter;
import net.schnellp.mycapnutrition.R;

public class SelectFood extends AppCompatActivity {

    public static class Purpose {
        public static final String INTENT_EXTRA_NAME = "SELECT_FOOD_PURPOSE";

        public static final String SWITCH_RECORD_FOOD = "SWITCH_RECORD_FOOD";
        public static final String FILTER_UNITS = "FILTER_UNITS";
        public static final String LIST = "LIST";
        public static final String CREATE_RECORD = "CREATE_RECORD";
    }

    private ListView listView;
    private FoodSearchAdapter foodSearchAdapter;

    private Food tempFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText editText = (EditText) findViewById(R.id.editTextFoodSearch);
        listView = (ListView) findViewById(R.id.listViewFoodResults);

        // Add Text Change Listener to EditText
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                foodSearchAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_food_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddFood.class);
                intent.putExtras(((SelectFood) view.getContext()).getIntent());
                startActivity(intent);
            }
        });

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listViewFoodResults) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_context_generic_edit_delete, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                foodSearchAdapter.editItem(info.position);
                return true;
            case R.id.delete:
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.clSelectFood), "Food deleted.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(findViewById(R.id.clSelectFood),
                                        "Food is restored!", Snackbar.LENGTH_SHORT);
                                SelectFood.this.foodSearchAdapter.restoreItem(tempFood);
                                snackbar1.show();
                            }
                        });
                snackbar.show();
                tempFood = (Food) foodSearchAdapter.getItem(info.position);
                foodSearchAdapter.deleteItem(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        foodSearchAdapter = new FoodSearchAdapter(this, SelectFood.this);
        listView.setAdapter(foodSearchAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
