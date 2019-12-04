package com.example.stadiumtracker.helpers;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.VisitViewActivity;
import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.getStadium;

import java.util.List;

public class VisitListAdapter implements ListAdapter {
    private List<Event> events;
    private Context context;
    private User user;

    public VisitListAdapter(List<Event> events, Context context, User user){
        this.events=events;
        this.context=context;
        this.user=user;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = events.get(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.visit_list_row, null);
            TextView teamsText = convertView.findViewById(R.id.visit_list_row_teams);
            TextView stadiumText = convertView.findViewById(R.id.visit_list_row_stadium);
            TextView resultText = convertView.findViewById(R.id.visit_list_row_result);
            String temp = event.getRoadTeam().getNickname() + " @ " + event.getHomeTeam().getNickname() + " ("+event.getLeague()+")";
            teamsText.setText(temp);
            temp = event.getStadium()+" - "+event.getDateFullString();
            stadiumText.setText(temp);
            temp = event.getRoadScore()+" - "+event.getHomeScore();
            resultText.setText(temp);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent switch to stadium view
                Intent intent = new Intent(context, VisitViewActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("event",event);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return events.size();
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
