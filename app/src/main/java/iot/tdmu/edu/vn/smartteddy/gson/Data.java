package iot.tdmu.edu.vn.smartteddy.gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nguye on 3/17/2017.
 *
 */

public class Data implements Serializable {
    private String title;
    private ArrayList<Song> song;

    public Data() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Song> getSongs() {
        return song;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.song = songs;
    }
}
