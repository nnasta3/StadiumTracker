package com.example.stadiumtracker.helpers;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Event;
import java.util.ArrayList;
import java.util.List;

public class CompareVisitsListAdapter extends BaseAdapter implements ListAdapter {

    private List<Event> list;
    private Context context;
    private ArrayList<Integer> userEventIDs;
    private ArrayList<Integer> friendEventIDs;

    /* NICHOLAS NASTA
     * Creates a new CompareVisitsListAdapter
     */
    public CompareVisitsListAdapter(List<Event> allEventList,Context context, ArrayList<Integer> userEvents, ArrayList<Integer> friendEvents) {
        this.list = allEventList;
        this.context = context;
        this.userEventIDs = userEvents;
        this.friendEventIDs = friendEvents;
    }

    /* NICHOLAS NASTA
     * Returns the size of the Event list
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /* NICHOLAS NASTA
     * Returns the Event in the list of Events
     */
    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    /* NICHOLAS NASTA
     * Returns the EventID in the list of Events
     */
    @Override
    public long getItemId(int pos) {
        return list.get(pos).getEventID();
    }

    /* NICHOLAS NASTA
     * Changes the ImageViews for user/friend depending on if a user/friend has been to that Event
     * Formats TextView and displays string from list of visits
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.compare_list_layout, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.compare_list_text_view);
        SpannableString s = new SpannableString(list.get(position).getRoadTeam().getNickname() + " @ " + list.get(position).getHomeTeam().getNickname() + "\n" + list.get(position).getStadium().getName() +" - "+ list.get(position).getDateFullString());
        s.setSpan(new RelativeSizeSpan(2),0,list.get(position).getRoadTeam().getNickname().length()+3+list.get(position).getHomeTeam().getNickname().length(),0);
        s.setSpan(new RelativeSizeSpan(1),list.get(position).getRoadTeam().getNickname().length()+3+list.get(position).getHomeTeam().getNickname().length(),list.get(position).getRoadTeam().getNickname().length() +list.get(position).getHomeTeam().getNickname().length() + list.get(position).getStadium().getName().length()+list.get(position).getDateFullString().length()+6,0);
        listItemText.setText(s);

        //Change ImageViews depending on if a user/friend has been to that Event
        ImageView userImage = (ImageView) view.findViewById(R.id.user_image_view);
        ImageView friendImage = (ImageView)view.findViewById(R.id.friend_image_view);

        if(!userEventIDs.contains(list.get(position).getEventID())){
            userImage.setImageResource(R.drawable.baseline_close_black_18dp);
        }
        else{
            userImage.setImageResource(R.drawable.baseline_check_black_18dp);
        }

        if(!friendEventIDs.contains(list.get(position).getEventID())){
            friendImage.setImageResource(R.drawable.baseline_close_black_18dp);
        }
        else{
            friendImage.setImageResource(R.drawable.baseline_check_black_18dp);
        }


        return view;
    }
}
