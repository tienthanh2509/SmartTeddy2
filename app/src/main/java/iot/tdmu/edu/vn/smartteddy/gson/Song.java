package iot.tdmu.edu.vn.smartteddy.gson;

import java.io.Serializable;

/**
 * Created by nguye on 3/17/2017.
 *
 */

public class Song implements Serializable {
    private int id;
    private String title;
    private String art;
    private String src;

    public Song() {
    }

    public Song(int id, String title, String art, String src) {
        this.id = id;
        this.title = title;
        this.art = art;
        this.src = src;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return this.id + "\n" + this.title + "\n" + this.art + "\n" + this.src;
    }
}
