package com.example.calnourish;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private FirebaseDatabase FDB;
    private DatabaseReference DBRef;
    private HashMap<String, HashMap<String, String>> inventoryMap = new HashMap<>();
    private ArrayList <String> items = new ArrayList<>();
    private ArrayList <String> list = new ArrayList<>();
    private ArrayAdapter <String> adapter;
    private ListView listView;
    private String strTemp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("inventory");

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.onActionViewExpanded();
        //CharSequence query = searchView.getQuery();
        listView = (ListView) findViewById(R.id.lv1);

        final ArrayList<String> arrayStr = new ArrayList<>();

        //BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        //Menu menu = bottomNavigationView.getMenu();
        //MenuItem menuItem = menu.getItem(2);
        //menuItem.setChecked(true);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        MenuItem search = menu.findItem(R.id.search);
//        searchView = (SearchView) search.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        final Button updateButton = (Button) findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        inventoryMap = (HashMap)dataSnapshot.getValue();
                        HashMap<String, String> val = new HashMap<>();
                        for(Map.Entry<String, HashMap<String, String>> entry : inventoryMap.entrySet()) {
                            val = entry.getValue();
                            String itemName = val.get("itemName");
                            items.add(itemName);
                            list.add(itemName.toLowerCase());
                        }
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

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(items.contains(query) || list.contains(query)){
                    adapter.getFilter().filter(query);
                    arrayStr.add(query);
                } else {
                    Toast.makeText(SearchActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(arrayStr.size() > 0) {
                    strTemp = arrayStr.get(0);
                } else {
                    strTemp = items.get(position);
                }
                String[] arr = strTemp.split(" ", 5);
                String holderStr = "";
                for (String str : arr) {
                    holderStr += str.substring(0, 1).toUpperCase() + str.substring(1) + " ";
                }

                Intent intent = new Intent(SearchActivity.this, InventoryActivity.class);
                intent.putExtra("itemName", holderStr.substring(0, holderStr.length()-1));
                intent.putExtra("category", "");
                startActivity(intent);
            }
        });

        //searchView.onActionViewExpanded();



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.info:
                        Intent infoIntent = new Intent(SearchActivity.this, InfoActivity.class);
                        startActivity(infoIntent);
                        break;

                    case R.id.category:
                        Intent categoryIntent = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;

                    case R.id.search:
                        Intent searchIntent = new Intent(SearchActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;

                    case R.id.foodrecovery:
                        Intent foodrecoveryIntent = new Intent(SearchActivity.this, FoodRecoveryActivity.class);
                        startActivity(foodrecoveryIntent);
                        break;

                    case R.id.menu:
                        Intent menuIntent = new Intent(SearchActivity.this, MenuActivity.class);
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



/*package com.example.calnourish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String categoryStr = intent.getStringExtra("category");

        TextView title = (TextView) findViewById(R.id.text1);
        title.setText(categoryStr);

        SearchView searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.onActionViewExpanded();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.info:
                        Intent infoIntent = new Intent(SearchActivity.this, InfoActivity.class);
                        startActivity(infoIntent);
                        break;

                    case R.id.category:
                        Intent categoryIntent = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;

                    case R.id.search:
                        Intent searchIntent = new Intent(SearchActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;

                    case R.id.foodrecovery:
                        Intent foodrecoveryIntent = new Intent(SearchActivity.this, FoodRecoveryActivity.class);
                        startActivity(foodrecoveryIntent);
                        break;

                    case R.id.menu:
                        Intent menuIntent = new Intent(SearchActivity.this, MenuActivity.class);
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
}*/
