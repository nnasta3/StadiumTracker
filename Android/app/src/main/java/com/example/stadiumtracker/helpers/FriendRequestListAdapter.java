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
import com.example.stadiumtracker.R;
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

    /*NICHOLAS NASTA
     * Creates a new CompareVisitsListAdapter
     */
    public FriendRequestListAdapter(ArrayList<String> list, Context context,User user) {
        this.list = list;
        this.context = context;
        FriendRequestListContext = context;
        passedUser = user;
    }

    /* NICHOLAS NASTA
     * Returns the size of the Friend Name list
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /* NICHOLAS NASTA
     * Returns the Friend Name from the list of Friend Names
     */
    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    /* NICHOLAS NASTA
     * Returns 0, since there are no IDs associated with Friend Names
     */
    @Override
    public long getItemId(int pos) {
        return 0;
    }

    /* NICHOLAS NASTA
     * Formats TextView and displays string from list of Friend Names
     * Buttons for adding and deleting friend requests instantiated
     * onClickListeners for buttons
     * addBtn adds accepts a friend request
     * deleteBtn deletes the friend request
     */
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
