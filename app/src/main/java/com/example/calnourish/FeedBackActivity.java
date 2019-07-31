package com.example.calnourish;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends AppCompatActivity {

    private FirebaseDatabase FDB;
    private DatabaseReference DBRef;
    private String temp = "Testing";
    private String feedStr1 = "n/a";
    private String feedStr2 = "n/a";
    private String feedStr3 = "n/a";

    private HashMap<String, String> tempMap = new HashMap<>();
    private int count;

    private long lastFeedbackSubmission = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_feedback);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);


        final EditText feed1 = (EditText) findViewById(R.id.feedback1);
        final TextInputLayout feedbackLayout1 = findViewById(R.id.feedbackLayout1);
        final EditText feed2 = (EditText) findViewById(R.id.feedback2);
        final TextInputLayout feedbackLayout2 = findViewById(R.id.feedbackLayout2);
        final EditText feed3 = (EditText) findViewById(R.id.feedback3);
        final TextInputLayout feedbackLayout3 = findViewById(R.id.feedbackLayout3);
        final Button submitButton = (Button) findViewById(R.id.sendbutton);

        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("feedback");

        feed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    feedbackLayout1.setError(null);
                    feedbackLayout2.setError(null);
                    feedbackLayout3.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        feed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    feedbackLayout1.setError(null);
                    feedbackLayout2.setError(null);
                    feedbackLayout3.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        feed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    feedbackLayout1.setError(null);
                    feedbackLayout2.setError(null);
                    feedbackLayout3.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prevent spamming feedback, threshold 3 sec
                if (SystemClock.elapsedRealtime() - lastFeedbackSubmission < 3000) {
                    // Say there is an error
                    Toast.makeText(FeedBackActivity.this, "Error. Please try again in a few seconds",
                            Toast.LENGTH_LONG).show();
                    feedbackLayout3.setError("Error. Please try again soon");

                } else {
                    feedStr1 = feed1.getText().toString();
                    feedStr2 = feed2.getText().toString();
                    feedStr3 = feed3.getText().toString();

                    final String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    if (feedStr1.length() == 0 || feedStr2.length() == 0 || feedStr3.length() == 0) {
                        feedbackLayout1.setError("Please enter n/a if no comment");
                        feedbackLayout2.setError("Please enter n/a if no comment");
                        feedbackLayout3.setError("Please enter n/a if no comment");

                    } else{
                        lastFeedbackSubmission = SystemClock.elapsedRealtime();

                        count = 0;
                        DBRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap<String, HashMap<String, String>> feedMap = (HashMap)dataSnapshot.getValue();
                                HashMap<String, String> val = new HashMap<>();
                                HashMap<String, String> randMap = new HashMap<>();
                                val.put("improve upon", feedStr1);
                                val.put("bugs and crashes", feedStr2);
                                val.put("other feedback", feedStr3);
                                val.put("time", timeStamp);
                                feedMap.put("uniqueId", val);
//                        for(Map.Entry<String, HashMap<String, String>> entry : feedMap.entrySet()) {
//                            String key = entry.getKey();
//                            val = entry.getValue();
//                        }
//                        val.put("count", "700000000");
//                        DBRef.push().setValue((Map) val);
                                if(count == 0){
                                    DBRef.push().setValue((Map) val);
                                    count++;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                //Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                        DBRef.push().setValue((Map) tempMap);
                        feed1.getText().clear();
                        feed2.getText().clear();
                        feed3.getText().clear();
                        Toast.makeText(FeedBackActivity.this, "Thanks for the feedback!",
                                Toast.LENGTH_LONG).show();
                        feedbackLayout3.setError(null);
                    }
                }

            }

        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.info:
                        Intent infoIntent = new Intent(FeedBackActivity.this, InfoActivity.class);
                        startActivity(infoIntent);
                        break;
                    case R.id.category:
                        Intent categoryIntent = new Intent(FeedBackActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;
                    case R.id.search:
//                        SearchView searchView = (SearchView) findViewById(R.id.search_bar);
//                        searchView.onActionViewExpanded();
                        Intent searchIntent = new Intent(FeedBackActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;
                    case R.id.notifications:
                        Intent foodrecoveryIntent = new Intent(FeedBackActivity.this, NotificationListActivity.class);
                        startActivity(foodrecoveryIntent);
                        break;
                    case R.id.menu:
                        Intent menuIntent = new Intent(FeedBackActivity.this, MenuActivity.class);
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
