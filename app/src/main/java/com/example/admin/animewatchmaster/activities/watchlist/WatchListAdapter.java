package com.example.admin.animewatchmaster.activities.watchlist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.model.WatchListModel;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 10/6/2016.
 */
public class WatchListAdapter extends ArrayAdapter<WatchListModel> {

    public WatchListAdapter(Context context,List<WatchListModel> models) {
        super(context,0,models);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final WatchListModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_watchlist_row,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        AutofitTextView title = (AutofitTextView)convertView.findViewById(R.id.title);
        title.setText(model.getTitle());


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

        final EditText watchedepisodes = (EditText)convertView.findViewById(R.id.episodeswatched);
        watchedepisodes.setText(""+model.getEpisodeswatched());

        final EditText currentepisodes = (EditText)convertView.findViewById(R.id.currentepisode);
        currentepisodes.setText(""+model.getCurrentEpisode());

        TextView lastupdate = (TextView)convertView.findViewById(R.id.lastupdate);
        if(model.getLastupdated() != null && !model.getLastupdated().trim().isEmpty()) {
            lastupdate.setVisibility(View.VISIBLE);
            lastupdate.setText("Last episode: "+model.getLastupdated()+" ago");
        } else {
            lastupdate.setText("");
            lastupdate.setVisibility(View.GONE);
        }

        //if(model.getCurrentEpisode() > model.getEpisodeswatched())
        //    watchedepisodes.setTextColor(Color.RED);
        //else
         //   watchedepisodes.setTextColor(Color.BLACK);


        Button btn = (Button)convertView.findViewById(R.id.BtnRemove);
        btn.setOnClickListener(new RemoveOnClick(model));

        RippleView btninc = (RippleView)convertView.findViewById(R.id.btnIncrement);
        RippleView btndec = (RippleView)convertView.findViewById(R.id.btnDecrement);

        btninc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DBHelper.getInstance(getContext()).incrementEpisodesWatched(model.getId()))
                    return;

