package com.example.administrator.mdf.responsebody;



public class MusicResponsebody extends Basecord {
    private String music_name;
    private String music_url;
    private String singer;

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }

    public String getMusic_name() {

        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }
}
