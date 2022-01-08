package com.example.galleryv1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FullVideoView extends AppCompatActivity {

    VideoView view;
    Video video;
    String videoUrl;
    MediaController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_video_view);

        Intent intent = getIntent();
        Bundle videoBundle = intent.getBundleExtra("videoBundle");
        video = (Video) videoBundle.getSerializable("video");


        view = (VideoView) findViewById(R.id.videoView);
        controller = new MediaController(this);
        playVideo(view);

    }


    public void playVideo(View v) {

        videoUrl = video.getPath();
        view.setVideoPath(videoUrl);
        view.start();
        controller.setMediaPlayer(view);
        view.setMediaController(controller);

    }
}