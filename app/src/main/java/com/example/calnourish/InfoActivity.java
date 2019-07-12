package com.example.calnourish;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class InfoActivity extends AppCompatActivity {

    private FirebaseDatabase FDB;
    private DatabaseReference DBRef;
    private HashMap<String, Object> infoMap = new HashMap<>();
    private HashMap<String, TextView> textViews = new HashMap<>();
    private Button bugReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_info);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        final Button updateButton = (Button) findViewById(R.id.updateButton);
        bugReport = (Button) findViewById(R.id.bugReport);

        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("info");

        Calendar dayOfWeek = Calendar.getInstance();
        Map<Integer, String> daysOfTheWeek = new HashMap();
        daysOfTheWeek.put(0, "Sunday");
        daysOfTheWeek.put(1, "Monday");
        daysOfTheWeek.put(2, "Tuesday");
        daysOfTheWeek.put(3, "Wednesday");
        daysOfTheWeek.put(4, "Thursday");
        daysOfTheWeek.put(5, "Friday");
        daysOfTheWeek.put(6, "Saturday");

        int today = dayOfWeek.get(Calendar.DAY_OF_WEEK);
        TextView day0 = findViewById(R.id.day0);
        day0.setText(daysOfTheWeek.get(today - 1 % 7));
        TextView day1 = findViewById(R.id.day1);
        day1.setText(daysOfTheWeek.get((today) % 7));
        TextView day2 = findViewById(R.id.day2);
        day2.setText(daysOfTheWeek.get((today + 1) % 7));
        TextView day3 = findViewById(R.id.day3);
        day3.setText(daysOfTheWeek.get((today + 2) % 7));
        TextView day4 = findViewById(R.id.day4);
        day4.setText(daysOfTheWeek.get((today + 3) % 7));
        TextView day5 = findViewById(R.id.day5);
        day5.setText(daysOfTheWeek.get((today + 4) % 7));
        TextView day6 = findViewById(R.id.day6);
        day6.setText(daysOfTheWeek.get((today + 5) % 7));

        System.out.println(day0.getText().toString());
        textViews.put(day0.getText().toString(), (TextView) findViewById(R.id.day0_hours));
        textViews.put(day1.getText().toString(), (TextView) findViewById(R.id.day1_hours));
        textViews.put(day2.getText().toString(), (TextView) findViewById(R.id.day2_hours));
        textViews.put(day3.getText().toString(), (TextView) findViewById(R.id.day3_hours));
        textViews.put(day4.getText().toString(), (TextView) findViewById(R.id.day4_hours));
        textViews.put(day5.getText().toString(), (TextView) findViewById(R.id.day5_hours));
        textViews.put(day6.getText().toString(), (TextView) findViewById(R.id.day6_hours));
        textViews.put("email", (TextView) findViewById(R.id.emailText));
        textViews.put("url", (TextView) findViewById(R.id.urlText));
        textViews.put("location", (TextView) findViewById(R.id.locationText));
        textViews.put("calnourishemail", (TextView) findViewById(R.id.calnourishEmailText));
        textViews.put("phone", (TextView) findViewById(R.id.phoneText));


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        infoMap = (HashMap)dataSnapshot.getValue();

                        textViews.get("Sunday").setText((String) ((Map) infoMap.get("-sunday")).get("12hours"));
                        textViews.get("Monday").setText((String) ((Map) infoMap.get("-monday")).get("12hours"));
                        textViews.get("Tuesday").setText((String) ((Map) infoMap.get("-tuesday")).get("12hours"));
                        textViews.get("Wednesday").setText((String) ((Map) infoMap.get("-wednesday")).get("12hours"));
                        textViews.get("Thursday").setText((String) ((Map) infoMap.get("-thursday")).get("12hours"));
                        textViews.get("Friday").setText((String) ((Map) infoMap.get("-friday")).get("12hours"));
                        textViews.get("Saturday").setText((String) ((Map) infoMap.get("-saturday")).get("12hours"));
                        textViews.get("email").setText((String) ((Map) infoMap.get("-contact")).get("email"));
                        String url = (String) ((Map) infoMap.get("-contact")).get("url");
                        String text = "<a href='" + url + "'>" + url + "</a>";
                        textViews.get("url").setClickable(true);
                        textViews.get("url").setMovementMethod(LinkMovementMethod.getInstance());
                        textViews.get("url").setText(Html.fromHtml(text));
                        textViews.get("location").setText((String) ((Map) infoMap.get("-location")).get("location"));
                        textViews.get("calnourishemail").setText((String) ((Map) infoMap.get("calnourishcontact")).get("email"));
                        textViews.get("phone").setText((String) ((Map) infoMap.get("calnourishcontact")).get("phone"));


                        // TODO: should refactor the switch
                        Calendar calendar = Calendar.getInstance();
                        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                        String now = LocalDateTime.now().format(timeFormat);
                        LocalTime currentTime = LocalTime.parse(now, timeFormat);
                        int day = calendar.get(Calendar.DAY_OF_WEEK);
                        TextView openOrClose = findViewById(R.id.pantryTime);

                        LocalTime open;
                        LocalTime close;
                        String pantryTime;

                        switch (day) {
                            case Calendar.SUNDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-sunday")).get("24hours"));
                                break;
                            case Calendar.MONDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-monday")).get("24hours"));
                                break;
                            case Calendar.TUESDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-tuesday")).get("24hours"));
                                break;
                            case Calendar.WEDNESDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-wednesday")).get("24hours"));
                                break;
                            case Calendar.THURSDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-thursday")).get("24hours"));
                                break;
                            case Calendar.FRIDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-friday")).get("24hours"));
                                break;
                            case Calendar.SATURDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-saturday")).get("24hours"));
                                break;
                            default:
                                pantryTime = "closed";
                        }

                        if (pantryTime.contains("[a-zA-Z]+")) {
                            openOrClose.setText("closed");
                            openOrClose.setBackground(getDrawable(R.drawable.pantry_closed));
                        } else {
                            try {
                                open = LocalTime.parse(pantryTime.split(" ")[0], timeFormat);
                                close = LocalTime.parse(pantryTime.split(" ")[2], timeFormat);
                                if (close.minusHours(1).isBefore(currentTime) && close.isAfter(currentTime)) {
                                    openOrClose.setText("closing soon");
                                    openOrClose.setTextSize(18);
                                    openOrClose.setBackground(getDrawable(R.drawable.pantry_closing_soon));
                                } else if (currentTime.isAfter(open) && currentTime.isBefore(close)) {
                                    openOrClose.setText("open");
                                    openOrClose.setBackground(getDrawable(R.drawable.pantry_open));
                                } else {
                                    openOrClose.setText("closed");
                                    openOrClose.setBackground(getDrawable(R.drawable.pantry_closed));
                                }
                            } catch (Exception e) {
                                // If invalid date or time, or simply closed? Should check input on web app to prevent error
                                System.out.println("May be closed or wrong input");
                                System.out.println("ACTUAL ERROR: " + e);
                                openOrClose.setText("closed");
                                openOrClose.setBackground(getDrawable(R.drawable.pantry_closed));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        //Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });

        bugReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://goo.gl/forms/mo3vo83oYbRQSrtr1";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateButton.performClick();
            }
        }, 1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.info:
//                        Intent infoIntent = new Intent(InfoActivity.this, InfoActivity.class);
//                        startActivity(infoIntent);
                        break;

                    case R.id.category:
                        Intent categoryIntent = new Intent(InfoActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;

                    case R.id.search:
                        Intent searchIntent = new Intent(InfoActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;

                    case R.id.notifications:
                        Intent foodrecoveryIntent = new Intent(InfoActivity.this, NotificationListActivity.class);
                        startActivity(foodrecoveryIntent);
                        break;

                    case R.id.menu:
                        Intent menuIntent = new Intent(InfoActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                        break;
                }


                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
