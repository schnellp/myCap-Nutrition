package net.schnellp.mycapnutrition.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import net.schnellp.mycapnutrition.model.Food;
import net.schnellp.mycapnutrition.model.Unit;
import net.schnellp.mycapnutrition.MyCapNutrition;
import net.schnellp.mycapnutrition.presenter.UnitListAdapter;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.multiselect.MultiSelectActivity;
import net.schnellp.mycapnutrition.view.util.OptionsMenuUtil;

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

        food = MyCapNutrition.dataManager.foodManager.get(getIntent().getIntExtra("food_dbid", -1));
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new UnitListAdapter(UnitList.this, food);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
        listView.setOnItemLongClickListener(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_multi_select, menu);
        this.optionsMenu = menu;
        OptionsMenuUtil.tintMenuItems(this, menu);
        setSingleSelectOptionsMenuVisible(false);
        setMultiSelectOptionsMenuVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            case R.id.action_edit:
                adapter.editItem(adapter.getCheckedPositions().get(0));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setSingleSelectOptionsMenuVisible(boolean visible) {
        optionsMenu.setGroupVisible(R.id.menu_options_single_select_group, visible);
    }

    @Override
    public void setMultiSelectOptionsMenuVisible(boolean visible) {
        optionsMenu.setGroupVisible(R.id.menu_options_multi_select_group, visible);
    }
}
