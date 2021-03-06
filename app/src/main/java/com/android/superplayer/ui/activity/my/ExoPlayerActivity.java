package com.android.superplayer.ui.activity.my;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.superplayer.R;
import com.android.superplayer.base.BaseActivity;
import com.android.superplayer.config.LogUtil;
import com.android.superplayer.ui.activity.MainActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Formatter;
import java.util.Locale;

/**
 * 视频播放
 *
 * @author zuochunsheng
 * @time 2018/9/14 16:44
 */
public class ExoPlayerActivity extends BaseActivity {


    private SimpleExoPlayer mSimpleExoPlayer;

    private SimpleExoPlayerView mExoPlayerView;
    private ProgressBar mProgressBar;
    private Context context = this;

    //net  show uc
   public static String origin = "https://mparticle.uc.cn/video.html?uc_param_str=frdnsnpfvecpntnwprdssskt#!wm_aid=2ef327d6a964424396ee6460e73ec563!!wm_id=41ad6b83918746c2ad1a4afbab13e1cd";
    //net
    public static String url = "https://iflow.uc.cn/ucnews/video?app=ucnewsvideo-iflow&aid=6826425571346778896&cid=10016&zzd_from=ucnewsvideo-iflow&uc_param_str=dndsfrvesvntnwpfgibi&recoid=8956383277257216137&rd_type=reco&original_url=http%3A%2F%2Fv.ums.uc.cn%2Fvideo%2Fv_c6a5448cb6e34b64.html&uc_biz_str=S%3Acustom%7CC%3Aiflow_video_hide&ums_id=c6a5448cb6e34b64&activity=1&activity2=1";
    //public static String html = "http://v.ums.uc.cn/video/v_2fd843c9b099f628.html";
    public static String zzd_url = "https://iflow.uc.cn/ucnews/video?app=ucnewsvideo-iflow&aid=13346669931047678326&cid=10016&zzd_from=ucnewsvideo-iflow&uc_param_str=dndsfrvesvntnwpfgibi&recoid=1932455237637573924&rd_type=reco&original_url=http%3A%2F%2Fv.ums.uc.cn%2Fvideo%2Fv_2fd843c9b099f628.html&uc_biz_str=S%3Acustom%7CC%3Aiflow_video_hide&ums_id=2fd843c9b099f628&activity=1&activity2=1";

    // play ok
    public static String mp4 = "http://smarticle.video.ums.uc.cn/video/wemedia/7fcb8203c5e041c3bf5c92d546534205/d0746842218427fcb9beecf1760c3c50-3529323208-2-0-2.mp4?auth_key=1595471059-41147fa876ab4c8eb62b37250a92f4f8-0-1a235426320c43d65f8ebc36a346da03";
    // play ok, huawei ok
    public static String mp4_url = "https://vd3.bdstatic.com/mda-kh5eix59aidwk52n/mda-kh5eix59aidwk52n.mp4";
    // 视频网络地址
    //Uri playerUri = Uri.parse(mp4_url);
    Uri playerUri = Uri.parse(mp4_url);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exo_player;
    }

    @Override
    protected void initViewAndData() {

        initPlayer();

        playVideo();
    }


    /**
     * 初始化player
     */
    private void initPlayer() {
        mExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        //1. 创建一个默认的 TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

        //2.创建ExoPlayer
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        //3.创建SimpleExoPlayerView

        //4.为SimpleExoPlayer设置播放器
        mExoPlayerView.setPlayer(mSimpleExoPlayer);

    }

    //开始播放:
    private void playVideo() {
        //测量播放过程中的带宽。 如果不需要,可以为null。
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // 生成加载媒体数据的DataSource实例。
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(ExoPlayerActivity.this,
                Util.getUserAgent(ExoPlayerActivity.this, "useExoplayer"), bandwidthMeter);
        // 生成用于解析媒体数据的Extractor实例。
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // MediaSource代表要播放的媒体。 可多个视频连续
        MediaSource videoSource = new ExtractorMediaSource(playerUri, dataSourceFactory, extractorsFactory,
                null, null);

        //要无限循环，通常最好使用ExoPlayer.setRepeatMode而不是LoopingMediaSource。
        //LoopingMediaSource 无缝循环固定次数
        // Plays the video twice.
        //LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource, 2);

        // 剪辑视频
        ClippingMediaSource clippingSource =
                new ClippingMediaSource(
                        videoSource,
                        /* startPositionUs= */ 5_000_000,
                        /* endPositionUs= */ 10_000_000);

        //侧加载字幕文件






        //Prepare the player with the source.
        mSimpleExoPlayer.prepare(videoSource);
        //添加监听的listener
        // mSimpleExoPlayer.setVideoListener(mVideoListener);
        mSimpleExoPlayer.addListener(eventListener);
        // mSimpleExoPlayer.setTextOutput(mOutput);

        // 播放
        mSimpleExoPlayer.setPlayWhenReady(true);


    }


    //监听播放器的状态:
    private ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            LogUtil.e("onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            LogUtil.e("onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            LogUtil.e("onLoadingChanged isLoading = " + isLoading);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            LogUtil.e("onPlayerStateChanged: playWhenReady = " + playWhenReady + " playbackState = " + playbackState);

            switch (playbackState) {
                case ExoPlayer.STATE_ENDED:
                    LogUtil.e("Playback ended!");
                    //Stop playback and return to start position
                    setPlayPause(false);
                    mSimpleExoPlayer.seekTo(0);
                    break;
                case ExoPlayer.STATE_READY:
                    mProgressBar.setVisibility(View.GONE);
                    LogUtil.e("ExoPlayer ready! pos: " + mSimpleExoPlayer.getCurrentPosition()
                            + " max: " + stringForTime((int) mSimpleExoPlayer.getDuration()));
                    setProgress(0);
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    LogUtil.e("Playback buffering!");
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case ExoPlayer.STATE_IDLE:
                    LogUtil.e("ExoPlayer idle!");
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            LogUtil.e("onRepeatModeChanged repeatMode =" + repeatMode);
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            LogUtil.e("onShuffleModeEnabledChanged shuffleModeEnabled = " + shuffleModeEnabled);
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            LogUtil.e("onPlaybackError: " + error.getMessage());
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            LogUtil.e("onPositionDiscontinuity : reason = " + reason);
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            LogUtil.e("onPlaybackParametersChanged  playbackParameters = " + playbackParameters.toString());
        }

        @Override
        public void onSeekProcessed() {
            LogUtil.e("onSeekProcessed");

        }
    };


//    setPlayWhenReady开始和暂停播放，各种seekTo方法在媒体内寻找，
//    setRepeatMode 控制媒体是否以及如何循环，
//    setShuffleModeEnabled 控制播放列表改组，
//    以及 setPlaybackParameters 调整播放速度和音高。

    private void setPlayPause(boolean play) {
        mSimpleExoPlayer.setPlayWhenReady(play);
    }

    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    @Override
    protected void onPause() {
        LogUtil.e("MainActivity.onPause.");
        super.onPause();
        mSimpleExoPlayer.stop();
    }

    @Override
    protected void onStop() {
        LogUtil.e("MainActivity.onStop.");
        super.onStop();
        mSimpleExoPlayer.release();
    }


}
