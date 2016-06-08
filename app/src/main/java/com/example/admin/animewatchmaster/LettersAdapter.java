package com.example.admin.animewatchmaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abraham on 9/6/2016.
 */
public class LettersAdapter extends ArrayAdapter<String> {

    public LettersAdapter(Context context, List<String> models) {
        super(context,0,models);
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        String letter = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_letters_row,parent,false);
        }

        TextView textViewLetter = (TextView)convertView.findViewById(R.id.letter);
        textViewLetter.setText(letter);

        return convertView;
    }

}
