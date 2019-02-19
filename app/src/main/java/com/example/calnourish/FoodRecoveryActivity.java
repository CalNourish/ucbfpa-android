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
import android.text.TextUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodRecoveryActivity extends AppCompatActivity {


//    TODO: enable scroll to clicked on view
    Calendar dateCalendar;
    DatePickerDialog datePicker;
    TextInputEditText nameView, phoneView, dateView, fromTimeView, toTimeView, locationView, donationView;
    TextInputLayout nameLayout, phoneLayout, dateLayout, fromLayout, toLayout, locationLayout, donationLayout;
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


        nameLayout = findViewById(R.id.nameLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        dateLayout = findViewById(R.id.dateLayout);
        fromLayout = findViewById(R.id.timeStartLayout);
        toLayout = findViewById(R.id.timeEndLayout);
        locationLayout = findViewById(R.id.locationLayout);
        donationLayout = findViewById(R.id.donationLayout);



        submitButton = findViewById(R.id.submit);
        infoButton = findViewById(R.id.infoButton);


        nameLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nameLayout.setError(null);
                }
            }
        });

        phoneLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View b, boolean hasFocus) {
                if (hasFocus) {
                    phoneLayout.setError(null);
                }
            }
        });





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

        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameLayout.setError(null);
            }
        });

        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneLayout.setError(null);
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

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateLayout.setError(null);
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

                    datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePicker.show();
                }
            }
        });


        fromTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromLayout.setError(null);
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
                toLayout.setError(null);
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

                boolean validForm = true;


                if (isValidName(name) > 0) {
                    nameLayout.setError("Name cannot be empty. Please enter name of donor");
                    validForm = false;
                }

                switch(isValidPhone(phone)) {
                    case 1:
                        phoneLayout.setError("Phone number cannot be empty");
                        validForm = false;
                        break;
                    case 2:
                        phoneLayout.setError("Please include your area code");
                        phoneView.getText().clear();
                        validForm = false;
                        break;
                    case 3:
                        phoneLayout.setError("Please enter a valid phone number");
                        phoneView.getText().clear();
                        validForm = false;
                        break;
                }

                switch(isValidTime(pickUpDate, pickUpStartTime, pickUpEndTime)) {
                    case 1:
                        toLayout.setError("Please enter the latest possible pick up time");
                        validForm = false;
                        break;
                    case 2:
                        fromLayout.setError("Please enter the earliest possible pick up time");
                        validForm = false;
                        break;
                    case 3:
                        toLayout.setError("Please enter the latest possible pick up time");
                        fromLayout.setError("Please enter the earliest possible pick up time");
                        validForm = false;
                        break;
                    case 4:
                        fromLayout.setError("The selected time has already passed");
                        validForm = false;
                        break;
                    case 5:
                        toLayout.setError("The selected latest pick up time is before the earliest selected time");
                        fromLayout.setError("Please check the from and to fields");
                        validForm = false;
                        break;

                }



                    if (isValidDate(pickUpDate) > 0) {
                    dateLayout.setError("Date cannot be empty");
                    validForm = false;
                }


                if (isValidLocation(location) > 0) {
                    locationLayout.setError("Location cannot be empty. Please enter location for pick up");
                    validForm = false;
                }

                if (isValidDonation(donation) > 0) {
                    donationLayout.setError("Donation cannot be empty. Please include a short description");
                    validForm = false;
                }



                if (validForm) {
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

                Toast.makeText(FoodRecoveryActivity.this, "Please complete the form",
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

    /**
     * FORM VALIDATION
     */

    public boolean isEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    /**
     *
     * @param name
     * @return the error validation type
     * 0: no error
     * 1: empty name
     */
    public int isValidName(String name) {
        if (isEmpty(name)) {
            return 1;
        }
        return 0;
    }

    /**
     *
     * @param phone
     * @return the error validation type
     * 0: no error
     * 1: empty phone number
     * 2: No area code
     * 2: invalid characters
     */
    public int isValidPhone(String phone) {
        String validPhoneCharacters = "^[+]?[0-9]{10,13}$";
        Pattern pattern = Pattern.compile(validPhoneCharacters);
        Matcher match = pattern.matcher(phone);
        if (isEmpty(phone)) {
            return 1;
        } else if (phone.length() < 10) {
            return 2;
        } else if (!match.matches()) {
            return 3;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param location
     * @return the error validation type
     * 0: no error
     * 1: empty location
     */
    public int isValidLocation(String location) {
        if (isEmpty(location)) {
            return 1;
        }
        return 0;
    }

    /**
     *
     * @param date
     * @return the error validation
     * 0: no error
     * 1: empty date
     */
    public int isValidDate(String date) {

        if (isEmpty(date)) {
            return 1;
        }
        return 0;
    }

    /**
     *
     * @param date
     * @param start
     * @param end
     * @return the error validation
     * 0: no error
     * 1: empty end
     * 2: empty start
     * 3: empty end and empty start
     * 4: start time has already passed
     * 5: end if before the start
     */
    public int isValidTime(String date, String start, String end) {
        int empty = isEmpty(start) && isEmpty(end) ? 3 : isEmpty(start) ? 2 : isEmpty(end) ? 1 : 0;
        if (empty > 0) {
            return empty;
        }
        Calendar now = Calendar.getInstance();
        Calendar instance = Calendar.getInstance();
        Date startParsed = null;
        Date endParsed = null;
        Date dateParsed = null;
        String timeString = null;
        Date timeNow = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        try {
            startParsed = timeFormat.parse(start);
            endParsed = timeFormat.parse(end);
            dateParsed = dateFormat.parse(date);
            timeString = timeFormat.format(instance.getTime());
            timeNow = timeFormat.parse(timeString);
            instance.setTime(dateParsed);

        } catch (ParseException e) {
        }
        if (now.get(Calendar.DATE) == instance.get(Calendar.DATE) && startParsed.before(timeNow)) {
            return 4;
        }
        if (!endParsed.after(startParsed)) {
            return 5;
        }


        return 0;
    }

    /**
     *
     * @param donation
     * @return the error validation type
     * 0: no error
     * 1: empty donation
     */
    public int isValidDonation(String donation) {
        if (isEmpty(donation)) {
            return 1;
        }
        return 0;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
