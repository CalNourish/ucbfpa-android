package com.example.calnourish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationListActivity extends AppCompatActivity {
    private FirebaseDatabase FDB;
    private DatabaseReference DBRef;
    private HashMap<String, Object> inventoryMap = new HashMap<>();
    private HashMap<String, Object> itemToCategory = new HashMap<>();
    private HashMap<String, HashMap<String, String>> categoryToItem = new HashMap<>();
    private HashMap<String, String> itemNameToimageName = new HashMap<>();

    private String categoryName = "";
    private String itemName = "";

    private RecyclerView recyclerView;
    private TextView message;
    private NotificationAdapter adapter;
    private ArrayList<Notification> notifications;
    private Button searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_notification_list);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        final Intent intent = getIntent();
        final Button updateButton = (Button) findViewById(R.id.updateButton);
        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("notification");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        inventoryMap = (HashMap)dataSnapshot.getValue();
                        HashMap<String, Object> val = new HashMap<>();
                        int counter = 0;
                        notifications = new ArrayList<>();

                        for (DataSnapshot notif : dataSnapshot.getChildren()) {
                            notifications.add(new Notification(notif.child("title").getValue().toString(),
                                                                notif.child("text").getValue().toString()));
                        }
                        updateCard();
//                        for(Map.Entry<String, Object> entry : inventoryMap.entrySet()) {
////                            //String key = entry.getKey();
////                            val = (HashMap<String, Object>) entry.getValue();
////                            //dbResults.setText(texthold);
////                            String itemName = (String) val.get("itemName");
////                            String imageName = (String) val.get("imageName");
////                            itemToCategory.put(itemName, val);
////                            itemNameToimageName.put(itemName, imageName);
////                            updateCard();
////                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        //Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

            }
        });

        searchBar = findViewById(R.id.search_bar);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(NotificationListActivity.this, SearchActivity.class);
                startActivity(searchIntent);
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
                        Intent infoIntent = new Intent(NotificationListActivity.this, InfoActivity.class);
                        startActivity(infoIntent);
                        break;

                    case R.id.category:
                        Intent categoryIntent = new Intent(NotificationListActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;

                    case R.id.search:
                        Intent searchIntent = new Intent(NotificationListActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;

                    case R.id.foodrecovery:
                        Intent notificationIntent = new Intent(NotificationListActivity.this, NotificationListActivity.class);
                        startActivity(notificationIntent);
                        break;

                    case R.id.menu:
                        Intent menuIntent = new Intent(NotificationListActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                        break;
                }
                return false;
            }
        });
    }

    public void updateCard() {
//        notifications = getNotifications();
        recyclerView = findViewById(R.id.main_recycler);
        adapter = new NotificationAdapter(notifications);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NotificationListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Item item = items.get(position);
//                Intent intent = new Intent(NotificationListActivity.this, IndividualItemActivity.class);
//                intent.putExtra("item_name", item.getItemName());
//                intent.putExtra("item_image", item.getItemImage());
//                intent.putExtra("item_count", item.getItemCount());
//                startActivity(intent);
//            }
//        });
    }
//
//    public ArrayList<Item> getNotifications() {
//        ArrayList<Notification> item = new ArrayList<>();
//        HashMap<String, Object> val = new HashMap<>();
//        for(Map.Entry<String, Object> entry : itemToCategory.entrySet()) {
//            val = (HashMap<String, Object>)entry.getValue();
//            for(Map.Entry<String, Object> entry1 : val.entrySet()) {
//                if (val.get("itemName").equals(itemName)) {
//                    item.add(new Item((String) val.get("itemName"), (String) val.get("cost"),
//                            (String) val.get("count"), (String) val.get("imageName")));
//                    break;
//                }
//                if (((HashMap) val.get("categoryName")).containsValue(categoryName)) {
//                    item.add(new Item((String) val.get("itemName"), (String) val.get("cost"),
//                            (String) val.get("count"), (String) val.get("imageName")));
//                    break;
//                }
//            }
//        }
//        return item;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
