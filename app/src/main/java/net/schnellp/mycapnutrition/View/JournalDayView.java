package net.schnellp.mycapnutrition.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.schnellp.mycapnutrition.model.Conversion;
import net.schnellp.mycapnutrition.model.Record;
import net.schnellp.mycapnutrition.Objective;
import net.schnellp.mycapnutrition.R;
import net.schnellp.mycapnutrition.multiselect.MultiSelectActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JournalDayView extends AppCompatActivity
        implements MultiSelectActivity, ViewPager.OnPageChangeListener {

    private Menu optionsMenu;

    private JournalDayFragment tempFragment;
    private ArrayList<Record> tempRecords;
    private ArrayList<Integer> tempPositions;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public void updateAdapter() {
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_day_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String strDate = sdfDate.format(now);

        mViewPager.setCurrentItem(Conversion.dateToDayNumber(strDate) + 1, false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SelectFood.class);
                String date = Conversion.dayNumberToDate(mViewPager.getCurrentItem());
                intent.putExtra(Objective.INTENT_EXTRA_NAME,
                        Objective.CREATE_RECORD);
                intent.putExtra("DATE", date);
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
        this.optionsMenu = menu;
        getMenuInflater().inflate(R.menu.menu_journal_day_view, menu);
        setMultiSelectOptionsMenuVisible(false);
        setSingleSelectOptionsMenuVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()) {
            case R.id.action_edit:
                JournalDayFragment fragment = (JournalDayFragment) mSectionsPagerAdapter
                        .instantiateItem(mViewPager, mViewPager.getCurrentItem());
                fragment.adapter.editItem(fragment.adapter.getCheckedPositions().get(0));
                return true;
            case R.id.action_delete:
                Snackbar snackbar = Snackbar
                        .make(mViewPager, "Record(s) deleted.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(mViewPager,
                                        "Record(s) restored!", Snackbar.LENGTH_SHORT);
                                tempFragment.adapter.restoreItems(tempRecords);
                                snackbar1.show();
                            }
                        });

                tempFragment = (JournalDayFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
                tempRecords = tempFragment.adapter.getCheckedItems();
                tempPositions = tempFragment.adapter.getCheckedPositions();
                tempFragment.adapter.deleteCheckedItems();
                snackbar.show();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void pageBack(View v) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    public void pageForward(View v) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    @Override
    public void setSingleSelectOptionsMenuVisible(boolean visible) {
        if (optionsMenu != null) {
            optionsMenu.setGroupVisible(R.id.menu_options_single_select_group, visible);
        }
    }

    @Override
    public void setMultiSelectOptionsMenuVisible(boolean visible) {
        if (optionsMenu != null) {
            optionsMenu.setGroupVisible(R.id.menu_options_multi_select_group, visible);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        TextView pagerTitle = (TextView) findViewById(R.id.pager_title);
        pagerTitle.setText(Conversion.dayNumberToLongRelativeDate(position));

        JournalDayFragment offScreenFragment = (JournalDayFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem() + 1);
        if (offScreenFragment != null && offScreenFragment.adapter != null) {
            offScreenFragment.adapter.uncheckAll();
        }

        offScreenFragment = (JournalDayFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem() - 1);
        if (offScreenFragment != null && offScreenFragment.adapter != null) {
            offScreenFragment.adapter.uncheckAll();
        }

        setSingleSelectOptionsMenuVisible(false);
        setMultiSelectOptionsMenuVisible(false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            JournalDayFragment fragment = new JournalDayFragment();
            Bundle args = new Bundle();
            args.putString(JournalDayFragment.DATE, Conversion.dayNumberToDate(position));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 365608;
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Conversion.dayNumberToDate(position);
            // return ((JournalDayFragment) getItem(position)).getDate();
        }
    }
}
