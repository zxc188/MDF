package com.example.administrator.mdf.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.administrator.mdf.R;

import java.util.Timer;
import java.util.TimerTask;

public class Play_Activity extends AppCompatActivity {

    private ImageButton btprevious;
    private ImageButton btpause;
    private ImageButton btnext;
    private SeekBar seekBar;
    private Timer timer;
    private boolean isfirst=true;
    private TimerTask timerTask;
    boolean ischange=false;
    private SeekBarchanged seekBarchanged;
    private final String MUSIC_ACTIVITY_ACTION_PREVIOUS = "ACTIVITY.To.PREVIOUS";
    private final String MUSIC_ACTIVITY_ACTION_NEXT = "ACTIVITY.To.NEXT";
    private final String MUSIC_ACTIVITY_ACTION_PLAY = "ACTIVITY.To.PAUSE";
    private final String MUSIC_NOTIFICAION_INTENT_KEY = "type";
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(seekBarchanged);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_main);
        btprevious = (ImageButton) findViewById(R.id.shang);
        btpause = (ImageButton) findViewById(R.id.pau);
        btnext = (ImageButton) findViewById(R.id.next);
        seekBar = (SeekBar) findViewById(R.id.seekBar12);
        //seekBar.setMax(1000000000);
        Intent intent2=getIntent();
        seekBar.setMax(intent2.getIntExtra("act",-1));
        isfirst=false;
        //seekBar.setProgress(intent2.getIntExtra("acc",-1));
        seekBarchanged = new SeekBarchanged();
        IntentFilter filter = new IntentFilter();
        IntentFilter filter1 = new IntentFilter();
        filter.addAction(MUSIC_ACTIVITY_ACTION_CHANGESEEKBAR);
        filter1.addAction(MUSIC_ACTIVITY_ACTION_CHANGEMUSIC);
        registerReceiver(seekBarchanged, filter);
        registerReceiver(seekBarchanged, filter1);


        /*
        * 上一首歌的监听器
        * */
       btprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MUSIC_ACTIVITY_ACTION_PREVIOUS);
                intent.putExtra(MUSIC_NOTIFICAION_INTENT_KEY,MUSIC_ACTIVITY_VALUE_PREVIOUS);
                sendBroadcast(intent);
            }
        });
        /*
        *下一首歌的监听器
        * */
        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MUSIC_ACTIVITY_ACTION_NEXT);
                intent.putExtra(MUSIC_NOTIFICAION_INTENT_KEY,MUSIC_ACTIVITY_VALUE_NEXT);
                sendBroadcast(intent);
            }
        });
        /*
        * 播放音乐的监听器
        * */
        btpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MUSIC_ACTIVITY_ACTION_PLAY);
                intent.putExtra(MUSIC_NOTIFICAION_INTENT_KEY,MUSIC_ACTIVITY_VALUE_PLAY);
                sendBroadcast(intent);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ischange=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent intent=new Intent(MUSIC_ACTIVITY_ACTION_REFLASHMUSIC);
                intent.putExtra(MUSIC_NOTIFICAION_INTENT_KEY,MUSIC_ACTIVITY_VALUE_REFLASHMUSIC);
                intent.putExtra("towhere",seekBar.getProgress());
                sendBroadcast(intent);
                ischange=false;
            }
        });

    }
    public class SeekBarchanged extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int val=intent.getIntExtra(MUSIC_NOTIFICAION_INTENT_KEY,-1);
            switch (val){
                case MUSIC_ACTIVITY_VALUE_CHANGESEEKBAR:
                    if(!ischange){
                        seekBar.setProgress(intent.getIntExtra("nows",-1));
                    }
                    break;
                case MUSIC_ACTIVITY_VALUE_CHANGEMUSIC:
                    if(isfirst){
                        isfirst=false;
                        Toast.makeText(Play_Activity.this,"1111111111111111",Toast.LENGTH_LONG).show();
                        System.out.println("11111111111111111");
                    }else{
                        Toast.makeText(Play_Activity.this,"22222222222222222222",Toast.LENGTH_LONG).show();
                        seekBar.setProgress(0);
                        seekBar.setMax(intent.getIntExtra("length",-1));
                    }
                    break;
            }
        }
        public  void changelength(int length){
            seekBar.setMax(length);
        }
    }
}
