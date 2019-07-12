package com.example.calnourish;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventoryActivity extends AppCompatActivity {

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
    private ItemAdapter adapter;
    private ArrayList<Item> items;
    private Button searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_inventory);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        final Intent intent = getIntent();
        categoryName = intent.getStringExtra("category");
        itemName = intent.getStringExtra("itemName");
        final TextView textView = (TextView) findViewById(R.id.Category);
        if(categoryName.equals("")) {
            textView.setText(itemName);
        } else {
            textView.setText(categoryName);
        }
        categoryName = categoryName.toLowerCase();
        if(categoryName.equals("prepared foods")) {
            categoryName = "prepared";
        } else if (categoryName.equals("canned foods")) {
            categoryName = "canned";
        }

        final Button updateButton = (Button) findViewById(R.id.updateButton);
        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("inventory");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        inventoryMap = (HashMap)dataSnapshot.getValue();
                        HashMap<String, Object> val = new HashMap<>();
                        int counter = 0;
                        for(Map.Entry<String, Object> entry : inventoryMap.entrySet()) {
                            //String key = entry.getKey();
                            val = (HashMap<String, Object>) entry.getValue();
                            //dbResults.setText(texthold);
                            String itemName = (String) val.get("itemName");
                            String imageName = (String) val.get("imageName");
                            itemToCategory.put(itemName, val);
                            itemNameToimageName.put(itemName, imageName);
                            updateCard();
                        }
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
                Intent searchIntent = new Intent(InventoryActivity.this, SearchActivity.class);
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
                        Intent infoIntent = new Intent(InventoryActivity.this, InfoActivity.class);
                        startActivity(infoIntent);
                        break;

                    case R.id.category:
                        Intent categoryIntent = new Intent(InventoryActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;

                    case R.id.search:
                        Intent searchIntent = new Intent(InventoryActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;

                    case R.id.notifications:
                        Intent foodrecoveryIntent = new Intent(InventoryActivity.this, NotificationListActivity.class);
                        startActivity(foodrecoveryIntent);
                        break;

                    case R.id.menu:
                        Intent menuIntent = new Intent(InventoryActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                        break;
                }
                return false;
            }
        });
    }

    public void updateCard() {
        items = getItems();
        recyclerView = findViewById(R.id.main_recycler);
        adapter = new ItemAdapter(items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InventoryActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Item item = items.get(position);
                Intent intent = new Intent(InventoryActivity.this, IndividualItemActivity.class);
                intent.putExtra("item_name", item.getItemName());
                intent.putExtra("item_image", item.getItemImage());
                intent.putExtra("item_count", item.getItemCount());
                startActivity(intent);
            }
        });
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> item = new ArrayList<>();
        HashMap<String, Object> val = new HashMap<>();
        for(Map.Entry<String, Object> entry : itemToCategory.entrySet()) {
            val = (HashMap<String, Object>)entry.getValue();
            for(Map.Entry<String, Object> entry1 : val.entrySet()) {
                if (val.get("itemName").equals(itemName)) {
                    item.add(new Item((String) val.get("itemName"), (String) val.get("cost"),
                            (String) val.get("count"), (String) val.get("imageName")));
                    break;
                }
                if (((HashMap) val.get("categoryName")).containsValue(categoryName)) {
                    item.add(new Item((String) val.get("itemName"), (String) val.get("cost"),
                            (String) val.get("count"), (String) val.get("imageName")));
                    break;
                }
            }
        }
        return item;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
