package iot.tdmu.edu.vn.smartteddy.ui;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import iot.tdmu.edu.vn.smartteddy.contanst.Constants;
import iot.tdmu.edu.vn.smartteddy.gson.DataA;
import iot.tdmu.edu.vn.smartteddy.gson.SongA;
import iot.tdmu.edu.vn.smartteddy.utils.FileUtil;

public class MusicActivity extends AppCompatActivity {

    ImageButton playButton, btnstop, loopButton, previousButton, nextButton, shuffleButton;
    SeekBar seekBar;
    TextView txtsongAlbum, txtCount;
    ListView lvPlaylist_ThieuNhi, lvPlayList_NgoaiQuoc, lvPlayList_Truyen;
    ArrayList<SongA> dsKQ;
    ArrayAdapter<SongA> songArrayAdapter;
    int ma, machuyendoi, tam;

    Intent intent;
    MediaPlayer player;
    Handler seekHandler = new Handler();
    private io.socket.client.Socket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        playButton = (ImageButton) findViewById(R.id.playButton);
        lvPlaylist_ThieuNhi = (ListView) findViewById(R.id.lvPlayList_ThieuNhi);
        lvPlayList_NgoaiQuoc = (ListView) findViewById(R.id.lvPlayList_NgoaiQuoc);
        lvPlayList_Truyen = (ListView) findViewById(R.id.lvPlayList_Truyen);
        txtsongAlbum = (TextView) findViewById(R.id.txtsongAlbum);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        txtCount = (TextView) findViewById(R.id.timeCount);
        btnstop = (ImageButton) findViewById(R.id.stopButton);
        loopButton = (ImageButton) findViewById(R.id.loopButton);
        previousButton = (ImageButton) findViewById(R.id.previousButton);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        shuffleButton = (ImageButton) findViewById(R.id.shuffleButton);
        dsKQ = new ArrayList<>();

        songArrayAdapter = new ArrayAdapter<>(MusicActivity.this, android.R.layout.simple_list_item_1, dsKQ);




