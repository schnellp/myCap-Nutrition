package net.schnellp.mycapnutrition.View;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.Presenter.UnitListAdapter;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.View.MultiSelectListView.MultiSelectActivity;

import java.util.ArrayList;

public class UnitList extends AppCompatActivity implements MultiSelectActivity {

    private ListView listView;
    private UnitListAdapter adapter;
    private Menu optionsMenu;

    private ArrayList<Unit> tempUnits;
    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.lvUnits);
        // registerForContextMenu(listView);

        food = MyCapNutrition.dataManager.getFood(getIntent().getIntExtra("food_dbid", -1));
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.action_delete:
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.clUnitList), "Unit(s) deleted.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(findViewById(R.id.clUnitList),
                                        "Unit(s) restored!", Snackbar.LENGTH_SHORT);
                                UnitList.this.adapter.restoreItems(tempUnits);
                                snackbar1.show();
                            }
                        });
                tempUnits = adapter.getCheckedItems();
                adapter.deleteCheckedItems();
                snackbar.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new UnitListAdapter(UnitList.this, food);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
