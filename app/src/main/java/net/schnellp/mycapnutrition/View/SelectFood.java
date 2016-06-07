package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Presenter.FoodSearchAdapter;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.MultiSelectListView.MultiSelectActivity;

import java.util.ArrayList;

public class SelectFood extends AppCompatActivity implements MultiSelectActivity {

    public static class Purpose {
        public static final String INTENT_EXTRA_NAME = "SELECT_FOOD_PURPOSE";

        public static final String SWITCH_RECORD_FOOD = "SWITCH_RECORD_FOOD";
        public static final String FILTER_UNITS = "FILTER_UNITS";
        public static final String LIST = "LIST";
        public static final String CREATE_RECORD = "CREATE_RECORD";
    }

    private ListView listView;
    private FoodSearchAdapter adapter;
    private Menu optionsMenu;

    private ArrayList<Food> tempFoods;
    private ArrayList<Integer> tempPositions;

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
                adapter.getFilter().filter(s.toString());
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

        // registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_multi_select, menu);
        this.optionsMenu = menu;
        setSingleSelectOptionsMenuVisible(false);
        setMultiSelectOptionsMenuVisible(false);
        return true;
    }

    @Override
    public void setSingleSelectOptionsMenuVisible(boolean visible) {
        optionsMenu.setGroupVisible(R.id.menu_options_single_select_group, visible);
    }

    @Override
    public void setMultiSelectOptionsMenuVisible(boolean visible) {
        optionsMenu.setGroupVisible(R.id.menu_options_multi_select_group, visible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete:
                Snackbar snackbar = Snackbar
                        .make(listView, "Food(s) deleted.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(listView,
                                        "Food(s) restored!", Snackbar.LENGTH_SHORT);
                                SelectFood.this.adapter.restoreItems(tempFoods, tempPositions);
                                snackbar1.show();
                            }
                        });
                tempFoods = adapter.getCheckedItems();
                tempPositions = adapter.getCheckedPositions();
                adapter.deleteCheckedItems();
                snackbar.show();
                return true;
            case R.id.action_edit:
                adapter.editItem(adapter.getCheckedPositions().get(0));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new FoodSearchAdapter(this, SelectFood.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
        listView.setOnItemLongClickListener(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
