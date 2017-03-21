package iot.tdmu.edu.vn.smartteddy.gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nguye on 3/18/2017.
 *
 */

public class DataA implements Serializable {
    private int album_id;
    private String album_name;
    private ArrayList<SongA> song;

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public ArrayList<SongA> getSong() {
        return song;
    }

    public void setSong(ArrayList<SongA> song) {
        this.song = song;
    }

    public DataA() {
    }


}
