package com.bytedance.videoplayer;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.security.PublicKey;

public class MainActivity extends AppCompatActivity {

    private String TAG="Video Player Debug:";
    public int VideoProgess;
    public String IntentPath;
    public static final int SEEKBAR_CHANGE=1;
    public TextView textView;
    public VideoView videoView;
    public Button button;
    public SeekBar seekBar;
    public int VideoLength;
    public boolean videostate=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        Uri uri=intent.getData();
        Uri bytedance=Uri.parse("file:///sdcard/DCIM/Camera/VID_20200510_121841.mp4");
        IntentPath=bytedance.getPath();
        Log.d(TAG,"the path is"+IntentPath);
        button=findViewById(R.id.bt_start);
        videoView=findViewById(R.id.VV);
        seekBar=findViewById(R.id.sb_video);
        textView=findViewById(R.id.tv_progress);
        videoView.setVideoURI(uri);
        button.setOnClickListener(new ButtonListener());
        videoView.setOnPreparedListener(new VideoPreparedListener());
        initVideoSeekBar();
        VideoThread videoThread=new VideoThread();
        videoThread.start();

    }
    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }

    public class VideoPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            VideoLength=videoView.getDuration();
            Log.d(TAG,"视频长度是："+String.valueOf(VideoLength));
        }
    }

    private class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(videostate)
            {
                videoView.pause();
                button.setBackground(getResources().getDrawable(R.drawable.start));
                videostate=false;
            }
            else
            {
                videoView.start();
                button.setBackground(getResources().getDrawable(R.drawable.pause));
                videostate=true;
            }
        }
    }

    private void initVideoSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(VideoLength*seekBar.getProgress()/100);
                Log.d(TAG,"当前视频播放位置："+String.valueOf(videoView.getCurrentPosition()));
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message message)
        {
            switch (message.what){
                case SEEKBAR_CHANGE:
                    if(VideoLength!=0)
                    {
                        VideoProgess=100*videoView.getCurrentPosition()/VideoLength;
                        Log.d(TAG,"当前进度是："+String.valueOf(VideoProgess));
                        seekBar.setProgress(VideoProgess);
                        textView.setText(initialtime(videoView.getCurrentPosition())+"/"+initialtime(VideoLength));
                    }
                    break;
            }
        }
    };

    class VideoThread extends Thread{
        public void run(){
            while(!Thread.currentThread().isInterrupted())
            {
                if(videoView.getCurrentPosition()==VideoLength&&VideoLength!=0)
                {
                    Thread.currentThread().interrupt();
                }
                Log.d(TAG,"子线程正在改变seekbar");
                Message message=new Message();
                message.what=SEEKBAR_CHANGE;
                handler.sendMessage(message);
                try{
                    Thread.sleep(1000);
                    Log.d(TAG,"子线程休息1s");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public String initialtime(int time){
        int hours;
        int minute;
        int secs;
        int totalsecs=time/1000;
        hours=totalsecs/3600;
        totalsecs=totalsecs%3600;
        minute=totalsecs/60;
        secs=totalsecs%60;
        return(integerToString(hours)+":"+integerToString(minute)+":"+integerToString(secs));
    }

    public String integerToString(int i)
    {
        if(i==0)
        {
            return "00";
        }
        else if(i<10)
        {
            return "0"+i;
        }
        else
        {
            return String.valueOf(i);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}
