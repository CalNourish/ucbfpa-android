package com.example.calnourish;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.lang.Math.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Controls how each CATEGORY card gets shown/displayed in the Categories view.
 */
public class  CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<Category> categories;
    private Context context;

    public CategoryAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_activity, parent, false);
        context = view.getContext();
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        holder.text.setText(categories.get(position).getText());

        if (categories.get(position).getDrawable() != null) {
            Picasso.get()
                    .load(categories.get(position).getDrawable())
                    .error(R.drawable.placeholder0)
                    .placeholder(R.drawable.placeholder0)
                    .into(holder.photo);
        } else {
            Picasso.get()
                    .load(categories.get(position).getPhoto())
                    .error(R.drawable.placeholder0)
                    .placeholder(R.drawable.placeholder0)
                    .into(holder.photo);
        }

        // TODO (jsluong): fill thi n s with the search for that particular category
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(context, InventoryActivity.class);
                intent.putExtra("category", categories.get(position).getText());
                intent.putExtra("itemName", "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView photo;
        Space spacer;
        View view;

        CategoryViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            text = (TextView) itemView.findViewById(R.id.text);
            photo = (ImageView) itemView.findViewById(R.id.photo);
        }
    }
}

