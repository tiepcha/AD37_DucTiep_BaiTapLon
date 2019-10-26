package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class Playing extends AppCompatActivity {

    VideoView videoView ;
    TextView tvtimecr;
    SeekBar pb;
    int d;
    private Handler updateHandler = new Handler();
    MediaController mediaController;
    String timecr;
    Boolean timerun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_playing);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Intent intent = getIntent();
        String a = intent.getStringExtra("mp4");
        videoView = findViewById( R.id.vv);
        tvtimecr = findViewById( R.id.tvtimecr);
        pb = findViewById( R.id.pb);
        final TextView tvtime_total = (TextView) findViewById(R.id.tvtime_total);
//        Drawable seekbarThumb = getResources().getDrawable( R.drawable.ring );


        videoView.setVideoPath(a);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    d = videoView.getDuration();


                    pb.setMax(d);
                    updateHandler.postDelayed(updateVideoTime, 1000);
                    tvtime_total.setText(String.format("%02d" , (d/1000)/60)+":"+ String.format("%02d" , (d/1000)%60)) ;
                }
            });



        videoView.start();


//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        videoView.setMediaController(mediaController);

        tvtimecr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerun = !timerun;
            }
        });


        pb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = pb.getProgress();
                videoView.seekTo(a);


            }
        });


    }


    private Runnable updateVideoTime = new Runnable() {
        @Override
        public void run() {
            long currentPosition = videoView.getCurrentPosition();
            pb.setProgress((int) currentPosition);


            int timecurrent = videoView.getCurrentPosition()/1000;

            int min = 0 ;
            if (timerun) {
            while (timecurrent>60){
                    min++;
                    timecurrent-=60 ;
            }
                timecr = String.format("%02d", min)+":"+String.format("%02d", timecurrent);
                tvtimecr.setText(timecr);
            }

            else {
                int timecurrent_left = d / 1000 - timecurrent;
                while (timecurrent_left > 60) {
                    min++;
                    timecurrent_left -= 60;
                }

                timecr = "-"+String.format("%02d", min)+":"+String.format("%02d", timecurrent_left);
                tvtimecr.setText(timecr);
            }








            updateHandler.postDelayed(this, 100);



        }
    };
}

