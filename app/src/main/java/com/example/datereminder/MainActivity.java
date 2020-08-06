package com.example.datereminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.datereminder.AddEventActivity.EVENT_STRING;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recView;
    private TextView txtNoEvent;
    private Toolbar toolbar;
    private EventAdapter adapter = new EventAdapter(this);

    private DatabaseHelper db = new DatabaseHelper(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(toolbar);


        recView.setLayoutManager( new LinearLayoutManager(this));
        recView.setAdapter(adapter);

        getEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setEvents((ArrayList<Event>) db.getAllEvents());
    }

    private void getEvents() {
        // Not needed anymore since we're not passing intents to this activity. Event is saved to database instead.
//        if (getIntent().getExtras() != null) {
//            // receive Event object and add to adapter.
//            Event event = (Event) getIntent().getExtras().get(EVENT_STRING);
//            db.addOne(event);
//        }

        if (db.getAllEvents() != null) {
            txtNoEvent.setVisibility(View.GONE);
            adapter.setEvents((ArrayList<Event>) db.getAllEvents());
        }


    }


    private void initViews() {
        recView = findViewById(R.id.recViewMain);
        txtNoEvent = findViewById(R.id.txtNoEvent);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnAdd:
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivity(intent);

                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}