package com.example.calnourish;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.example.calnourish.Category;
import com.example.calnourish.CategoryAdapter;
import com.example.calnourish.Item;
import com.example.calnourish.ItemAdapter;
import com.example.calnourish.InventoryActivity;
import com.example.calnourish.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    private Context context;

    public ItemAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_card_activity, parent, false);
        context = view.getContext();
        return new ItemAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ItemViewHolder holder, final int position) {
        holder.itemName.setText(items.get(position).getItemName());

        int pointValue = Integer.parseInt(items.get(position).getItemPoint());
        if (pointValue == 1) {
            holder.itemPoint.setText(pointValue + " point");
        } else {
            holder.itemPoint.setText(pointValue + " points");
        }

        int count = Integer.parseInt(items.get(position).getItemCount());
        holder.itemCount.setText(String.valueOf(count) + " IN STOCK");

        if (count <= 0) {
            holder.itemCount.setText("NOT IN STOCK");
            holder.itemCount.setTextColor(ContextCompat.getColor(context, R.color.calNourishText));
            holder.Card.setAlpha((float) 0.4);
        } else if (count < 10) {
            holder.itemCount.setTextColor(ContextCompat.getColor(context, R.color.calNourishRed));
        } else if (count < 20) {
            holder.itemCount.setTextColor(ContextCompat.getColor(context, R.color.calNourishAccent));
        } else {
            holder.itemCount.setTextColor(ContextCompat.getColor(context, R.color.calNourishGreen));
        }

        System.out.println(items.get(position).getItemImage());
        String placeholderImage = "placeholder" + Integer.toString(ThreadLocalRandom.current().nextInt(1, 5 + 1));
        int placeholderImageId = context.getResources().getIdentifier(placeholderImage, "drawable", context.getPackageName());

        if (items.get(position).getItemImage() != null) {
            Picasso.get()
                    .load(items.get(position).getItemImage())
                    .error(placeholderImageId)
                    .placeholder(placeholderImageId)
                    .into(holder.itemImage);
        }

//        int imageId = context.getResources().
//                getIdentifier("drawable/" + items.get(position).getItemImage(), null, context.getPackageName());
//
//        // If image doesn't exist
//        if (imageId == 0) {
//            holder.itemImage.setBackgroundColor(ContextCompat.getColor(context, R.color.calNourishAccent));
//        } else {
//            holder.itemImage.setImageResource(imageId);
//        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView Card;
        TextView itemName;
        TextView itemPoint;
        TextView itemCount;
        ImageView itemImage;

        ItemViewHolder(View itemView) {
            super(itemView);
            Card = (CardView) itemView.findViewById(R.id.cardView);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemPoint = (TextView) itemView.findViewById(R.id.itemPoint);
            itemCount = (TextView) itemView.findViewById(R.id.itemCount);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }
}
