package com.example.calnourish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

        Date timestamp = notifications.get(position).getTimestamp();
        String parsedTimestamp = parseTimestamp(timestamp);
        holder.timestamp.setText(parsedTimestamp);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public String parseTimestamp(Date timestamp) {
        int diffInMinutes = (int)( (new Date().getTime() - timestamp.getTime())
                / (1000 * 60) );
        int diffInHours = (int)( (new Date().getTime() - timestamp.getTime())
                / (1000 * 60 * 60) );
        int diffInDays = (int)( (new Date().getTime() - timestamp.getTime())
                / (1000 * 60 * 60 * 24) );
        int diffInWeeks = (int)( (new Date().getTime() - timestamp.getTime())
                / (1000 * 60 * 60 * 24 * 7) );

        if (diffInHours < 1) {
            return (diffInMinutes == 1) ? diffInMinutes + " minute ago" : diffInMinutes + " minutes ago";
        } else if (diffInDays < 1) {
            DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
            String formattedTime = dateFormat.format(timestamp);
            return "Today at " + formattedTime;
        } else if (diffInDays < 2){
            return "Yesterday at " + timestamp.getHours() + ":" + timestamp.getMinutes();
        } else {
            //handle actual date
            List<String> months = Arrays.asList("January", "February", "March",
                    "April", "May", "June", "July",
                    "August", "September", "October",
                    "November", "December");
            return months.get(timestamp.getMonth()) + " " + timestamp.getDay();
        }
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;
        TextView timestamp;
        View view;

        NotificationViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.notification_title);
            body = (TextView) itemView.findViewById(R.id.notification_body);
            timestamp = (TextView) itemView.findViewById((R.id.timestamp));
        }
    }
}
