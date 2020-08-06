package com.example.datereminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {
    EditText etName;
    Button btnEventDate, btnNext;
    TextView txtDateSet, txtHeader;
    Spinner spinReminder;
    Boolean isExist = false;
    int id;

    DatabaseHelper database = new DatabaseHelper(this);

    public static final String EVENT_STRING = "event";
    // TODO: pass Event object to Main Activity
    private Calendar eventDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initViews();

        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.btnPickerSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                eventDate.set(Calendar.YEAR, datePicker.getYear());
                eventDate.set(Calendar.MONTH, datePicker.getMonth());
                eventDate.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                eventDate.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                eventDate.set(Calendar.MINUTE, timePicker.getMinute());

                String dateSet = new SimpleDateFormat("MMM dd, yyyy h:mm a").format(eventDate.getTime());
                txtDateSet.setTextColor(ContextCompat.getColor(AddEventActivity.this, R.color.dateSet));
                txtDateSet.setText(dateSet);

                alertDialog.dismiss();


            }
        });

        // If receive intent for editing...
        if (getIntent().getExtras() != null) {
            // TODO: receive Event class object and let user edit
            Event event = (Event) getIntent().getExtras().get(EVENT_STRING);
            etName.setText(event.getName().toString());
            eventDate.setTimeInMillis(event.getEventDate());
            String dateSet = new SimpleDateFormat("MMM dd, yyyy h:mm a").format(eventDate.getTime());
            txtDateSet.setTextColor(ContextCompat.getColor(AddEventActivity.this, R.color.dateSet));
            txtDateSet.setText(dateSet);
            isExist = true;
            txtHeader.setText("Edit Event");
            id = event.getId();

        }


        btnEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Open calendar dialog for user to select a date and time
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        spinReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(adapterView.getItemAtPosition(i).toString()) {
                    case "Yes":
                        btnNext.setText("NEXT");
                        // TODO: Set onclick listener to launch user calendar app to select event date
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Editing event
                                if (isExist) {
                                    database.editOne(id, etName.getText().toString(), eventDate.getTimeInMillis(), true);
                                    database.close();
                                    Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                // Creating new Event
                                else {

                                    Event event = new Event(etName.getText().toString(), eventDate.getTimeInMillis(), true);
                                    database.addOne(event);
                                    database.close();

                                    Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                                // Opening user calendar app
                                Intent intent = new Intent(Intent.ACTION_EDIT);
                                intent.setType("vnd.android.cursor.item/event");
                                intent.putExtra("beginTime", eventDate.getTimeInMillis());
                                intent.putExtra("title", etName.getText().toString());

                                startActivity(intent);


                            }
                        });
                        break;
                        // User does not wish to open calendar app
                    case "No":
                        btnNext.setText("FINISH");
                        // Set onclick listener for btnNext to add event to database
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // If event object exists, edit it
                                if (isExist) {
                                    database.editOne(id, etName.getText().toString(), eventDate.getTimeInMillis(), true);
                                    database.close();
                                    Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                // Adding a new event
                                else {
                                    Event event = new Event(etName.getText().toString(), eventDate.getTimeInMillis(), false);
                                    database.addOne(event);
                                    database.close();

                                    Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        btnEventDate = findViewById(R.id.btnEventDate);
        btnNext = findViewById(R.id.btnNext);
        txtDateSet = findViewById(R.id.txtDateSet);
        spinReminder = findViewById(R.id.spinReminder);
        txtHeader = findViewById(R.id.txtHeader);


    }
}