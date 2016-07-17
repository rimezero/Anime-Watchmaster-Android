package com.evolsoft.animewatchmaster.activities.share;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.evolsoft.animewatchmaster.R;
import com.facebook.FacebookSdk;

/**
 * Created by abraham on 6/7/2016.
 */
public class ShareActivity extends AppCompatActivity {

    public static final String APP_LINK = "https://fb.me/1145819815452899";
    public static final String APP_LOGO_LINK = "http://wall-paged.rhcloud.com/applogomed.png";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.layout_share);

        //ShareLinkContent content = new ShareLinkContent.Builder()
        //       .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.example.admin.animewatchmaster"))
        //        .build();


        //ShareButton sharebutton = (ShareButton)findViewById(R.id.sharebutton);
        //sharebutton.setShareContent(content);
    }


    /*
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
    */

    //set up facebook access and shit
    //otherwise it will crash
    public void inviteFriends(View v) {

        /*
        String link = APP_LINK;
        String logoUrl = APP_LOGO_LINK;

        if(AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(link)
                    .setPreviewImageUrl(logoUrl)
                    .build();
            AppInviteDialog.show(this,content);
        }
        */

    }

    public void rateapp(View v) {

        /*
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }

        */

    }


}