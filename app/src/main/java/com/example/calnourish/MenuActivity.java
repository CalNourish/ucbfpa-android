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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity {

    private FirebaseDatabase FDB;
    private DatabaseReference DBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_menu);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("inventory");//.child("hello");

//        Button btn = (Button) findViewById(R.id.dbbutton);
//        final TextView dbResults = (TextView) findViewById(R.id.textView);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DBRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        HashMap<String, HashMap<String, String>> myText = (HashMap)dataSnapshot.getValue();
//                        HashMap<String, String> val = new HashMap<>();
//                        for(Map.Entry<String, HashMap<String, String>> entry : myText.entrySet()) {
//                            String key = entry.getKey();
//                            val = entry.getValue();
//                            //dbResults.setText(texthold);
//                        }
//                        System.out.print("HEREEEEEEEE**********************" + myText);
//                        val.put("count", "700000000");
//                        DBRef.updateChildren((Map) myText);
//                        //dbResults.setText((String) myText);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        //Log.w(TAG, "Failed to read value.", error.toException());
//                    }
//                });
//            }
//        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.info:
                        Intent infoIntent = new Intent(MenuActivity.this, InfoActivity.class);
                        startActivity(infoIntent);
                        break;

                    case R.id.category:
                        Intent categoryIntent = new Intent(MenuActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;

                    case R.id.search:
                        Intent searchIntent = new Intent(MenuActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;

                    case R.id.foodrecovery:
                        Intent foodrecoveryIntent = new Intent(MenuActivity.this, FoodRecoveryActivity.class);
                        startActivity(foodrecoveryIntent);
                        break;

                    case R.id.menu:
//                        Intent menuIntent = new Intent(MenuActivity.this, MenuActivity.class);
//                        startActivity(menuIntent);
                        break;
                }


                return false;
            }
        });

        Button infoButton = (Button) findViewById(R.id.menu0);
        Button categoryButton = (Button) findViewById(R.id.menu1);
        Button searchButton = (Button) findViewById(R.id.menu2);
        Button feedbackButton = (Button) findViewById(R.id.menu3);
        Button foodRecoveryButton = (Button) findViewById(R.id.menu4);
        Button communityGuideButton = (Button) findViewById(R.id.menu5);


        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(MenuActivity.this, InfoActivity.class);
                startActivity(infoIntent);
            }
        });

        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryIntent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(categoryIntent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(MenuActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedbackIntent = new Intent(MenuActivity.this, FeedBackActivity.class);
                startActivity(feedbackIntent);
            }
        });

        foodRecoveryButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent foodRecoveryIntent = new Intent(MenuActivity.this, FoodRecoveryActivity.class);
              startActivity(foodRecoveryIntent);
          }
        });

        communityGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent communityAgreementIntent = new Intent(MenuActivity.this, CommunityAgreementActivity.class);
                startActivity(communityAgreementIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
