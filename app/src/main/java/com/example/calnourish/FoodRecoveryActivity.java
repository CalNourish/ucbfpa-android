package com.example.calnourish;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FoodRecoveryActivity extends AppCompatActivity {


//    TODO: enable scroll to clicked on view
    Calendar dateCalendar;
    DatePickerDialog datePicker;
    TextInputEditText nameView, phoneView, dateView, fromTimeView, toTimeView, locationView, donationView;
    TextInputLayout locationLayout, donationLayout;
    Button submitButton, cancelButton;
    ImageButton infoButton;

    private FirebaseDatabase FDB;
    private DatabaseReference DBRef;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_foodrecovery);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.info:
                        Intent infoIntent = new Intent(FoodRecoveryActivity.this, InfoActivity.class);
                        startActivity(infoIntent);
                        break;

                    case R.id.category:
                        Intent categoryIntent = new Intent(FoodRecoveryActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;

                    case R.id.search:
                        Intent searchIntent = new Intent(FoodRecoveryActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;

                    case R.id.foodrecovery:
//                        Intent foodrecoveryIntent = new Intent(FoodRecoveryActivity.this, FoodRecoveryActivity.class);
//                        startActivity(foodrecoveryIntent);
                        break;

                    case R.id.menu:
                        Intent menuIntent = new Intent(FoodRecoveryActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                        break;
                }
                return false;
            }
        });

        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("foodRecovery");

        nameView = findViewById(R.id.name);
        phoneView = findViewById(R.id.phone);
        locationView = findViewById(R.id.location);
        dateView = findViewById(R.id.date);
        fromTimeView = findViewById(R.id.timeStart);
        toTimeView = findViewById(R.id.timeEnd);
        donationView = findViewById(R.id.donation);

        locationLayout = findViewById(R.id.locationLayout);
        donationLayout = findViewById(R.id.donationLayout);

        submitButton = findViewById(R.id.submit);
        infoButton = findViewById(R.id.infoButton);

        infoButton.setOnClickListener(new View.OnClickListener() {
            String infoContent = "Use this form to notify UC Berkeley Food Pantry volunteers of possible food donations";
            String title = "UC Berkeley Food Pantry Food Recovery";
            @Override
            public void onClick(View v) {
                AlertDialog.Builder infoDialogBuilder = new AlertDialog.Builder(FoodRecoveryActivity.this);
                infoDialogBuilder.setMessage(infoContent)
                        .setTitle(title);
                AlertDialog infoDialog = infoDialogBuilder.create();
                infoDialog.show();
            }
        });

        locationView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    locationLayout.setHelperText("Please enter precise location (ex: Dwinelle 102, Evans 70, Lower Sproul etc.)");
                } else {
                    locationLayout.setHelperText(null);
                }
            }
        });

//        TODO: Make sure the date is valid
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateCalendar = Calendar.getInstance();
                int day = dateCalendar.get(Calendar.DAY_OF_MONTH);
                int month = dateCalendar.get(Calendar.MONTH);
                int year = dateCalendar.get(Calendar.YEAR);

                // Requires API 24 but the apps min is 23 -- will break if 23 or lower... may need to find alternative...
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datePicker = new DatePickerDialog(FoodRecoveryActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePickerView, int setYear, int setMonth, int setDay) {
                            dateView.setText((setMonth + 1) + "/" + setDay + "/" + setYear);
                        }
                    }, year, month, day);
                    datePicker.show();
                }
            }
        });


//        TODO: Make sure the time range is valid
        fromTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(FoodRecoveryActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                        fromTimeView.setText(formatTime(setHour, setMinute));
                    }
                }, hour, minute, false);
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

        toTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(FoodRecoveryActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                        toTimeView.setText(formatTime(setHour, setMinute));
                    }
                }, hour, minute, false);
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

        donationView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    donationLayout.setHelperText("Ex: 30 Sandwiches, Fruit Platter, etc.");
                } else {
                    donationLayout.setHelperText(null);
                }
            }
        });

        // Send the food recovery
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                final String name, phone, location, pickUpDate, pickUpStartTime, pickUpEndTime, donation;
                name = nameView.getText().toString();
                phone = phoneView.getText().toString();
                location = locationView.getText().toString();
                pickUpDate = dateView.getText().toString();
                pickUpStartTime = fromTimeView.getText().toString();
                pickUpEndTime = toTimeView.getText().toString();
                donation = donationView.getText().toString();


                DBRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> foodRecoveryMap = new HashMap<>();
                        foodRecoveryMap.put("name", name);
                        foodRecoveryMap.put("phone", phone);
                        foodRecoveryMap.put("location", location);
                        foodRecoveryMap.put("pickUpDate", pickUpDate);
                        foodRecoveryMap.put("pickUpFrom", pickUpStartTime);
                        foodRecoveryMap.put("pickUpUntil", pickUpEndTime);
                        foodRecoveryMap.put("donation", donation);
                        if(count == 0){
                            DBRef.push().setValue((Map) foodRecoveryMap);
                            count++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        //Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

                nameView.getText().clear();
                phoneView.getText().clear();
                locationView.getText().clear();
                dateView.getText().clear();
                fromTimeView.getText().clear();
                toTimeView.getText().clear();
                donationView.getText().clear();

                Toast.makeText(FoodRecoveryActivity.this, "Thanks for your submission, a pantry volunteer will contact you",
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    public String formatTime(int h, int m) {
        String hour, minute;
        String timePeriod;
        if (h >= 12) {
            timePeriod = "PM";
            if (h == 12) {
                hour = "12";
            } else {
                hour = Integer.toString(h - 12);
            }
        }
        else {
            timePeriod = "AM";
            if (h == 0) {
                hour = "12";
            }
            else {
                hour = Integer.toString(h);
            }
        }
        if (m < 10) {
            minute = "0" + m;
        }
        else {
            minute = Integer.toString(m);
        }
        return hour + ":" + minute + " " + timePeriod;
    }








    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
