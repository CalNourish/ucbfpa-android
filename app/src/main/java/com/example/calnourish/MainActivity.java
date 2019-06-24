package com.example.calnourish;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView message;
    private CategoryAdapter adapter;
    private ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        categories = getCategories();
        recyclerView = findViewById(R.id.main_recycler);
        adapter = new CategoryAdapter(categories);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpace(2, 50, true));
        recyclerView.setAdapter(adapter);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.info:
                        Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
                        startActivity(infoIntent);
                        break;
                    case R.id.category:
//                        Intent categoryIntent = new Intent(MainActivity.this, MainActivity.class);
//                        startActivity(categoryIntent);
                        break;
                    case R.id.search:
//                        SearchView searchView = (SearchView) findViewById(R.id.search_bar);
//                        searchView.onActionViewExpanded();
                        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;
                    case R.id.foodrecovery:
                        Intent foodrecoveryIntent = new Intent(MainActivity.this, FoodRecoveryActivity.class);
                        startActivity(foodrecoveryIntent);
                        break;
                    case R.id.menu:
                        Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                        break;
                }
                return false;
            }
        });

    }

    /**
     * Temporary method for generating categories.
     */
    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();

        categories.add(new Category("New Items", R.drawable.water));
        categories.add(new Category("Favorites", R.drawable.favorites));
        categories.add(new Category("Prepared Foods", R.drawable.prepared));
        categories.add(new Category("Grains", R.drawable.grains));
        categories.add(new Category("Produce", R.drawable.produce));

        categories.add(new Category("Bread", R.drawable.bread));
        categories.add(new Category("Protein", R.drawable.eggs));
        categories.add(new Category("Frozen", R.drawable.frozen));
        categories.add(new Category("Snacks", R.drawable.yogurt));
        categories.add(new Category("Sauces", R.drawable.sauces));

        categories.add(new Category("Spices", R.drawable.spices));
        categories.add(new Category("Beverages", R.drawable.milk));

        return categories;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}

