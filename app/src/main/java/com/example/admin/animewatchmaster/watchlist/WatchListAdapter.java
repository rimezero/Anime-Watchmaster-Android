package com.example.admin.animewatchmaster.watchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.admin.animewatchmaster.R;

import java.util.ArrayList;

/**
 * Created by abraham on 10/6/2016.
 */
public class WatchListAdapter extends ArrayAdapter<WatchListModel> {


    public WatchListAdapter(Context context,ArrayList<WatchListModel> models) {
        super(context,0,models);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final WatchListModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_watchlist_row,parent,false);
        }



        return convertView;
    }


}
