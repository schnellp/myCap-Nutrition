package net.schnellp.mycapnutrition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.schnellp.mycapnutrition.data.Food;
import net.schnellp.mycapnutrition.data.DataManager;

import java.util.ArrayList;

public class SelectFood extends AppCompatActivity {

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
            inflater.inflate(R.menu.menu_context_select_food_view, menu);
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
                                SelectFood.this.foodSearchAdapter.addItem(tempFood);
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

        foodSearchAdapter = new FoodSearchAdapter(SelectFood.this);
        listView.setAdapter(foodSearchAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public class FoodSearchAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Food> foodsOriginalValues = new ArrayList<>();
        private ArrayList<Food> foodsDisplayedValues = new ArrayList<>();
        LayoutInflater inflater;
        DataManager datasource;

        public FoodSearchAdapter(Context context) {
            foodsOriginalValues.addAll(MyCapNutrition.dataManager.getAllFoods());
            foodsDisplayedValues.addAll(MyCapNutrition.dataManager.getAllFoods());
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return foodsDisplayedValues.size();
        }

        @Override
        public Object getItem(int position) {
            return foodsDisplayedValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void editItem(int position) {
            SelectFood context = SelectFood.this;
            Food food = foodsDisplayedValues.get(position);
            Intent intent = new Intent(context, AddFood.class);
            intent.putExtra("CALLED_FOR_RESULT", true);
            intent.putExtra("food_dbid", food.DBID);
            startActivityForResult(intent, 1);
        }

        public void addItem(Food food) {
            MyCapNutrition.dataManager.restoreFood(food);
            foodsOriginalValues.add(food);
            foodSearchAdapter.getFilter().filter("");
            notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            Food food = foodsDisplayedValues.get(position);
            MyCapNutrition.dataManager.deleteFood(food);
            foodsOriginalValues.remove(position);
            foodSearchAdapter.getFilter().filter("");
            notifyDataSetChanged();
        }

        private class ViewHolder {
            LinearLayout llContainer;
            TextView tvName,tvDetails;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.foodrow, null);
                holder.llContainer = (LinearLayout) convertView.findViewById(R.id.selectfoodrow);
                holder.tvName = (TextView) convertView.findViewById(R.id.foodrowName);
                holder.tvDetails = (TextView) convertView.findViewById(R.id.foodrowDetails);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(foodsDisplayedValues.get(position).name);
            holder.tvDetails.setText(foodsDisplayedValues.get(position).kcal+"");


            holder.llContainer.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent intent = new Intent(SelectFood.this, RecordView.class);
                    Food food = foodsDisplayedValues.get(position);
                    intent.putExtra("food_dbid", food.DBID);
                    intent.putExtras(((SelectFood) v.getContext()).getIntent());

                    if (((SelectFood) v.getContext()).getIntent().getBooleanExtra("CALLED_FOR_RESULT", false)) {
                        setResult(RESULT_OK,intent);
                        finish();
                    } else {
                        startActivity(intent);
                    }
                }
            });

            /*
            holder.llContainer.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    v.setBackgroundColor(ContextCompat.getColor(v.getContext(), android.R.color.holo_purple));

                    return true;
                }
            });
            */

            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,FilterResults results) {

                    foodsDisplayedValues = (ArrayList<Food>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<Food> FilteredArrList = new ArrayList<>();

                    if (foodsOriginalValues == null) {
                        foodsOriginalValues = new ArrayList<>(foodsDisplayedValues); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = foodsOriginalValues.size();
                        results.values = foodsOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < foodsOriginalValues.size(); i++) {
                            String data = foodsOriginalValues.get(i).name;
                            if (data.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(foodsOriginalValues.get(i));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }

    }

}
