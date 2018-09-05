package com.android.superplayer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.superplayer.service.entity.MusicResult;

import java.io.IOException;

/**
 * anther: created by zuochunsheng on 2018/9/5 14 : 31
 * descript :
 */
public class MusicService extends Service {


    private static final String flag_service = "com.android.superplayer.Service";
    private MediaPlayer player = new MediaPlayer();
    private MusicResult music;

    private int state = 0x11 ; // 0x11 : 为第一次播放歌曲  ；  0x12 : 暂停 ； 0x13  继续播放
    @Override
    public void onCreate() {// 进行初始化操作
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(flag_service);
        registerReceiver(receiver,intentFilter);


        super.onCreate();


    }

    public class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int newmusic = intent.getIntExtra("newmusic", -1);// 1 新音乐


            if(newmusic != -1 ){
                music = (MusicResult) intent.getSerializableExtra("music");
                if(music != null){
                    playMusic(music);
                    state = 0x12;
                    // 将当前状态发送给 activity
                    sendBroadToActivity();
                }

            }

            int isPlay = intent.getIntExtra("isPlay", -1);//  三种状态
            if (isPlay != -1){
                //0x11 : 为第一次播放歌曲  ；  0x12 : 暂停 ； 0x13  继续播放
                switch (state){
                    case 0x11:
                        music = (MusicResult) intent.getSerializableExtra("music");
                        playMusic(music);
                        state = 0x12;
                        break;

                    case 0x12:
                        player.pause();
                        state = 0x13;

                        break;

                    case 0x13:

                        player.start();
                        state = 0x12;
                        break;

                }

                // 将当前状态发送给 activity
                sendBroadToActivity();

            }



        }
    }
    private void sendBroadToActivity(){

        Intent intent2 = new Intent("com.android.superplayer.Activity");
        intent2.putExtra("state",state);
        sendBroadcast(intent2);

    }



    /*
     * 播放歌曲
     */
    public void playMusic(MusicResult music){
        if(player != null ){
            // 停止播放
            player.stop();
            player.reset();

            try {
                // 获取播放歌曲路径
                player.setDataSource(music.getPath());
                //  准备
                player.prepare();
                // 播放
                player.start();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
