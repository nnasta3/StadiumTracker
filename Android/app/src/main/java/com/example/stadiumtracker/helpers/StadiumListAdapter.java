package com.example.stadiumtracker.helpers;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.stadiumtracker.R;

import java.util.List;

public class StadiumListAdapter implements ListAdapter {
    private List<StadiumListHelper> stadiumListHelpers;
    private Context context;

    public StadiumListAdapter(Context context, List<StadiumListHelper> stadiumListHelpers){
        this.context = context;
        this.stadiumListHelpers = stadiumListHelpers;
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
        return stadiumListHelpers.size();
    }

    @Override
    public Object getItem(int position) {
        return stadiumListHelpers.get(position);
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
        StadiumListHelper stadiumListHelper = stadiumListHelpers.get(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.stadiums_list_row, null);
            TextView stadiumName = convertView.findViewById(R.id.stadiums_list_row_name);
            TextView city = convertView.findViewById(R.id.stadiums_list_row_city);
            TextView numVisits = convertView.findViewById(R.id.stadiums_list_row_visits);
            stadiumName.setText(stadiumListHelper.getStadium().getName());
            String cityString = stadiumListHelper.getStadium().getCity() + ", " + stadiumListHelper.getStadium().getCountry();
            city.setText(cityString);
            numVisits.setText(String.valueOf(stadiumListHelper.getVisits()));
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return stadiumListHelpers.size();
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
