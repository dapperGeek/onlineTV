//package com.app.greenberrytv;
//
//import android.content.DialogInterface;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ProgressBar;
//
//import com.google.android.exoplayer2.DefaultLoadControl;
//import com.google.android.exoplayer2.ExoPlaybackException;
//import com.google.android.exoplayer2.ExoPlayer;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.LoadControl;
//import com.google.android.exoplayer2.PlaybackParameters;
//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.Timeline;
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
//import com.google.android.exoplayer2.extractor.ExtractorsFactory;
//import com.google.android.exoplayer2.source.TrackGroupArray;
//import com.google.android.exoplayer2.source.hls.HlsMediaSource;
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.trackselection.TrackSelection;
//import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
//import com.google.android.exoplayer2.trackselection.TrackSelector;
//import com.google.android.exoplayer2.ui.PlayerView;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.util.Util;
//
//public class VideoPlayerActivity extends AppCompatActivity implements ExoPlayer.EventListener {
//
//    private PlayerView playerView;
//    private String url = "http://playertest.longtailvideo.com/adaptive/bbbfull/bbbfull.m3u8";
//    private SimpleExoPlayer player;
//    private ProgressBar progressBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tv_play);
//
//        // keep screen on
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        // 1. Create a default TrackSelector
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        TrackSelector trackSelector = new DefaultTrackSelector();
//
//        // 2. Create a default LoadControl
//        LoadControl loadControl = new DefaultLoadControl();
//
//
//        // 3. Create the player
//        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
//
//        playerView = (PlayerView) findViewById(R.id.player_view);
//        playerView.setPlayer(player);
//
//        // Measures bandwidth during playback. Can be null if not required.
//        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
//        // Produces DataSource instances through which media data is loaded.
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
//                Util.getUserAgent(this, "Exo2"), bandwidthMeter);
//        // Produces Extractor instances for parsing the media data.
//        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//        // This is the MediaSource representing the media to be played.
//        HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));
//
//        player.addListener(this);
//        player.seekTo(0, 20000);
//        player.prepare(hlsMediaSource);
//        playerView.requestFocus();
//        player.setPlayWhenReady(true);
//
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//    }
//
//    @Override
//    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//
//    }
//
//    @Override
//    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//    }
//
//    @Override
//    public void onLoadingChanged(boolean isLoading) {
//
//    }
//
//    @Override
//    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//
//        switch (playbackState) {
//            case Player.STATE_BUFFERING:
//                //You can use progress dialog to show user that video is preparing or buffering so please wait
//                progressBar.setVisibility(View.VISIBLE);
//                break;
//            case Player.STATE_IDLE:
//                //idle state
//                break;
//            case Player.STATE_READY:
//                // dismiss your dialog here because our video is ready to play now
//                progressBar.setVisibility(View.GONE);
//                break;
//            case Player.STATE_ENDED:
//                // do your processing after ending of video
//                break;
//        }
//    }
//
//    @Override
//    public void onRepeatModeChanged(int repeatMode) {
//
//    }
//
//    @Override
//    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//    }
//
//    @Override
//    public void onPlayerError(ExoPlaybackException error) {
//
//        AlertDialog.Builder adb = new AlertDialog.Builder(VideoPlayerActivity.this);
//        adb.setTitle("Was not able to stream video");
//        adb.setMessage("It seems that something is going wrong.\nPlease try again.");
//        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish(); // take out user from this activity. you can skip this
//            }
//        });
//        AlertDialog ad = adb.create();
//        ad.show();
//    }
//
//    @Override
//    public void onPositionDiscontinuity(int reason) {
//
//    }
//
//    @Override
//    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//    }
//
//    @Override
//    public void onSeekProcessed() {
//
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (player != null) {
//            player.setPlayWhenReady(false); //to pause a video because now our video player is not in focus
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        player.release();
//    }
//}
