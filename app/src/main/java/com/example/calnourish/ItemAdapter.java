package com.example.calnourish;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private OnItemClickListener mListener;
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
        return new ItemAdapter.ItemViewHolder(view, mListener);
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
                    .load(R.drawable.blue_app_logo)
//                    .load(items.get(position).getItemImage())
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

        ItemViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            Card = (CardView) itemView.findViewById(R.id.cardView);
            itemName = (TextView) itemView.findViewById(R.id.notification_title);
            itemPoint = (TextView) itemView.findViewById(R.id.notification_body);
            itemCount = (TextView) itemView.findViewById(R.id.itemCount);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                             listener.onItemClick(position );
                        }
                    }

                }
            });
        }
    }
}