                model.setEpisodeswatched(model.getEpisodeswatched() + 1);
                watchedepisodes.setText("" + model.getEpisodeswatched());
                currentepisodes.setText("" + model.getCurrentEpisode());
                // if(model.getCurrentEpisode() > model.getEpisodeswatched())
                //   watchedepisodes.setTextColor(Color.RED);
                // else
                //   watchedepisodes.setTextColor(Color.BLACK);

            }
        });

        btndec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!DBHelper.getInstance(getContext()).decrementEpisodesWatched(model.getId()))
                    return;


                model.setEpisodeswatched(model.getEpisodeswatched() - 1);
                watchedepisodes.setText("" + model.getEpisodeswatched());
                currentepisodes.setText("" + model.getCurrentEpisode());
                // if(model.getCurrentEpisode() > model.getEpisodeswatched())
                //    watchedepisodes.setTextColor(Color.RED);
                // else
                //    watchedepisodes.setTextColor(Color.BLACK);

            }
        });

        final LinearLayout linearLayoutsave = (LinearLayout)convertView.findViewById(R.id.save);

        watchedepisodes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (linearLayoutsave.getVisibility() == View.GONE) {
                    linearLayoutsave.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });

        currentepisodes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (linearLayoutsave.getVisibility() == View.GONE) {
                    linearLayoutsave.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });


        final View finalConvertView = convertView;
        linearLayoutsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayoutsave.setVisibility(View.GONE);

                try {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        //hide keyboar
                        imm.hideSoftInputFromWindow(finalConvertView.getWindowToken(), 0);
                    }
                } catch (Exception ex) {

                }

                String newcurrentepisodes = currentepisodes.getText().toString();

                try {
                    int currentepisodesnum = Integer.valueOf(newcurrentepisodes);
                    if (currentepisodesnum < model.getEpisodeswatched()) {
                        Log.i("WatchlistAdapter", "Currentepisodes value lower than episodeswatched");
                        currentepisodes.setText(String.valueOf(model.getCurrentEpisode()));
                        return;
                    }
                    if (currentepisodesnum > 999 || currentepisodesnum < 0) {
                        Log.i("WatchlistAdapter", "Currentepisode index out of bounds");
                        currentepisodes.setText(String.valueOf(model.getCurrentEpisode()));
                        return;
                    }

                    model.setCurrentEpisode(currentepisodesnum);
                    saveEpisodes(model.getId(), currentepisodesnum, null);
                    //DBHelper.getInstance(getContext()).updateWatchlistAnimeEps(model.getId(), currentepisodesnum, null);

                    String newwatchedepisodevalue = watchedepisodes.getText().toString();

                    int watchedepisodenum = Integer.valueOf(newwatchedepisodevalue);
                    if (watchedepisodenum > model.getCurrentEpisode()) {
                        Log.i("WatchlistAdapter", "Episodeswatched value greater than currentepisodes");
                        watchedepisodes.setText(String.valueOf(model.getEpisodeswatched()));
                        return;
                    }
                    if (watchedepisodenum > 999 || watchedepisodenum < 0) {
                        Log.i("WatchlistAdapter", "Episodeswatched index out of bounds");
                        watchedepisodes.setText(String.valueOf(model.getEpisodeswatched()));
                        return;
                    }

                    model.setEpisodeswatched(watchedepisodenum);
                    saveEpisodes(model.getId(), null, watchedepisodenum);
                    //DBHelper.getInstance(getContext()).updateWatchlistAnimeEps(model.getId(), null, watchedepisodenum);


                } catch (NumberFormatException ex) {
                    Log.e("WatchlistAdapter", "NumberFormatExcpetion trying to parse currentepisodes input to Integer");
                }


            }
        });



        final View finalConvertView1 = convertView;
        watchedepisodes.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    watchedepisodes.performClick();
                    //enter

                    linearLayoutsave.setVisibility(View.GONE);

                    try {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            //hide keyboar
                            imm.hideSoftInputFromWindow(finalConvertView1.getWindowToken(), 0);
                        }

                        String newcurrentepisodes = currentepisodes.getText().toString();

                        try {
                            int currentepisodesnum = Integer.valueOf(newcurrentepisodes);
                            if (currentepisodesnum < model.getEpisodeswatched()) {
                                Log.i("WatchlistAdapter", "Currentepisodes value lower than episodeswatched");
                                currentepisodes.setText(String.valueOf(model.getCurrentEpisode()));
                                return true;
                            }
                            if (currentepisodesnum > 999 || currentepisodesnum < 0) {
                                Log.i("WatchlistAdapter", "Currentepisode index out of bounds");
                                currentepisodes.setText(String.valueOf(model.getCurrentEpisode()));
                                return true;
                            }

                            model.setCurrentEpisode(currentepisodesnum);
                            saveEpisodes(model.getId(),currentepisodesnum,null);
                            //DBHelper.getInstance(getContext()).updateWatchlistAnimeEps(model.getId(), currentepisodesnum, null);

                            String newwatchedepisodevalue = watchedepisodes.getText().toString();

                            int watchedepisodenum = Integer.valueOf(newwatchedepisodevalue);
                            if (watchedepisodenum > model.getCurrentEpisode()) {
                                Log.i("WatchlistAdapter", "Episodeswatched value greater than currentepisodes");
                                watchedepisodes.setText(String.valueOf(model.getEpisodeswatched()));
                                return true;
                            }
                            if (watchedepisodenum > 999 || watchedepisodenum < 0) {
                                Log.i("WatchlistAdapter", "Episodeswatched index out of bounds");
                                watchedepisodes.setText(String.valueOf(model.getEpisodeswatched()));
                                return true;
                            }

                            model.setEpisodeswatched(watchedepisodenum);
                            saveEpisodes(model.getId(),null,watchedepisodenum);
                            //DBHelper.getInstance(getContext()).updateWatchlistAnimeEps(model.getId(), null, watchedepisodenum);


                        } catch (NumberFormatException ex) {
                            Log.e("WatchlistAdapter", "NumberFormatExcpetion trying to parse currentepisodes input to Integer");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;

                }
                return false;
            }
        });



        currentepisodes.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    currentepisodes.performClick();
                    //enter

                    linearLayoutsave.setVisibility(View.GONE);

                    try {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            //hide keyboar
                            imm.hideSoftInputFromWindow(finalConvertView1.getWindowToken(), 0);
                        }

                        String newcurrentepisodes = currentepisodes.getText().toString();

                        try {
                            int currentepisodesnum = Integer.valueOf(newcurrentepisodes);
                            if (currentepisodesnum < model.getEpisodeswatched()) {
                                Log.i("WatchlistAdapter", "Currentepisodes value lower than episodeswatched");
                                currentepisodes.setText(String.valueOf(model.getCurrentEpisode()));
                                return true;
                            }
                            if (currentepisodesnum > 999 || currentepisodesnum < 0) {
                                Log.i("WatchlistAdapter", "Currentepisode index out of bounds");
                                currentepisodes.setText(String.valueOf(model.getCurrentEpisode()));
                                return true;
                            }

                            model.setCurrentEpisode(currentepisodesnum);
                            saveEpisodes(model.getId(),currentepisodesnum,null);
                            //DBHelper.getInstance(getContext()).updateWatchlistAnimeEps(model.getId(), currentepisodesnum, null);

                            String newwatchedepisodevalue = watchedepisodes.getText().toString();

                            int watchedepisodenum = Integer.valueOf(newwatchedepisodevalue);
                            if (watchedepisodenum > model.getCurrentEpisode()) {
                                Log.i("WatchlistAdapter", "Episodeswatched value greater than currentepisodes");
                                watchedepisodes.setText(String.valueOf(model.getEpisodeswatched()));
                                return true;
                            }
                            if (watchedepisodenum > 999 || watchedepisodenum < 0) {
                                Log.i("WatchlistAdapter", "Episodeswatched index out of bounds");
                                watchedepisodes.setText(String.valueOf(model.getEpisodeswatched()));
                                return true;
                            }

                            model.setEpisodeswatched(watchedepisodenum);
                            saveEpisodes(model.getId(),null,watchedepisodenum);
                            //DBHelper.getInstance(getContext()).updateWatchlistAnimeEps(model.getId(), null, watchedepisodenum);


                        } catch (NumberFormatException ex) {
                            Log.e("WatchlistAdapter", "NumberFormatExcpetion trying to parse currentepisodes input to Integer");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });


        LinearLayout cancelLinear = (LinearLayout)convertView.findViewById(R.id.cancel);

        cancelLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayoutsave.setVisibility(View.GONE);
                watchedepisodes.setText(""+model.getEpisodeswatched());
                currentepisodes.setText(""+model.getCurrentEpisode());

                try {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        //hide keyboar
                        imm.hideSoftInputFromWindow(finalConvertView1.getWindowToken(), 0);
                    }


                }catch (Exception ex) {

                }

            }
        });

        return convertView;
    }


    private class RemoveOnClick implements View.OnClickListener{
        private WatchListModel model;

        public RemoveOnClick(WatchListModel model) {
            this.model = model;
        }

        @Override
        public void onClick(View v) {

            DBHelper.getInstance(getContext()).deleteWatchlistAnime(model.getId());
            remove(model);
            if(!DBHelper.getInstance(getContext()).checkIfExistsInWatchedList(model.getId()))
                DBHelper.getInstance(getContext()).insertIntoWatchedlist(model.getId());

        }
    }




    private void saveEpisodes(int id,Integer currentepisodesnum,Integer episodeswatched) {
        DBHelper.getInstance(getContext()).updateWatchlistAnimeEps(id, currentepisodesnum, episodeswatched);
    }





}
