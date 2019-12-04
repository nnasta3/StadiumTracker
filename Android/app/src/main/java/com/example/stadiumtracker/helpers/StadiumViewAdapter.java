package com.example.stadiumtracker.helpers;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.StadiumViewActivity;
import com.example.stadiumtracker.VisitViewActivity;
import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.User;

import java.util.Calendar;
import java.util.List;

public class StadiumViewAdapter implements ListAdapter {
    private Context context;
    private List<Event> events;
    private User user;
    private int numVisits;

    public StadiumViewAdapter(Context context, List<Event> events, User user, int numVisits){
        this.context=context;
        this.events=events;
        this.user=user;
        this.numVisits=numVisits;
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
        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.stadium_view_row, null);
            TextView teams = (TextView) convertView.findViewById(R.id.stadium_view_row_teams);
            TextView sub = (TextView) convertView.findViewById(R.id.stadium_view_row_sub);
            TextView res = (TextView) convertView.findViewById(R.id.stadium_view_row_res);
            String temp = event.getRoadTeam().getNickname()+" @ "+event.getHomeTeam().getNickname();
            teams.setText(temp);
            Calendar cal = event.getDate();
            temp = event.getLeague()+" - "+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR);
            sub.setText(temp);
            temp = event.getRoadScore()+" - "+event.getHomeScore();
            res.setText(temp);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent switch to stadium view
                Intent intent = new Intent(context, VisitViewActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("event",event);
                intent.putExtra("numVisits",numVisits);
                intent.putExtra("from","stadiumView");
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
