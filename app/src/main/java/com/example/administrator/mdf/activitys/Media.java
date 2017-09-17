package com.example.administrator.mdf.activitys;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class Media{
    private MediaPlayer mediaPlayer;
    private String path;
    private Uri uri;
    private Context context;
    private boolean already=false;
    public void setUri(){
        uri = Uri.parse(path);
        already=false;
    }
    //MediaPlayer读取器
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    //url修改器
    public void setPath(String path) {
        this.path = path;
    }
    //构造方法
    public Media(MediaPlayer mediaPlayer,String path,Context context) {
        this.mediaPlayer = mediaPlayer;
        this.path = path;
        this.context = context;
    }
    //音乐开始播放
    public void musicstart() {
                try {
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.reset();
                    }
                    mediaPlayer.setDataSource(context,uri);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            already=true;

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
    //暂停播放
    public void musicpause() {
        mediaPlayer.pause();
    }
    //停止播放
    public void musicstop() {
        mediaPlayer.stop();
    }

}
