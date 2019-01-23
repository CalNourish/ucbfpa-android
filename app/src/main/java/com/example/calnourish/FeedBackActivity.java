package com.example.calnourish;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
//        feedStr = feed.getText().toString();
        final Button submitButton = (Button) findViewById(R.id.sendbutton);

        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("feedback");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                feedStr = feed.getText().toString();
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
                Toast.makeText(FeedBackActivity.this, "Submission Complete",
                        Toast.LENGTH_LONG).show();
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
                    case R.id.foodrecovery:
                        Intent foodrecoveryIntent = new Intent(FeedBackActivity.this, FoodRecoveryActivity.class);
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
