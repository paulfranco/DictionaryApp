package co.paulfran.paulfranco.dictionaryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    SearchView search;
    static DatabaseHelper mDbHelper;
    static boolean databaseOpened = false;

    SimpleCursorAdapter suggestionAdapter;

    ArrayList<History> historyList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter historyAdapter;

    RelativeLayout emptyHistory;
    Cursor cursorHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search = findViewById(R.id.search_view);

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
        // setup Simple Cursor Adapter
        final String[] from = new String[] {"en_word"};
        final int[] to = new int[] {R.id.suggestion_text};

        suggestionAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.suggestion_row, null, from, to, 0) {
            @Override
            public void changeCursor(Cursor cursor) {
                super.swapCursor(cursor);
            }
        };

        search.setSuggestionsAdapter(suggestionAdapter);

        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                // code here

                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                // add clicked text to search box
                CursorAdapter ca = search.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                String clicked_word = cursor.getString(cursor.getColumnIndex("en_word"));
                search.setQuery(clicked_word, false);

                search.clearFocus();
                search.setFocusable(false);

                Intent intent = new Intent(MainActivity.this , WordMeaningActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("en_word", clicked_word);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                String text = search.getQuery().toString();

                // REGEX to avoid app from crashing when iser enters ' (single quote)
                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m  = p.matcher(s);

                if (m.matches()) {

                    Cursor c = mDbHelper.getMeaning(text);

                    if (c.getCount() == 0) {

                        showAlertDialog();

                    } else {

                        search.clearFocus();
                        search.setFocusable(false);

                        Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("en_word", text);
                        startActivity(intent);

                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                // Give suggestions list margins
                search.setIconifiedByDefault(false);

                // REGEX to avoid app from crashing when iser enters ' (single quote)
                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m  = p.matcher(s);

                if (m.matches()) {

                    Cursor cursorSuggestion = mDbHelper.getSuggestions(s);
                    suggestionAdapter.changeCursor(cursorSuggestion);

                } else {
                    showAlertDialog();
                }

                return false;
            }
        });

        emptyHistory = (RelativeLayout) findViewById(R.id.empty_history);

        // recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_history);
        layoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        fetch_history();

    }



    protected static void openDatabase() {
        try {
            mDbHelper.openDataBase();
            databaseOpened = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetch_history() {

        historyList = new ArrayList<>();
        historyAdapter = new RecyclerViewAdapterHistory(this, historyList);
        recyclerView.setAdapter(historyAdapter);

        History h;

        if (databaseOpened) {
            cursorHistory = mDbHelper.getHistory();
            if (cursorHistory.moveToFirst()) {
                do {
                    h = new History(cursorHistory.getString(cursorHistory.getColumnIndex("word")), cursorHistory.getString(cursorHistory.getColumnIndex("en_definition")));
                    historyList.add(h);
                }
                while (cursorHistory.moveToNext());
            }
            historyAdapter.notifyDataSetChanged();

            if (historyAdapter.getItemCount() == 0) {
                emptyHistory.setVisibility(View.VISIBLE);
            } else {
                emptyHistory.setVisibility(View.GONE);
            }
        }


    }


    private void showAlertDialog() {

        search.setQuery("", false);

        AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Word Not Found");
        builder.setMessage("Please search again");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // positive button logic
            }
        });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                search.clearFocus();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

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

    @Override
    protected void onResume() {
        super.onResume();
        fetch_history();
    }
}
