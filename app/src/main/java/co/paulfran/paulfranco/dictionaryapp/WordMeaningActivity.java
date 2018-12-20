package co.paulfran.paulfranco.dictionaryapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import co.paulfran.paulfranco.dictionaryapp.fragments.FragmentAntonyms;
import co.paulfran.paulfranco.dictionaryapp.fragments.FragmentDefinition;
import co.paulfran.paulfranco.dictionaryapp.fragments.FragmentExample;
import co.paulfran.paulfranco.dictionaryapp.fragments.FragmentSynonyms;


public class WordMeaningActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        // Add The Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("English Words");

        // Add back navigation on the Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        mViewPager = (ViewPager) findViewById(R.id.tab_viewpager);

        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }

        // Set up view pager to the tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        // this method changes the view pager to the selected tab
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
               // Empty due to not needing these methods
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Empty due to not needing these methods
            }
        });

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        // Create List to contain all four fragments
        private final List<Fragment> mFragmentList = new ArrayList<>();
        // Create a List of strings to contain the four titles
        private final List<String> mFragmentTitleList = new ArrayList<>();

        // Create the constructor
        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        // Create a method to add the fragments and titles to this list
        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        // This method returns the current fragment
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        // This method returns the size of the Fragment List
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        // This method returns the title of the Fragment List
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    // Method that sets up the view pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDefinition(), "Definition");
        adapter.addFrag(new FragmentSynonyms(), "Synonyms");
        adapter.addFrag(new FragmentAntonyms(), "Antonyms");
        adapter.addFrag(new FragmentExample(), "Example");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Press the Back Icon
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
