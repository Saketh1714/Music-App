package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    TextView textview;
    ImageView play,next,previous;
    ArrayList<File>songs;
    MediaPlayer mediaPlayer;
    String textcontent;
    int position;
    SeekBar seekBar;
    Thread updateseek;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textview=findViewById(R.id.textView);
        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        previous=findViewById(R.id.previous);
        seekBar=findViewById(R.id.seekBar);


        Intent intent=getIntent();
        Bundle bundle= getIntent().getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("songlist");
        textcontent= intent.getStringExtra("currentsong");
        textview.setText(textcontent);
        textview.setSelected(true);
        position=intent.getIntExtra("position",0);
        Uri uri= Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        updateseek=new Thread(){
            @Override
            public void run() {
                int currentposition=0;
                try{
                      while(currentposition<mediaPlayer.getDuration()){
                          currentposition=mediaPlayer.getCurrentPosition();
                          seekBar.setProgress(currentposition);
                          sleep(800);
                      }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        };
        updateseek.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    play.setImageResource(R.drawable.img_1);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.img_4);
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0)
                {
                    position=position-1;
                }
               else{
                   position=songs.size()-1;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);

                play.setImageResource(R.drawable.img_4);
                mediaPlayer.start();

                seekBar.setMax(mediaPlayer.getDuration());
                textcontent=songs.get(position).getName().toString();
                textview.setText(textcontent);
                textview.setSelected(true);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1)
                {
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);

                play.setImageResource(R.drawable.img_4);
                mediaPlayer.start();

                seekBar.setMax(mediaPlayer.getDuration());
                textcontent=songs.get(position).getName().toString();
                textview.setText(textcontent);
                textview.setSelected(true);
            }
        });


    }
}