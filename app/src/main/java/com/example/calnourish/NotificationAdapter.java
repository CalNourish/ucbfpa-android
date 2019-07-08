package com.example.calnourish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private ArrayList<Notification> notifications;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public NotificationAdapter(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    } //constructor

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.notification_card_activity, parent, false);
        context = view.getContext();
        return new NotificationAdapter.NotificationViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        holder.title.setText(notifications.get(position).getTitle());
        holder.body.setText(notifications.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;
        Space spacer;
        View view;

        NotificationViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.notification_title);
            body = (TextView) itemView.findViewById(R.id.notification_body);
        }
    }
}
