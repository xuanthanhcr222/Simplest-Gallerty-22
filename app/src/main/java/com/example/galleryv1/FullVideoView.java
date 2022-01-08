package com.example.galleryv1;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FullVideoView extends AppCompatActivity {

    VideoView view;
    ImageView imgView;
    SeekBar seekBar;
    String videoUrl;
    boolean isPlay=false;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_video_view);

        view=(VideoView) findViewById(R.id.videoView);
        imgView=(ImageView) findViewById(R.id.toogleButton);
        seekBar=(SeekBar) findViewById(R.id.seekBar);

        videoUrl=getIntent().getStringExtra("video");
        view.setVideoPath(videoUrl);

        handler=new Handler();
        view.start();

        isPlay=true;
        imgView.setImageResource(R.drawable.pause_button);
        updateSeekBar();

    }

    private void updateSeekBar() {
        handler.postDelayed(updateTimeTask,100);
    }

    public Runnable updateTimeTask=new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(view.getCurrentPosition());
            seekBar.setMax(view.getDuration());
            handler.postDelayed(this,100);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    handler.removeCallbacks(updateTimeTask);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    handler.removeCallbacks(updateTimeTask);
                    view.seekTo(seekBar.getProgress());
                    updateSeekBar();
                }
            });
        }
    };

    public void toogleMethod(View v){
        if(isPlay){
            view.pause();
            isPlay=false;
            imgView.setImageResource(R.drawable.play_button);
        }
        else {
            view.start();
            updateSeekBar();
            isPlay=true;
            imgView.setImageResource(R.drawable.pause_button);
        }
    }
}
