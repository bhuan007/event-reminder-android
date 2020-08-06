package com.example.datereminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.datereminder.AddEventActivity.EVENT_STRING;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private static final String TAG = "EventAdapter";


    private ArrayList<Event> events = new ArrayList<>();
    private Context context;
    private Calendar calendar = Calendar.getInstance();
    private Date today = new Date();

    public EventAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final DatabaseHelper db = new DatabaseHelper(context);

        Log.d(TAG, "onBindViewHolder: called");
        holder.txtName.setText(events.get(position).getName());
        // TODO: May need to refactor this
        Calendar eventSet = Calendar.getInstance();
        eventSet.setTimeInMillis(events.get(position).getEventDate());
        String dateSet = new SimpleDateFormat("MMM dd, yyyy h:mm a").format(eventSet.getTime());
        holder.txtDate.setText(dateSet);

        //TODO: set txtcountdown to a format such as: 10 DAY / 2 HRS
        calendar.setTime(today);
        long timeDiff =  events.get(position).getEventDate() - calendar.getTimeInMillis();
        long secondsDay = timeDiff/1000;
        long hours = (secondsDay / 3600);
        long minutes = (secondsDay / 60) % 60;

        // if difference is less than a day

        if (timeDiff < 0) {
            holder.txtCountDown.setText("EVENT ENDED");
            holder.txtRemain.setVisibility(View.GONE);
            holder.eventContainer.setAlpha((float) 0.3);
        }
        else if (timeDiff < 86400000) {
            String countDownFormat = String.format("%s HRS %s MINS", hours, minutes);
            holder.txtCountDown.setText(countDownFormat);
        }
        else {
            String countDownFormat = new SimpleDateFormat("d 'DAYS' k 'HRS'").format(timeDiff);
            holder.txtCountDown.setText(countDownFormat);
        }





        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Code for deleting a list here
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.delete_message) + " " + events.get(position).getName() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // delete from database and set to arraylist on adapter
                                Event event = events.get(position);
                                db.deleteOne(event);

                                events = (ArrayList<Event>) db.getAllEvents();
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User canceled
                            }
                        });
                builder.create().show();


            }
        });

        holder.eventContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Code for opening edit event activity here need to pass event id
                try {
                    Intent intent = new Intent(context, AddEventActivity.class);
                    intent.putExtra(EVENT_STRING, events.get(position));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "Oops! An error occurred.", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void addEvent(Event event) {
        events.add(event);
        notifyDataSetChanged();

    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout eventContainer;
        private TextView txtName, txtDate, txtCountDown, txtRemain;
        private Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            eventContainer = itemView.findViewById(R.id.eventContainer);
            txtCountDown = itemView.findViewById(R.id.txtCountDown);
            txtRemain = itemView.findViewById(R.id.txtRemain);

        }
    }
}
