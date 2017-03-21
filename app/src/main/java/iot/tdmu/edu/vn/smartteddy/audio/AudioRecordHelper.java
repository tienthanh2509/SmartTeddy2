package iot.tdmu.edu.vn.smartteddy.audio;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

import iot.tdmu.edu.vn.smartteddy.utils.FileUtil;

/**
 * Created by nguye on 3/21/2017.
 *
 */

public class AudioRecordHelper {
    private final String TAG = AudioRecordHelper.class.getSimpleName();

    private String mFileName = null;
    private MediaRecorder mRecorder = null;

    public AudioRecordHelper(Context context) {
        // Record to the external cache directory for visibility
        mFileName = context.getExternalCacheDir().getAbsolutePath();
        mFileName += "/smartteddy-2017-tdmu-g1.m4a";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mFileName);



        Log.d(TAG, mFileName);
    }

    public void startRecording() {
        try {
            mRecorder.prepare();
            Log.d(TAG, "Bắt đầu ghi âm");
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "prepare() failed");
        }
    }

    public void stopRecording() {
        Log.d(TAG, "Dừng ghi âm");

        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRecording() {
        return (mRecorder != null);
    }

    public byte[] getByteArray() {
        return FileUtil.getByteArrayFromLocalFile(mFileName);
    }
}
