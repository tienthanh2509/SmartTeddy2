package iot.tdmu.edu.vn.smartteddy.gson;

import java.io.Serializable;

/**
 * Created by nguye on 3/17/2017.
 * ServerData
 */

public class ServerData implements Serializable{
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
