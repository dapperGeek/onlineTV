package com.thegeek0.ontv;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class TVPlayActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, Player.EventListener {

    private String url;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private DefaultBandwidthMeter defaultBandwidthMeter;
//    private DefaultTimeBar timeBar;
    private DefaultDataSourceFactory dataSourceFactory;
    private ProgressBar progressBar;
    private VideoView mVideoView;
    private ProgressBar load;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Vitamio.isInitialized(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_tv_play);
        url = getIntent().getStringExtra("videoUrl");

        playerView = findViewById(R.id.player_view);
//        timeBar = findViewById(R.id.exo_progress);
//        timeBar.setDuration(10000000L);
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Create a default bandwidth meter
        defaultBandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(defaultBandwidthMeter);

        // Create a default TrackSelector
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        playerView.setPlayer(player);

        // ExoPlayer 2.8.0
        DataSource.Factory dataSourceWithBandwidthMeter = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getResources().getString(R.string.app_name)), defaultBandwidthMeter);

        DataSource.Factory dataSourceWithoutBandwidthMeter = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getResources().getString(R.string.app_name)));
        HlsMediaSource mediaSource = new HlsMediaSource.Factory(new DefaultHlsDataSourceFactory(dataSourceWithBandwidthMeter)).createMediaSource(Uri.parse(url));

        // ExoPlayer 2.9.0 update
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getResources().getString(R.string.app_name)));
//
//        HlsMediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));

        player.prepare(mediaSource);
        player.setPlayWhenReady(true);

    }

    @Override
    protected void onStop(){
        super.onStop();

        playerView.setPlayer(null);
        player.release();
        player = null;
    }


    private void loadComplete(MediaPlayer arg0) {
        load.setVisibility(View.GONE);
        // vv.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        mVideoView.start();
        mVideoView.resume();
    }

    private void error(String msg) {
        load.setVisibility(View.GONE);
        mVideoView.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        if (msg != null)
            empty.setText(msg);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
        Log.d("ONLINE TV", "Prepared");
        loadComplete(mp);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        Log.d("ONLINE TV", "Error");
        error("Unable to play this channel.");
        Toast.makeText(this, getString(R.string.error_video), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        Log.d("ONLINE TV", "Complete");
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        AlertDialog.Builder adb = new AlertDialog.Builder(TVPlayActivity.this);
        adb.setTitle("Was not able to stream video");
        adb.setMessage("It seems that something is going wrong.\nPlease try again.");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish(); // take out user from this activity. you can skip this
            }
        });
        AlertDialog ad = adb.create();
        ad.show();
        Toast.makeText(this, getString(R.string.error_video), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
