package co.paulfran.paulfranco.dictionaryapp;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;


public class MainActivity extends AppCompatActivity {

    SearchView search;
    static DatabaseHelper mDbHelper;
    static boolean databaseOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search = (SearchView) findViewById(R.id.search_view);

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });

        mDbHelper = new DatabaseHelper(this);

        if (mDbHelper.checkDatabase()) {
            openDatabase();
        } else {
            // if database doesnt exist
            LoadDatabaseAsync task = new LoadDatabaseAsync(MainActivity.this);
            task.execute();
        }
    }

    protected static void openDatabase() {
        try {
            mDbHelper.openDataBase();
            databaseOpened = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; This adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/up button
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_exit) {
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