        intent = getIntent();
        ma = intent.getIntExtra("MA", 0);
        machuyendoi = getSharedPreferences("machuyendoi",MODE_PRIVATE).getInt("trangthai",0);
        Log.e("MAB",machuyendoi + "");
        switch (ma) {
            case 1:
                lvPlaylist_ThieuNhi.setVisibility(View.VISIBLE);
                lvPlayList_NgoaiQuoc.setVisibility(View.GONE);
                lvPlayList_Truyen.setVisibility(View.GONE);
                URL url = null;
                try {
                    url = new URL("http://103.27.236.133:3000/api/v1/album/1");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                DataTask task = new DataTask();
                task.execute(url);
                lvPlaylist_ThieuNhi.setAdapter(songArrayAdapter);
                break;
            case 2:
                lvPlaylist_ThieuNhi.setVisibility(View.GONE);
                lvPlayList_NgoaiQuoc.setVisibility(View.VISIBLE);
                lvPlayList_Truyen.setVisibility(View.GONE);
                URL url1 = null;
                try {
                    url1 = new URL("http://103.27.236.133:3000/api/v1/album/3");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                DataTask task1 = new DataTask();
                task1.execute(url1);
                lvPlayList_NgoaiQuoc.setAdapter(songArrayAdapter);
                break;
            case 3:
                lvPlaylist_ThieuNhi.setVisibility(View.GONE);
                lvPlayList_NgoaiQuoc.setVisibility(View.GONE);
                lvPlayList_Truyen.setVisibility(View.VISIBLE);
                URL url2 = null;
                try {
                    url2 = new URL("http://103.27.236.133:3000/api/v1/album/2");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                DataTask task2 = new DataTask();
                task2.execute(url2);
                lvPlayList_Truyen.setAdapter(songArrayAdapter);
                break;
        }

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.setType("*/*");
                if (Build.VERSION.SDK_INT < 19) {
                    intent2.setAction(Intent.ACTION_GET_CONTENT);
                    intent2 = Intent.createChooser(intent2, "Select file");
                } else {
                    intent2.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent2.addCategory(Intent.CATEGORY_OPENABLE);
                }
                startActivityForResult(intent2, Constants.REQUEST_BROWSE);
            }
        });
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index =  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_BROWSE
                && resultCode == Activity.RESULT_OK && data != null) {
            final Uri uri = data.getData();
            final String result = "/storage/emulated/0/Music/" + getFileName(uri);
            //final String result = getRealPathFromURI(MusicActivity.this,uri);
            //Log.e("FILE",result);
            if (uri != null) {
                final byte[] as = FileUtil.getByteArrayFromLocalFile(result);
                Log.e("FILE",as.length +"");
                Log.e("FILE",uri.toString());
                try {
                    socket = IO.socket("http://103.27.236.133:3000/");
                    socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("data_music",as);

                                socket.emit("playBuffer",obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    socket.connect();


                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ServerDataTask extends AsyncTask<String, Void, ArrayList<SongA>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            songArrayAdapter.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<SongA> songs) {
            super.onPostExecute(songs);
            songArrayAdapter.clear();
            songArrayAdapter.addAll(songs);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<SongA> doInBackground(String... params) {
            ArrayList<SongA> ds = new ArrayList<>();

            try {
                //String keyword = params[0];
                String link = "http://103.27.236.133:3000/api/v1/album/1";
                //"http://103.27.236.133:3000/media/nhac-thieu-nhi.json";

                URL url = new URL(link);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openStream(), "UTF-8");
                StringWriter writer = new StringWriter();
                IOUtils.copy(inputStreamReader, writer);
                String theString = writer.toString();
                Log.e("TAG", theString);
                //ServerData serverData = new Gson().fromJson(theString, ServerData.class);

                //ds = serverData.getData().getSongs();
                DataA dataA = new Gson().fromJson(theString, DataA.class);
                ds = dataA.getSong();

            } catch (Exception ex) {
                Log.e("LOI", ex.toString());
            }
            return ds;
        }
    }

    private class DataTask extends AsyncTask<URL, Void, ArrayList<SongA>> {
        int album_id;
        String album_name;
        int posListView;


        //Download Json
        @Override
        protected ArrayList<SongA> doInBackground(URL... params) {
            ArrayList<SongA> ds = new ArrayList<>();
            try {
                URL link = params[0];

                InputStreamReader inputStreamReader = new InputStreamReader(link.openStream(), "UTF-8");
                StringWriter writer = new StringWriter();
                IOUtils.copy(inputStreamReader, writer);
                String theString = writer.toString();
                Log.e("TAG", theString);
                DataA dataA = new Gson().fromJson(theString, DataA.class);
                ds = dataA.getSong();
                album_id = dataA.getAlbum_id();
                album_name = dataA.getAlbum_name();
            } catch (Exception ex) {
                Log.e("LOI", ex.toString());
            }
            return ds;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            songArrayAdapter.clear();
        }

        @Override
        protected void onPostExecute(final ArrayList<SongA> songs) {
            super.onPostExecute(songs);
            songArrayAdapter.clear();
            songArrayAdapter.addAll(songs);

            txtsongAlbum.setText(album_name);
            //Tương tác trên listview
            lvPlaylist_ThieuNhi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tam = machuyendoi;
                    Log.e("MACHUYENDOI",tam + "");
                    if(tam == 0) {
                        posListView = position;
                        Log.e("MACHUYENDOI", machuyendoi + " ");
                        try {
                            if (player != null && player.isPlaying()) {
                                player.stop();
                                player.release();
                                player = null;
                            }

                            player = new MediaPlayer();
                            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            player.setDataSource(String.format("http://103.27.236.133:3000%s", songs.get(position).getSong_src()));
                            player.prepare();
                            player.start();
                            seekBar.setMax(player.getDuration());
                            seekUpdation();
                            playButton.setVisibility(View.VISIBLE);
                            playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                            btnstop.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Log.e("LOI", e.toString());
                        }
                    } else if (tam == 1){
                        class RequetsTask extends AsyncTask<URL,Void,ArrayList<SongA>>{


                            @Override
                            protected ArrayList<SongA> doInBackground(URL... params) {
                                URL link = params[0];

                                InputStreamReader inputStreamReader = null;
                                try {
                                    inputStreamReader = new InputStreamReader(link.openStream(), "UTF-8");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                StringWriter writer = new StringWriter();
                                try {
                                    IOUtils.copy(inputStreamReader, writer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String theString = writer.toString();
                                Log.e("TAG", theString);
                                return null;
                            }
                        }

                        URL url1 = null;
                        try {
                            url1 = new URL("http://103.27.236.133:3000/api/v1/play/" + songs.get(position).getSong_id());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        RequetsTask requetsTask = new RequetsTask();
                        requetsTask.execute(url1);
                    }

                }
            });
            lvPlayList_NgoaiQuoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    posListView = position;
                    tam = machuyendoi;
                    if(tam == 0) {
                        try {
                            if (player != null && player.isPlaying()) {
                                player.stop();
                                player.release();
                                player = null;
                            }

                            player = new MediaPlayer();
                            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            player.setDataSource(String.format("http://103.27.236.133:3000%s", songs.get(position).getSong_src()));
                            player.prepare();
                            player.start();
                            seekBar.setMax(player.getDuration());
                            seekUpdation();
                            playButton.setVisibility(View.VISIBLE);
                            playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                            btnstop.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Log.e("LOI", e.toString());
                        }
                    }else if (tam == 1){
                        class RequetsTask1 extends AsyncTask<URL,Void,ArrayList<SongA>>{


                            @Override
                            protected ArrayList<SongA> doInBackground(URL... params) {
                                URL link = params[0];

                                InputStreamReader inputStreamReader = null;
                                try {
                                    inputStreamReader = new InputStreamReader(link.openStream(), "UTF-8");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                StringWriter writer = new StringWriter();
                                try {
                                    IOUtils.copy(inputStreamReader, writer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String theString = writer.toString();
                                Log.e("TAG", theString);
                                return null;
                            }
                        }

                        URL url1 = null;
                        try {
                            url1 = new URL("http://103.27.236.133:3000/api/v1/play/" + songs.get(position).getSong_id());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        RequetsTask1 requetsTask = new RequetsTask1();
                        requetsTask.execute(url1);
                    }
                }
            });
            lvPlayList_Truyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    posListView = position;
                    tam = machuyendoi;
                    if(tam == 0) {
                        try {
                            if (player != null && player.isPlaying()) {
                                player.stop();
                                player.release();
                                player = null;
                            }

                            player = new MediaPlayer();
                            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            player.setDataSource(String.format("http://103.27.236.133:3000%s", songs.get(position).getSong_src()));
                            player.prepare();
                            player.start();
                            seekBar.setMax(player.getDuration());
                            seekUpdation();
                            playButton.setVisibility(View.VISIBLE);
                            playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                            btnstop.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Log.e("LOI", e.toString());
                        }
                    }else if (tam == 1){
                        class RequetsTask2 extends AsyncTask<URL,Void,ArrayList<SongA>>{


                            @Override
                            protected ArrayList<SongA> doInBackground(URL... params) {
                                URL link = params[0];

                                InputStreamReader inputStreamReader = null;
                                try {
                                    inputStreamReader = new InputStreamReader(link.openStream(), "UTF-8");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                StringWriter writer = new StringWriter();
                                try {
                                    IOUtils.copy(inputStreamReader, writer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String theString = writer.toString();
                                Log.e("TAG", theString);
                                return null;
                            }
                        }

                        URL url1 = null;
                        try {
                            url1 = new URL("http://103.27.236.133:3000/api/v1/play/" + songs.get(position).getSong_id());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        RequetsTask2 requetsTask = new RequetsTask2();
                        requetsTask.execute(url1);
                    }
                }
            });
            //play bài hát trước
            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int listSize = songs.size();

                    if (posListView == 0) {
                        posListView = listSize - 1;
                    } else {
                        posListView--;
                    }

                    try {
                        if (player != null && player.isPlaying()) {
                            player.stop();
                            player.release();
                            player = null;
                        }

                        Log.e("TAG", "Play cur:" + posListView + ' ' + songs.get(posListView).toString());

                        player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(String.format("http://103.27.236.133:3000%s", songs.get(posListView).getSong_src()));
                        player.prepare();
                        player.start();
                        seekBar.setMax(player.getDuration());
                        seekUpdation();
                        playButton.setImageResource(R.drawable.ic_pause_black_24dp);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            //play bài hát sau
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int listSize = songs.size();
                    if (posListView == listSize - 1) {
                        posListView = 0;
                    } else {
                        posListView++;
                    }
                    try {
                        if (player != null && player.isPlaying()) {
                            player.stop();
                            player.release();
                            player = null;
                        }

                        player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(String.format("http://103.27.236.133:3000%s", songs.get(posListView).getSong_src()));
                        player.prepare();
                        player.start();
                        seekBar.setMax(player.getDuration());
                        seekUpdation();
                        playButton.setImageResource(R.drawable.ic_pause_black_24dp);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            //bắt đầu và tạm dừng
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.isPlaying()) {
                        player.pause();
                        playButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    } else {
                        player.start();
                        playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    }
                }
            });
            //dừng
            btnstop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.isPlaying()) {
                        player.stop();
                        player.prepareAsync();
                        player.seekTo(0);
                        playButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    } else {
                        player.start();
                    }
                }
            });
            //bắt đầu lại
            loopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!player.isPlaying()) {
                        player.start();
                    }
                }
            });
            //Thanh thời gian thay đổi
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                boolean userTouch;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (player.isPlaying() && userTouch) {
                        player.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    userTouch = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    userTouch = false;
                }
            });

        }

        //
        Runnable run = new Runnable() {
            @Override
            public void run() {
                seekUpdation();
            }
        };

        private void seekUpdation() {
            if(player != null) {
                seekBar.setProgress(player.getCurrentPosition());
                seekHandler.postDelayed(run, 1000);
                txtCount.setText(milliSecondsToTimer(player.getCurrentPosition()) + "/" + milliSecondsToTimer(player.getDuration()));
            }
        }

        private String milliSecondsToTimer(long milliseconds) {
            String finalTimerString = "";
            String secondsString = "";

            // Convert total duration into time
            int hours = (int) (milliseconds / (1000 * 60 * 60));
            int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
            int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
            // Add hours if there
            if (hours > 0) {
                finalTimerString = hours + ":";
            }

            // Prepending 0 to seconds if it is one digit
            if (seconds < 10) {
                secondsString = "0" + seconds;
            } else {
                secondsString = "" + seconds;
            }

            finalTimerString = finalTimerString + minutes + ":" + secondsString;

            // return timer string
            return finalTimerString;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem item = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) item.getActionView();
        //searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //socket.disconnect();
        if (player != null && player.isPlaying()) {
            player.release();
            player = null;
        }
    }
}
