package com.example.calnourish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ThreadLocalRandom;

public class IndividualItemActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewCount;
    private ImageView imageView;
    private String itemName;
    private String itemPoint;
    private String itemCount;
    private String itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_item);
        textViewName = (TextView) findViewById(R.id.individualItemName);
        textViewCount = (TextView) findViewById(R.id.individualItemCount);
        imageView = (ImageView) findViewById(R.id.individualItemImage);
        Intent intent = getIntent();
        itemName = intent.getStringExtra("item_name");
        itemImage = intent.getStringExtra("item_image");
        itemCount = intent.getStringExtra("item_count");

        textViewName.setText(itemName);
        textViewCount.setText(itemCount);

        String placeholderImage = "placeholder" + Integer.toString(ThreadLocalRandom.current().nextInt(1, 5 + 1));
        int placeholderImageId = this.getResources().getIdentifier(placeholderImage, "drawable", this.getPackageName());
        Picasso.get()
                .load(itemImage)
                .error(placeholderImageId)
                .placeholder(placeholderImageId)
                .into(imageView);
    }
}
