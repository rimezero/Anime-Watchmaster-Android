package com.cspeitch.animewatchmaster.activities.share;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cspeitch.animewatchmaster.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareButton;

/**
 * Created by abraham on 6/7/2016.
 */
public class ShareActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.layout_share);

        ShareLinkContent content = new ShareLinkContent.Builder()
               .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName()))
               .build();


        ShareButton sharebutton = (ShareButton)findViewById(R.id.sharebutton);
        sharebutton.setShareContent(content);
    }



    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }


    //set up facebook access and shit
    //otherwise it will crash
    public void inviteFriends(View v) {


        String link = getResources().getString(R.string.app_link);
        String logoUrl = getResources().getString(R.string.app_logo_url);

        if(AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(link)
                    .setPreviewImageUrl(logoUrl)
                    .build();
            AppInviteDialog.show(this,content);
        }


    }



    public void rateapp(View v) {


        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }



    }


}