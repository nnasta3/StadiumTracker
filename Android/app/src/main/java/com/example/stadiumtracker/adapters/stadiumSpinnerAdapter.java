package com.example.stadiumtracker.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Stadium;

import java.util.ArrayList;

public class stadiumSpinnerAdapter extends ArrayAdapter<Stadium> {
    private static class ViewHolder{
        private TextView itemView;
    }

    public stadiumSpinnerAdapter(Context context, int textViewResourceID, ArrayList<Stadium> list){
        super(context,textViewResourceID,list);
    }


}