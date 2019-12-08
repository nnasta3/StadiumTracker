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
import com.example.stadiumtracker.data.User;

import java.util.List;

public class StadiumListAdapter implements ListAdapter {
    private List<StadiumListHelper> stadiumListHelpers;
    private Context context;
    private User user;
    /* John Strauser
        constructor for the adapter used in the StadiumListActivity
        context and user are used in switching intent to StadiumViewActivity
        stadiumListHelpers is the list of information to be displayed
     */
    public StadiumListAdapter(Context context, List<StadiumListHelper> stadiumListHelpers, User user){
        this.context = context;
        this.stadiumListHelpers = stadiumListHelpers;
        this.user = user;
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
        return stadiumListHelpers.size();
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
        the only function that really matters here
        takes the information from stadiumListHelpers and converts it to a displayable list item
        also sets on click to send user to StadiumViewActivity
     */
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
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent switch to stadium view
                Intent intent = new Intent(context, StadiumViewActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("stadium",stadiumListHelper.getStadium());
                intent.putExtra("numVisits",stadiumListHelper.getVisits());
                intent.putExtra("from","stadiumList");
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
        return stadiumListHelpers.size();
    }
    /* John Strauser
        required function from the ListAdapter interface
     */
    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
