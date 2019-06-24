package com.example.calnourish;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Controls how each CATEGORY card gets shown/displayed in the Categories view.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

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

