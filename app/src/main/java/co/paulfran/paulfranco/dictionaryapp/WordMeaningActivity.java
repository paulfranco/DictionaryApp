package co.paulfran.paulfranco.dictionaryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.paulfran.paulfranco.dictionaryapp.fragments.FragmentAntonyms;
import co.paulfran.paulfranco.dictionaryapp.fragments.FragmentDefinition;
import co.paulfran.paulfranco.dictionaryapp.fragments.FragmentExample;
import co.paulfran.paulfranco.dictionaryapp.fragments.FragmentSynonyms;


public class WordMeaningActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    String enWord;
    DatabaseHelper myDbHelper;
    Cursor c = null;

    public String enDefinition;
    public String example;
    public String synonyms;
    public String antonyms;

    TextToSpeech tts;

    boolean startedFromShare = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        // received values
        Bundle bundle = getIntent().getExtras();
        enWord = bundle.getString("en_word");


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                startedFromShare = true;
                if (sharedText != null) {
                    Pattern p = Pattern.compile("[A-Za-z ]{1,25}");
                    Matcher m = p.matcher(sharedText);
                    if (m.matches()) {
                        enWord = sharedText;
                    } else {
                        enWord = "Not Available";
                    }
                }
            }
        }

        // open the database
        myDbHelper = new DatabaseHelper(this);
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        // get meaning
        c = myDbHelper.getMeaning(enWord);

        // move cursor to the first result
        if (c.moveToFirst()) {
            //get data from our variables
            enDefinition = c.getString(c.getColumnIndex("en_definition"));
            example = c.getString(c.getColumnIndex("example"));
            synonyms = c.getString(c.getColumnIndex("synonyms"));
            antonyms = c.getString(c.getColumnIndex("antonyms"));

            myDbHelper.insertHistory(enWord);

        } else {
            enWord = "Not available";
        }

        ImageButton btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts = new TextToSpeech(WordMeaningActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            int result = tts.setLanguage(Locale.getDefault());
                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("error", "This Language is not supported");
                            } else {
                                tts.speak(enWord, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else {
                            Log.e("error", "Initialization Failed!");
                        }
                    }
                });
            }
        });

        // Add The Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(enWord);

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
            if (startedFromShare) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
