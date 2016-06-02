package net.schnellp.mycapnutrition.View;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
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

public class UnitList extends AppCompatActivity {

    private ListView listView;
    private UnitListAdapter adapter;

    private Unit tempUnit;
    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.lvUnits);
        registerForContextMenu(listView);

        food = MyCapNutrition.dataManager.getFood(getIntent().getIntExtra("food_dbid", -1));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lvUnits) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_context_generic_delete, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.clUnitList), "Unit deleted.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(findViewById(R.id.clUnitList),
                                        "Unit is restored!", Snackbar.LENGTH_SHORT);
                                UnitList.this.adapter.restoreItem(tempUnit);
                                snackbar1.show();
                            }
                        });
                snackbar.show();

                tempUnit = (Unit) adapter.getItem(info.position);
                adapter.deleteItem(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
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
