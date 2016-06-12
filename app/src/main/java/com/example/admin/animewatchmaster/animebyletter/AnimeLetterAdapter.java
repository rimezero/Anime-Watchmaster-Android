package com.example.admin.animewatchmaster.animebyletter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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

    private String queryText;

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

        AutofitTextView text = (AutofitTextView)convertView.findViewById(R.id.title);

        //tha to sinexisw ali fora
        //einai gia na fenete sto title to komati pou ekane query o xristis
        if(queryText != null && !queryText.isEmpty()) {
            text.setText(model.getTitle());
        } else {
            text.setText(model.getTitle());
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
