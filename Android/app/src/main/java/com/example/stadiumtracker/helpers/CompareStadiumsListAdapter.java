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
import com.example.stadiumtracker.data.Stadium;
import java.util.ArrayList;
import java.util.List;

public class CompareStadiumsListAdapter extends BaseAdapter implements ListAdapter {

    private List<Stadium> list;
    private Context context;
    private ArrayList<Integer> userStadiumIDs;
    private ArrayList<Integer> friendStadiumIDs;

    /* NICHOLAS NASTA
     * Creates a new CompareStadiumsListAdapter
     */
    public CompareStadiumsListAdapter(List<Stadium> list, Context context, ArrayList<Integer> userStadiumIDs, ArrayList<Integer> friendStadiumIDs) {
        this.list = list;
        this.context = context;
        this.userStadiumIDs = userStadiumIDs;
        this.friendStadiumIDs = friendStadiumIDs;
    }

    /* NICHOLAS NASTA
     * Returns the size of the Stadiums list
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /* NICHOLAS NASTA
     * Returns the Stadium in the list of Stadiums
     */
    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    /* NICHOLAS NASTA
     * Returns the StadiumID in the list of Stadiums
     */
    @Override
    public long getItemId(int pos) {
        return list.get(pos).getStadiumID();
    }

    /* NICHOLAS NASTA
     * Changes the ImageViews for user/friend depending on if a user/friend has been to that stadium
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
        SpannableString s = new SpannableString(list.get(position).getName() + "\n" + list.get(position).getCity() + ", " + list.get(position).getCountry());
        s.setSpan(new RelativeSizeSpan(2),0,list.get(position).getName().length(),0);
        s.setSpan(new RelativeSizeSpan(1),list.get(position).getName().length(),list.get(position).getName().length()+list.get(position).getCity().length() + list.get(position).getCountry().length()+2,0);
        listItemText.setText(s);

        //Change ImageViews depending on if a user/friend has been to that stadium
        ImageView userImage = (ImageView) view.findViewById(R.id.user_image_view);
        ImageView friendImage = (ImageView)view.findViewById(R.id.friend_image_view);

        if(!userStadiumIDs.contains(list.get(position).getStadiumID())){
            userImage.setImageResource(R.drawable.baseline_close_black_18dp);
        }
        else{
            userImage.setImageResource(R.drawable.baseline_check_black_18dp);
        }

        if(!friendStadiumIDs.contains(list.get(position).getStadiumID())){
            friendImage.setImageResource(R.drawable.baseline_close_black_18dp);
        }
        else{
            friendImage.setImageResource(R.drawable.baseline_check_black_18dp);
        }

        return view;
    }
}
