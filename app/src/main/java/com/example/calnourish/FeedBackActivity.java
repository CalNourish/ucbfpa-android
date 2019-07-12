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

import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends AppCompatActivity {

    private FirebaseDatabase FDB;
    private DatabaseReference DBRef;
    private String temp = "Testing";
    private String feedStr = "";
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

        final EditText feed = (EditText) findViewById(R.id.feedback);
        final TextInputLayout feedbackLayout = findViewById(R.id.feedbackLayout);
//        feedStr = feed.getText().toString();
        final Button submitButton = (Button) findViewById(R.id.sendbutton);

        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("feedback");

        feed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    feedbackLayout.setError(null);
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
                    feedbackLayout.setError("Error. Please try again soon");

                } else {
                    feedStr = feed.getText().toString();

                    if (feedStr.length() == 0) {
                        feedbackLayout.setError("Feedback cannot be empty");

                    } else{
                        lastFeedbackSubmission = SystemClock.elapsedRealtime();

                        count = 0;
                        DBRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap<String, HashMap<String, String>> feedMap = (HashMap)dataSnapshot.getValue();
                                HashMap<String, String> val = new HashMap<>();
                                HashMap<String, String> randMap = new HashMap<>();
                                val.put("feedbackID", temp);
                                val.put("feedbackText", feedStr);
                                val.put("feedbackType", temp);
                                val.put("time", temp);
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
                        feed.getText().clear();
                        Toast.makeText(FeedBackActivity.this, "Thanks for the feedback!",
                                Toast.LENGTH_LONG).show();
                        feedbackLayout.setError(null);
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
