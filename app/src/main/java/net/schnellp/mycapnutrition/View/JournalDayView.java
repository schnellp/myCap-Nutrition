package net.schnellp.mycapnutrition.View;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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

import net.schnellp.mycapnutrition.Model.Conversion;
import net.schnellp.mycapnutrition.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalDayView extends AppCompatActivity {

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

    private void buildPagerAdapter() {
        setContentView(R.layout.activity_journal_day_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                TextView pagerTitle = (TextView) findViewById(R.id.pager_title);
                pagerTitle.setText(Conversion.dayNumberToLongRelativeDate(position));
            }
        });

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
                intent.putExtra(SelectFood.Purpose.INTENT_EXTRA_NAME,
                        SelectFood.Purpose.CREATE_RECORD);
                intent.putExtra("DATE", date);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildPagerAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildPagerAdapter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_day_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pageBack(View v) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    public void pageForward(View v) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

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
