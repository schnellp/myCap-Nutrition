package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import net.schnellp.mycapnutrition.Model.DBContract;
import net.schnellp.mycapnutrition.Model.Food;
import net.schnellp.mycapnutrition.Objective;
import net.schnellp.mycapnutrition.Presenter.FoodSearchAdapter;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.MultiSelectListView.MultiSelectActivity;

import java.util.ArrayList;

public class SelectFood extends AppCompatActivity implements MultiSelectActivity {

    private static class Page {
        public static final int FOOD = 0;
        public static final int RECIPES = 1;
    }

    private Menu optionsMenu;

    private FoodListFragment tempFragment;
    private ArrayList<Food> tempFoods;
    private ArrayList<Integer> tempPositions;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public void updateAdapter() {
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0, false);

        EditText editText = (EditText) findViewById(R.id.editTextFoodSearch);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FoodListFragment fragment = (FoodListFragment) mSectionsPagerAdapter
                        .instantiateItem(mViewPager, mViewPager.getCurrentItem());
                fragment.adapter.getFilter().filter(s.toString());
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
                Intent intent;
                switch (SelectFood.this.mViewPager.getCurrentItem()) {
                    case Page.FOOD:
                        intent = new Intent(view.getContext(), AddFood.class);
                        break;
                    case Page.RECIPES:
                        intent = new Intent(view.getContext(), RecipeForm.class);
                        intent.putExtra(Objective.INTENT_EXTRA_NAME, Objective.CREATE_RECIPE);
                        break;
                    default:
                        return;
                }
                intent.putExtras(((SelectFood) view.getContext()).getIntent());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_multi_select, menu);
        this.optionsMenu = menu;
        setSingleSelectOptionsMenuVisible(false);
        setMultiSelectOptionsMenuVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete:
                Snackbar snackbar = Snackbar
                        .make(mViewPager, "Food(s) deleted.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(mViewPager,
                                        "Food(s) restored!", Snackbar.LENGTH_SHORT);
                                tempFragment.adapter.restoreItems(tempFoods, tempPositions);
                                snackbar1.show();
                            }
                        });
                tempFragment = (FoodListFragment) mSectionsPagerAdapter.instantiateItem(
                        mViewPager, mViewPager.getCurrentItem());
                tempFoods = tempFragment.adapter.getCheckedItems();
                tempPositions = tempFragment.adapter.getCheckedPositions();
                tempFragment.adapter.deleteCheckedItems();
                snackbar.show();
                return true;
            case R.id.action_edit:
                FoodListFragment fragment = (FoodListFragment) mSectionsPagerAdapter
                        .instantiateItem(mViewPager, mViewPager.getCurrentItem());
                fragment.adapter.editItem(fragment.adapter.getCheckedPositions().get(0));
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

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            FoodListFragment fragment = FoodListFragment.newInstance(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Food";
                case 1:
                    return "Recipes";
                default:
                    return null;
            }
        }
    }
}
