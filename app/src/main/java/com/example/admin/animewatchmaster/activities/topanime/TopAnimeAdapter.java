package com.example.admin.animewatchmaster.activities.topanime;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.model.TopanimeModel;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 2/7/2016.
 */
public class TopAnimeAdapter extends ArrayAdapter<TopanimeModel> {

    public TopAnimeAdapter(Context context,List<TopanimeModel> models) {
        super(context,0,models);
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent){

        final TopanimeModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_top_anime_row,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        if(model.getImgurl() != null && !model.getImgurl().trim().isEmpty()) {
            Picasso.with(getContext())
                    .load(model.getImgurl())
                    .fit()
                    .into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Anime anime = DBHelper.getInstance(getContext()).getAnimeInfo(model.getId());

                Intent intent = new Intent(getContext(), AnimeInfo.class);
                intent.putExtra("anime", anime);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }
        });

        AutofitTextView textView = (AutofitTextView)convertView.findViewById(R.id.title);
        textView.setText(model.getTitle());

        TextView score = (TextView)convertView.findViewById(R.id.score);
        score.setText("Score | "+model.getScore());

        final LinearLayout morelinear = (LinearLayout)convertView.findViewById(R.id.morelinear);

        ImageView moreimage = (ImageView)convertView.findViewById(R.id.moreimage);
        moreimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(morelinear.getVisibility() == View.VISIBLE) {

                    morelinear.setVisibility(View.GONE);

                } else {

                    morelinear.setAlpha(0f);
                    morelinear.setVisibility(View.VISIBLE);
                    morelinear.animate()
                            .alpha(1f)
                            .setDuration(400)
                            .start();
                }

            }
        });


        TextView addwatched = (TextView)convertView.findViewById(R.id.addwatched);
        TextView addwatchlist = (TextView)convertView.findViewById(R.id.addwatchlist);
        TextView addwatchlater = (TextView)convertView.findViewById(R.id.addwatchlater);
        TextView addmore = (TextView)convertView.findViewById(R.id.addmore);


        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //future staff like share..
            }
        });

        addwatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                morelinear.setVisibility(View.GONE);
                //do dbstuff

            }
        });


        addwatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                morelinear.setVisibility(View.GONE);
                //do dbstuff

            }
        });


        addwatchlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                morelinear.setVisibility(View.GONE);
                //do dbstuff

            }
        });


        return convertView;
    }


}
