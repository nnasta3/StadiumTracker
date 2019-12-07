package com.example.stadiumtracker.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.acceptFriendRequest;
import com.example.stadiumtracker.database.deleteFriendRequest;
import com.example.stadiumtracker.database.getFriendIDFromName;
import com.example.stadiumtracker.database.selectDistinctStadiumsVisited;

import java.util.ArrayList;
import java.util.List;

public class CompareStadiumsListAdapter extends BaseAdapter implements ListAdapter {

    private List<Stadium> list;
    private Context context;
    private User passedUser;
    private Context CompareStadiumsContext;
    private int friendID;
    private ArrayList<Integer> userStadiumIDs;
    private ArrayList<Integer> friendStadiumIDs;


    public CompareStadiumsListAdapter(List<Stadium> list, Context context, User user, int friend, ArrayList<Integer> userStadiumIDs, ArrayList<Integer> friendStadiumIDs) {
        this.list = list;
        this.context = context;
        CompareStadiumsContext = context;
        passedUser = user;
        friendID = friend;
        this.userStadiumIDs = userStadiumIDs;
        this.friendStadiumIDs = friendStadiumIDs;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

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
