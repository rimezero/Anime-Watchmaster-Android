package com.example.admin.animewatchmaster.watchlater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.model.WatchlaterlistModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 6/10/2016.
 */
public class WatchLaterAdapter extends ArrayAdapter<WatchlaterlistModel> {
    public WatchLaterAdapter(Context context, List<WatchlaterlistModel> models) {
        super(context,0,models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final WatchlaterlistModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_watchlaterlist_row,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);


        if(model.getImgurl() != null && !model.getImgurl().trim().isEmpty()) {
            Picasso.with(getContext())
                    .load(model.getImgurl())
                    .into(imageView);
        }

        TextView textView = (TextView)convertView.findViewById(R.id.txtTitle);
        textView.setText(model.getTitle());

        TextView txtGenre = (TextView)convertView.findViewById(R.id.txtGenre);
        if(model.getGenre()!=null && !model.getGenre().trim().isEmpty())
            txtGenre.setText(model.getGenre());

        Button btn = (Button)convertView.findViewById(R.id.BtnRemove);
        btn.setOnClickListener(new RemoveOnClick(model));

        return convertView;
    }

    private class RemoveOnClick implements View.OnClickListener{
        private WatchlaterlistModel model;

        public RemoveOnClick(WatchlaterlistModel model) {
            this.model = model;
        }

        @Override
        public void onClick(View v) {
            DBHelper.getInstance(getContext()).deleteWatchlaterlistAnime(model.getId());
            remove(model);
        }
    }
}
