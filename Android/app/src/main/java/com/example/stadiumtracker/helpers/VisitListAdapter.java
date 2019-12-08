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
    /* John Strauser
        constructor for the list adapter for the VisitListActivity
        events is the list of events that will be displayed
        context is the context of the VisitListActivity, used in onItemClickListener of the list items
        user is the user instance of the VisitListActivity, used in onItemClickListener of the list items
     */
    public VisitListAdapter(List<Event> events, Context context, User user){
        this.events=events;
        this.context=context;
        this.user=user;
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public int getCount() {
        return events.size();
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public Object getItem(int position) {
        return position;
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }
    /* John Strauser
        required function from the ListAdapter interface
        The only function that really matter in this adapter
        creates the list item for each entry in the list
        also sets the onItemClistListener which sends the user to the VisitViewActivity if a list item is clicked
     */
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
                intent.putExtra("from","visitList");
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public int getViewTypeCount() {
        return events.size();
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
