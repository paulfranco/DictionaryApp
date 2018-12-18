package co.paulfran.paulfranco.dictionaryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class WordMeaningActivity extends AppCompatActivity {

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
