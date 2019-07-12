package com.example.calnourish;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ThreadLocalRandom;

public class IndividualItemActivity extends AppCompatActivity {
    private Button searchBar;
    private TextView textViewName;
    private TextView textViewCount;
    private ImageView imageView;
    private String itemName;
//    private String itemPoint;
    private String itemCount;
    private String itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_individual_item);
        searchBar = (Button) findViewById(R.id.search_bar2);
        textViewName = (TextView) findViewById(R.id.individualItemName);
        textViewCount = (TextView) findViewById(R.id.individualItemCount);
        imageView = (ImageView) findViewById(R.id.individualItemImage);

        Intent intent = getIntent();
        itemName = intent.getStringExtra("item_name");
        itemImage = intent.getStringExtra("item_image");
        itemCount = intent.getStringExtra("item_count");
        textViewName.setText(itemName);
        setTextViewCount();
        setImageView();

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(IndividualItemActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.info:
                        Intent infoIntent = new Intent(IndividualItemActivity.this, InfoActivity.class);
                        startActivity(infoIntent);
                        break;

                    case R.id.category:
                        Intent categoryIntent = new Intent(IndividualItemActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;

                    case R.id.search:
                        Intent searchIntent = new Intent(IndividualItemActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;

                    case R.id.notifications:
                        Intent foodrecoveryIntent = new Intent(IndividualItemActivity.this, NotificationListActivity.class);
                        startActivity(foodrecoveryIntent);
                        break;

                    case R.id.menu:
                        Intent menuIntent = new Intent(IndividualItemActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                        break;
                }


                return false;
            }
        });
    }

    public void setTextViewCount() {
        textViewCount.setText(itemCount + " IN STOCK");
        if (Integer.parseInt(itemCount) <= 0) {
            textViewCount.setText("NOT IN STOCK");
            textViewCount.setTextColor(ContextCompat.getColor(this, R.color.calNourishText));
        } else if (Integer.parseInt(itemCount) < 10) {
            textViewCount.setTextColor(ContextCompat.getColor(this, R.color.calNourishRed));
        } else if (Integer.parseInt(itemCount) < 20) {
            textViewCount.setTextColor(ContextCompat.getColor(this, R.color.calNourishAccent));
        } else {
            textViewCount.setTextColor(ContextCompat.getColor(this, R.color.calNourishGreen));
        }
    }

    public void setImageView() {
        String placeholderImage = "placeholder" + Integer.toString(ThreadLocalRandom.current().nextInt(1, 5 + 1));
        int placeholderImageId = this.getResources().getIdentifier(placeholderImage, "drawable", this.getPackageName());
        Picasso.get()
                .load(itemImage)
                .error(placeholderImageId)
                .placeholder(placeholderImageId)
                .into(imageView);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
