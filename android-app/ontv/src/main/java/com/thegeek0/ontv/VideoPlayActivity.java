package com.thegeek0.ontv;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VideoPlayActivity extends AppCompatActivity {

    private BetterVideoPlayer player;
    String url;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);
        Intent intent = getIntent();
        url = intent.getStringExtra("videoUrl");
        player = findViewById(R.id.player);
        player.setSource(Uri.parse(url));
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }
}
