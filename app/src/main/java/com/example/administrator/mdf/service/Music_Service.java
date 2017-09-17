package com.example.administrator.mdf.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.administrator.mdf.R;
import com.example.administrator.mdf.activitys.Media;
import com.example.administrator.mdf.activitys.Play_Activity;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Music_Service extends Service{


    private List<Map<String, Object>> list_music;
    private MediaPlayer mediaPlayer;
    private Media media;
    private int possion;
    private NotificationManager manager;
    private NotificationCompat.Builder builder=null;
    private boolean show=false;
    private int Notification_ID=1;
    private Context context;
    private static int max_list=14;
    private Timer timer;
    private TimerTask timerTask;
    private boolean musicchangeed=false;

    /*
    * 向service发送广播
    * */
    private MusicBroadCast musicBroadCast;
    private final String MUSIC_NOTIFICATION_ACTION_PLAY = "musicnotificaion.To.PLAY";
    private final String MUSIC_NOTIFICATION_ACTION_NEXT = "musicnotificaion.To.NEXT";
    private final String MUSIC_NOTIFICATION_ACTION_CLOSE = "musicnotificaion.To.CLOSE";
    private final String MUSIC_NOTIFICAION_INTENT_KEY = "type";
    private final int MUSIC_NOTIFICATION_VALUE_PLAY = 0001;
    private final int MUSIC_NOTIFICATION_VALUE_NEXT = 0002;
    private final int MUSIC_NOTIFICATION_VALUE_CLOSE =0003;
    private Intent play=null,next=null,close = null;

    /*
    * Play_Activity发送过来的广播
    * */
    private final String MUSIC_ACTIVITY_ACTION_PREVIOUS = "ACTIVITY.To.PREVIOUS";
    private final String MUSIC_ACTIVITY_ACTION_NEXT = "ACTIVITY.To.NEXT";
    private final String MUSIC_ACTIVITY_ACTION_PLAY = "ACTIVITY.To.PAUSE";
    private final int MUSIC_ACTIVITY_VALUE_PREVIOUS = 1001;
    private final int MUSIC_ACTIVITY_VALUE_NEXT = 1002;
    private final int MUSIC_ACTIVITY_VALUE_PLAY =1003;


    /*
    * 更新进度条的广播
    * */
    private final String MUSIC_ACTIVITY_ACTION_CHANGESEEKBAR = "ACTIVITY.To.CHANGESEEKBAR";
    private final int MUSIC_ACTIVITY_VALUE_CHANGESEEKBAR = 3101;
    private final String MUSIC_ACTIVITY_ACTION_CHANGEMUSIC = "ACTIVITY.To.CHANGEMUSIC";
    private final int MUSIC_ACTIVITY_VALUE_CHANGEMUSIC = 3102;
    private final String MUSIC_ACTIVITY_ACTION_REFLASHMUSIC = "ACTIVITY.To.REFLASHMUSIC";
    private final int MUSIC_ACTIVITY_VALUE_REFLASHMUSIC = 3103;


    public void setContext(Context context) {
        this.context = context;
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public NotificationManager getManager() {
        return manager;
    }

    public int getPossion() {
        return possion;
    }

    public void setPossion(int possion) {
        this.possion = possion;
    }

    public Media getMedia() {
        return media;
    }

    public void setList_music(List<Map<String, Object>> list_music) {
        this.list_music = list_music;
    }

    public class LocalBinder extends Binder {
        public Music_Service GetService(){
            return Music_Service.this;
        }
    }

    private LocalBinder localBinder=new LocalBinder();
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("aervice","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        /*
        * 回收所有资源
        * */
        timer=null;
        timerTask=null;
        list_music=null;
        media.musicstop();
        mediaPlayer.release();
        mediaPlayer=null;
        media=null;
        manager.cancel(Notification_ID);
        manager=null;
        builder=null;
        possion=-1;
        Log.i("service","onDestroy");
        unregisterReceiver(musicBroadCast);
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        /*
        * 音乐播放完之后做的操作
        * */
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ++possion;
                if(possion>14){
                    possion=0;
                }
                media.setPath(list_music.get(possion).get("url").toString());
                media.setUri();
                media.getMediaPlayer().reset();
                media.musicstart();
                if(show){
                    Notification_change();
                }
            }
        });
        musicBroadCast = new MusicBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MUSIC_NOTIFICATION_ACTION_PLAY);
        filter.addAction(MUSIC_NOTIFICATION_ACTION_NEXT);
        filter.addAction(MUSIC_NOTIFICATION_ACTION_CLOSE);
        filter.addAction(MUSIC_ACTIVITY_ACTION_PLAY);
        filter.addAction(MUSIC_ACTIVITY_ACTION_NEXT);
        filter.addAction(MUSIC_ACTIVITY_ACTION_PREVIOUS);
        filter.addAction(MUSIC_ACTIVITY_ACTION_REFLASHMUSIC);
        registerReceiver(musicBroadCast, filter);
        super.onCreate();
        Log.i("service","onCreate");
    }


    /*
    * 音乐播放的操作
    * */
    public void start(){
        media=new Media(mediaPlayer,list_music.get(possion).get("url").toString(),getApplicationContext());
        play_music();
        Notification_show();
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                if(!musicchangeed){
                    Intent intent=new Intent(MUSIC_ACTIVITY_ACTION_CHANGESEEKBAR);
                    intent.putExtra(MUSIC_NOTIFICAION_INTENT_KEY,MUSIC_ACTIVITY_VALUE_CHANGESEEKBAR);
                    intent.putExtra("nows",mediaPlayer.getCurrentPosition());
                    sendBroadcast(intent);
                }else{
                    return;
                }

            }
        };
        timer.schedule(timerTask,100,100);
    }

    public void play_music() {
        musicchangeed=true;
        Intent intent=new Intent(MUSIC_ACTIVITY_ACTION_CHANGEMUSIC);
        intent.putExtra(MUSIC_NOTIFICAION_INTENT_KEY,MUSIC_ACTIVITY_VALUE_CHANGEMUSIC);
        intent.putExtra("length",mediaPlayer.getDuration());
        sendBroadcast(intent);
        if (mediaPlayer.isPlaying()) {
            media.setUri();
            media.getMediaPlayer().reset();
            media.musicstart();
        } else {
            media.setUri();
            media.musicstart();
        }
        musicchangeed=false;
    }

    public void music_reflash(){
        media.setPath(list_music.get(possion).get("url").toString());
        play_music();
        Notification_change();
    }


    /*
    * 通知栏操作
    * */
    public void Notification_show(){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), Play_Activity.class);
        PendingIntent pendingIntent_main = PendingIntent.getActivity(getApplication(), MUSIC_NOTIFICATION_VALUE_PLAY, intent, PendingIntent.FLAG_NO_CREATE);

        play = new Intent();
        play.putExtra(MUSIC_NOTIFICAION_INTENT_KEY, MUSIC_NOTIFICATION_VALUE_PLAY);
        play.setAction(MUSIC_NOTIFICATION_ACTION_PLAY);

        next = new Intent();
        next.putExtra(MUSIC_NOTIFICAION_INTENT_KEY, MUSIC_NOTIFICATION_VALUE_NEXT);
        next.setAction(MUSIC_NOTIFICATION_ACTION_NEXT);

        close = new Intent();
        close.putExtra(MUSIC_NOTIFICAION_INTENT_KEY, MUSIC_NOTIFICATION_VALUE_CLOSE);
        close.setAction(MUSIC_NOTIFICATION_ACTION_CLOSE);
        PendingIntent pendingIntent_play = PendingIntent.getBroadcast(this, MUSIC_NOTIFICATION_VALUE_PLAY, play, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent_next = PendingIntent.getBroadcast(this, MUSIC_NOTIFICATION_VALUE_NEXT, next, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent_close = PendingIntent.getBroadcast(this, MUSIC_NOTIFICATION_VALUE_CLOSE, close, PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(getApplicationContext());
        builder .setLargeIcon(BitmapFactory.decodeResource(
                 getResources(), R.mipmap.bg))
                .setSmallIcon(R.drawable.iot)
                .setOngoing(true)
                .addAction(R.drawable.notification_next,null,pendingIntent_play)
                .addAction(R.drawable.notification_start,null,pendingIntent_next)
                .addAction(R.drawable.notification_x,null,pendingIntent_close)
                .setStyle(new NotificationCompat.MediaStyle())
                .setContentText(list_music.get(possion).get("singer").toString())
                .setContentTitle(list_music.get(possion).get("name").toString());
        manager.notify(Notification_ID,builder.build());
        show=true;
    }
    /*
    * 更改通知栏
    * */
    public void Notification_change(){
        builder.setContentTitle(list_music.get(possion).get("name").toString())
                .setContentText(list_music.get(possion).get("singer").toString());
        manager.notify(Notification_ID,builder.build());
    }

    public class  MusicBroadCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra(MUSIC_NOTIFICAION_INTENT_KEY,-1);
            switch (action){
                case MUSIC_NOTIFICATION_VALUE_PLAY:
                    ++possion;
                    if(possion>max_list){
                        possion=0;
                    }
                    music_reflash();
                    break;
                case MUSIC_NOTIFICATION_VALUE_NEXT:
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }else{
                        mediaPlayer.start();
                    }
                    break;
                case MUSIC_NOTIFICATION_VALUE_CLOSE:
                    media.musicstop();
                    manager.cancel(Notification_ID);
                    break;
                case MUSIC_ACTIVITY_VALUE_NEXT:
                    ++possion;
                    if(possion>max_list){
                        possion=0;
                    }
                    music_reflash();
                    break;
                case MUSIC_ACTIVITY_VALUE_PREVIOUS:
                    --possion;
                    if(possion<0){
                        possion = max_list;
                    }
                    music_reflash();
                    break;
                case MUSIC_ACTIVITY_VALUE_PLAY:
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }else{
                        mediaPlayer.start();
                    }
                    break;
                case MUSIC_ACTIVITY_VALUE_REFLASHMUSIC:
                    int towhere=intent.getIntExtra("towhere",-1);
                    mediaPlayer.seekTo(towhere);
                    break;
            }
        }
    }

}