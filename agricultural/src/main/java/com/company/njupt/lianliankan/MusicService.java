package com.company.njupt.lianliankan;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import java.io.IOException;

public class MusicService extends Service {
    private MediaPlayer mpv = new MediaPlayer();
    private int state = 0;
    public MusicService() {
    }
class MyMusicBinder extends Binder{
        public MyMusicBinder(){

        }
    public void play() {
        playMusic();
    }
    public void pause() {
        if (mpv != null && mpv.isPlaying()) {
            mpv.pause();
            state=1;
        }
    }
    public void stop(){
        if (mpv != null && mpv.isPlaying()) {
            mpv.stop();
            state=0;
        }
    }
}
    @Override
    public IBinder onBind(Intent intent) {
        return new MyMusicBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playMusic();
        return super.onStartCommand(intent, flags, startId);
    }

    private void playMusic() {
        try {
            if(state==0) {
                if (mpv != null && mpv.isPlaying()) {
                    mpv.stop();
                }

                if (mpv != null) mpv.reset();
                mpv.setDataSource(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/cef" +
                        ".mp3");
                mpv.prepareAsync();
                mpv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            }
            else{
                mpv.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mpv != null && mpv.isPlaying()) {
            mpv.stop();
            if (mpv != null) mpv.release();
            mpv = null;
        }
    }
}