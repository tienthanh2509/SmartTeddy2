package iot.tdmu.edu.vn.smartteddy.gson;

import java.io.Serializable;

/**
 * Created by nguye on 3/18/2017.
 * Song Album
 */

public class SongA implements Serializable {
    private int artist_id;
    private String artist_name;
    private int song_id;
    private String song_title;
    private String song_src;

    public SongA() {
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }

    public String getSong_src() {
        return song_src;
    }

    public void setSong_src(String song_src) {
        this.song_src = song_src;
    }

    @Override
    public String toString() {
        return this.song_title;
    }
}
