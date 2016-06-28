package com.example.admin.animewatchmaster.activities.animebyletter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.Anime;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 9/6/2016.
 */
public class AnimeLetterAdapter extends ArrayAdapter<Anime> {

    private String queryText = ActivityLetters.queryText;

    public AnimeLetterAdapter(Context context, List<Anime> models) {
        super(context, 0, models);
    }

    public void setQueryText(String qt) {
        this.queryText = qt;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        Anime model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_anime_by_letter_row,parent,false);
        }

        TextView titlestart = (TextView)convertView.findViewById(R.id.titlestart);
        TextView titlemiddle = (TextView)convertView.findViewById(R.id.titlemiddle);
        TextView titleend = (TextView)convertView.findViewById(R.id.titleend);


        String temptitle = model.getTitle().toLowerCase();
        String title = model.getTitle();
        if(queryText != null && !queryText.isEmpty()) {
            String tempqueryText = queryText;
            tempqueryText = tempqueryText.toLowerCase();

            if(temptitle.contains(tempqueryText) && (title.length() > queryText.length())) {
                //begin
                if(temptitle.startsWith(tempqueryText)) {

                    String sub = title.substring(queryText.length(),title.length());

                    titlestart.setText(queryText);
                    titlestart.setTextColor(Color.YELLOW);
                    titleend.setText(sub);
                    titleend.setTextColor(Color.WHITE);

                    titlemiddle.setText("");


                //end
                } else if(temptitle.endsWith(tempqueryText)) {

                    String sub = title.substring(0,(title.length()-queryText.length()));

                    titlestart.setText(sub);
                    titlestart.setTextColor(Color.WHITE);

                    titleend.setText(queryText);
                    titleend.setTextColor(Color.YELLOW);

                    titlemiddle.setText("");

                //middle
                } else {

                    String substart = title.substring(0,temptitle.lastIndexOf(tempqueryText));
                    String subend = title.substring(substart.length()+queryText.length(),title.length());

                    titlestart.setText(substart);
                    titlestart.setTextColor(Color.WHITE);

                    titlemiddle.setText(queryText);
                    titlemiddle.setTextColor(Color.YELLOW);

                    titleend.setText(subend);
                    titleend.setTextColor(Color.WHITE);
                }
            }
        } else {
            titlestart.setText(model.getTitle());
        }




        final ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.loading);
        imageView.setImageBitmap(bitmap);

        try {

            Target target = new Target()  {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    //do stuff if...
                    Log.i("onBitmapFailed","failed to get bitmap");
                    Bitmap errorBitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_signal_wifi_off_white_24dp);
                    imageView.setImageDrawable(null);
                    imageView.setImageBitmap(errorBitmap);

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.with(getContext())
                    .load(model.getImgurl())
                    .resize(150,200)
                    .into(target);

        } catch (Exception ex) {

        }

        AutofitTextView textView = (AutofitTextView)convertView.findViewById(R.id.type);
        textView.setText(model.getAnimetype());

        convertView.setAlpha(0f);
        convertView.animate()
                .alpha(1f)
                .setDuration(750)
                .start();

        return convertView;

    }





}
