package com.example.stadiumtracker.helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stadiumtracker.FriendRequestsActivity;
import com.example.stadiumtracker.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.acceptFriendRequest;
import com.example.stadiumtracker.database.getFriendIDFromName;
import com.example.stadiumtracker.database.deleteFriendRequest;


public class FriendRequestListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private User passedUser;
    private Context FriendRequestListContext;


    public FriendRequestListAdapter(ArrayList<String> list, Context context,User user) {
        this.list = list;
        this.context = context;
        FriendRequestListContext = context;
        passedUser = user;
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
            view = inflater.inflate(R.layout.friend_request_list_layout, null);
        }
        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.senderName);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.deleteButton);
        Button addBtn = (Button)view.findViewById(R.id.addButton);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Delete this friend request
                try {
                    int senderID = new getFriendIDFromName(FriendRequestListContext).execute(list.get(position)).get();
                    new deleteFriendRequest(FriendRequestListContext).execute(senderID,passedUser.getUserID());
                }
                catch(Exception e){
                    Log.w("Error deleteFriendRequest query", "" + e);
                }
                Toast.makeText(FriendRequestListContext,"Friend Request From "+ list.get(position)+" Has Been Deleted",Toast.LENGTH_LONG).show();
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Add Friend and delete this friend request
                try {
                    int senderID = new getFriendIDFromName(FriendRequestListContext).execute(list.get(position)).get();
                    new acceptFriendRequest(FriendRequestListContext).execute(senderID,passedUser.getUserID());
                    new deleteFriendRequest(FriendRequestListContext).execute(senderID,passedUser.getUserID());
                }
                catch(Exception e){
                    Log.w("Error getFriendIDFromName query", "" + e);
                }
                Toast.makeText(FriendRequestListContext,"Friend Request From "+ list.get(position)+" Has Been Accepted",Toast.LENGTH_LONG).show();
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
