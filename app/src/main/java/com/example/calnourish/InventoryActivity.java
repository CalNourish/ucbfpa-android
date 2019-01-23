package com.example.calnourish;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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

        Intent intent = getIntent();
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
                            //categoryToItem.put(val.get("categoryName"), val);
//                            items = getItems();
//                            recyclerView = findViewById(R.id.main_recycler);
//                            adapter = new ItemAdapter(items);
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InventoryActivity.this);
//                            recyclerView.setLayoutManager(layoutManager);
//                            recyclerView.setAdapter(adapter);
                        }
                        //val.put("count", "700000000");
                        //DBRef.updateChildren((Map) myText);
                        //dbResults.setText((String) myText);

//                        for(Map.Entry<String, HashMap<String, String>> entry : itemToCategory.entrySet()) {
//                            val = entry.getValue();
//                            String itemName = val.get("itemName");
//                            items = getItems(itemName, val.get("count"));
//                            recyclerView = findViewById(R.id.main_recycler);
//                            adapter = new ItemAdapter(items);
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InventoryActivity.this);
//                            recyclerView.setLayoutManager(layoutManager);
//                            recyclerView.setAdapter(adapter);
//                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        //Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
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

                    case R.id.foodrecovery:
                        Intent foodrecoveryIntent = new Intent(InventoryActivity.this, FoodRecoveryActivity.class);
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
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> item = new ArrayList<>();
        HashMap<String, Object> val = new HashMap<>();
        for(Map.Entry<String, Object> entry : itemToCategory.entrySet()) {
            val = (HashMap<String, Object>)entry.getValue();
            for(Map.Entry<String, Object> entry1 : val.entrySet()) {
                if (val.get("itemName").equals(itemName)) {
                    item.add(new Item((String) val.get("itemName"), (String) val.get("cost"),
                            (String) val.get("count"), (String) val.get("imageUrl")));
                    break;
                }
                if (((HashMap) val.get("categoryName")).containsValue(categoryName)) {
                    item.add(new Item((String) val.get("itemName"), (String) val.get("cost"),
                            (String) val.get("count"), (String) val.get("imageUrl")));
                    break;
                }
            }
        }
//        items.add(new Item("Item Name", categoryName));
//        items.add(new Item("Item Name", "Item Count"));
//        items.add(new Item("Item Name", "Item Count"));
        return item;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
