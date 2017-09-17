package com.example.administrator.mdf.activitys;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.administrator.mdf.MYapplication;
import com.example.administrator.mdf.R;
import com.example.administrator.mdf.adapter.GradeAdapter;
import com.example.administrator.mdf.adapter.ListviewAdapter;
import com.example.administrator.mdf.apis.APIsourse;
import com.example.administrator.mdf.apis.Music;
import com.example.administrator.mdf.responsebody.ErrorMessage;
import com.example.administrator.mdf.responsebody.MusicResponsebody;
import com.example.administrator.mdf.responsebody.Resource;
import com.example.administrator.mdf.service.Music_Service;
import com.example.administrator.mdf.view.MyListview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GridView gri;
    private MyListview listview;
    private List<Map<String, Object>> list;
    private List<Map<String, Object>> list_music;
    private ScrollView scrollingView;
    private static int i=1;
    private static int my_possion=200;
    private Music_Service my_service=null;
    public static ServiceConnection serviceConnection;
    private ImageButton img_previous;
    private ImageButton img_play;
    private ImageButton img_next;
    private ImageView img_change;
    /*
    * 向 MUSIC_SERVICE 发送广播,进行更改播放音乐状态
    * */
    private final String MUSIC_ACTIVITY_ACTION_PREVIOUS = "ACTIVITY.To.PREVIOUS";
    private final String MUSIC_ACTIVITY_ACTION_NEXT = "ACTIVITY.To.NEXT";
    private final String MUSIC_ACTIVITY_ACTION_PLAY = "ACTIVITY.To.PAUSE";
    private final String MUSIC_NOTIFICAION_INTENT_KEY = "type";
    private final int MUSIC_ACTIVITY_VALUE_PREVIOUS = 1001;
    private final int MUSIC_ACTIVITY_VALUE_NEXT = 1002;
    private final int MUSIC_ACTIVITY_VALUE_PLAY =1003;
    private Intent int_previous,int_play, int_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                my_service = ((Music_Service.LocalBinder) service).GetService();
                my_service.setList_music(list_music);
                my_service.setPossion(my_possion);
                my_service.start();
                Log.i("service","onServiceConnected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        scrollingView = (ScrollView) findViewById(R.id.scro);
        img_previous = (ImageButton) findViewById(R.id.img_previous);
        img_play = (ImageButton) findViewById(R.id.img_play);
        img_next = (ImageButton) findViewById(R.id.img_next);
        img_change = (ImageView) findViewById(R.id.imageView5);
        scrollingView.smoothScrollTo(0, 0);
        list = new ArrayList<>();
        list_music = new ArrayList<>();
        int picture[] = {R.mipmap.hauyu,R.mipmap.oumei,R.mipmap.yingwen,R.mipmap.qinggan,
        R.mipmap.liuxing,R.mipmap.shuqing,R.mipmap.minyao,R.mipmap.yaogun};
        String dif[]={"华语排行榜","欧美排行榜","英文","情感","流行","抒情","民谣","摇滚"};
        /*GradeView加载内容*/
        gri = (GridView) findViewById(R.id.grade_vi);
        for (int j = 0; j < 8; j++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", dif[j]);
            map.put("pic", picture[j]);
            list.add(map);
        }
        /*ListView加载内容*/
        listview = (MyListview) findViewById(R.id.list_view);
        final Music service = APIsourse.getInstance().getAPIObject(Music.class);
        Call<Resource<MusicResponsebody>> call = service.getall();
        call.enqueue(new Callback<Resource<MusicResponsebody>>() {
            @Override
            public void onResponse(Call<Resource<MusicResponsebody>> call, Response<Resource<MusicResponsebody>> response) {
                if (response.isSuccessful()) {
                    i=1;
                    List<MusicResponsebody> list = response.body().getResource();
                    for (MusicResponsebody musicresponsebody : list) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("num", "" + i);
                        map.put("name", musicresponsebody.getMusic_name());
                        map.put("singer", musicresponsebody.getSinger());
                        map.put("url", musicresponsebody.getMusic_url());
                        list_music.add(map);
                        ++i;
                    }
                } else {
                    Log.i("music", "请求错误");
                    ErrorMessage e = APIsourse.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<MusicResponsebody>> call, Throwable t) {
                System.out.println("error：" + t.toString());
                Toast.makeText(MainActivity.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        /*
        * GradeView适配器
        * */
        GradeAdapter adapter = new GradeAdapter(this, list);
        /*
        * ListView适配器
        * */
        ListviewAdapter adapter_music = new ListviewAdapter(this, list_music);

        gri.setAdapter(adapter);

        listview.setAdapter(adapter_music);

        /*
        * listview的监听器
        * */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(my_possion==position){
                    Toast.makeText(MainActivity.this,"音乐正在播放",Toast.LENGTH_LONG).show();
                }else{
                    my_possion=position;
                    if(my_service==null){
                        Intent intent = new Intent(getApplicationContext(), Music_Service.class);
                        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
                    }else{
                        my_service.setPossion(position);
                        my_service.music_reflash();
                    }
                }
            }
        });
        /*
        * Gradeview的监听器
        * */
        gri.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                }
            }
        });


        img_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(my_service.getMedia().getMediaPlayer().isPlaying()){
                    Intent intent=new Intent();
                    intent.setClass(getApplicationContext(), Play_Activity.class);
                    intent.putExtra("act",my_service.getMedia().getMediaPlayer().getDuration());
                    intent.putExtra("acc",my_service.getMedia().getMediaPlayer().getCurrentPosition());
                    startActivity(intent);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_logout:
                if(my_service!=null){
                    unbindService(serviceConnection);
                    my_service=null;
                    my_possion=-1;
                    MYapplication.setMy_service(my_service);
                }
                break;
            case R.id.nav_person:
                break;
            case R.id.nav_repassword:
                break;
            case R.id.nav_banben:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    public void Img_Onclic(View view){
        int id = view.getId();
        switch (id) {
            case R.id.img_previous:
                int_previous = new Intent();
                int_previous.setAction(MUSIC_ACTIVITY_ACTION_PREVIOUS);
                int_previous.putExtra(MUSIC_NOTIFICAION_INTENT_KEY, MUSIC_ACTIVITY_VALUE_PREVIOUS);
                sendBroadcast(int_previous);
                break;
            case R.id.img_play:
                int_play = new Intent();
                int_play.setAction(MUSIC_ACTIVITY_ACTION_PLAY);
                int_play.putExtra(MUSIC_NOTIFICAION_INTENT_KEY, MUSIC_ACTIVITY_VALUE_PLAY);
                sendBroadcast(int_play);
                break;
            case R.id.img_next:
                int_next = new Intent();
                int_next.setAction(MUSIC_ACTIVITY_ACTION_NEXT);
                int_next.putExtra(MUSIC_NOTIFICAION_INTENT_KEY, MUSIC_ACTIVITY_VALUE_NEXT);
                sendBroadcast(int_next);
                break;
        }
    }
}
