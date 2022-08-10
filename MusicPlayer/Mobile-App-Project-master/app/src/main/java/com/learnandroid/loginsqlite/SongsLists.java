package com.learnandroid.loginsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class SongsLists extends AppCompatActivity {

    ImageView play, prev, next, imageView;
    TextView songTitle;
    SeekBar musicSeekBartime, mSeekBarVolume;
    static MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    int currentIndex = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_list);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Initialising Views
        songTitle = findViewById(R.id.songTitle);
        play = findViewById(R.id.btnPlay);
        prev = findViewById(R.id.btnPrev);
        next = findViewById(R.id.btnSkip);
        musicSeekBartime = findViewById(R.id.seekBarTime);
        mSeekBarVolume = findViewById(R.id.seekBarVol);
        imageView = (ImageView) findViewById(R.id.imageView);

        //Creating arraylist to store songs

        ArrayList<Integer> songs = new ArrayList<>();

        songs.add(0, R.raw.copy_light);
        songs.add(1, R.raw.red_swan);
        songs.add(2,R.raw.blue_bigbang);
        songs.add(3,R.raw.brunomars_justthewayyouare);
        songs.add(4,R.raw.taylor_augutst);

        //seekbar volume

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSeekBarVolume.setMax(maxVolume);
        mSeekBarVolume.setProgress(currentVolume);

        mSeekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Initializing MediaPlayer
        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSeekBartime.setMax(mediaPlayer.getDuration());
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.ic_baseline_pause_24);
                }
                songNames();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    next.setImageResource(R.drawable.ic_baseline_skip_next_24);
                }
                if (currentIndex < songs.size() - 1) {
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mediaPlayer.start();
                songNames();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    play.setImageResource(R.drawable.ic_baseline_pause_24);
                }

                if (currentIndex > 0) {
                    currentIndex--;
                } else {
                    currentIndex = songs.size() - 1;
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mediaPlayer.start();
                songNames();
            }
        });
    }

    //Shows song name on textView
    @SuppressLint("SetTextI18n")
    private void songNames() {
        if (currentIndex == 0) {
            songTitle.setText("Copy Light - TK from Ling Tosite Sigure");
            imageView.setImageResource(R.drawable.copylight);
        } else if (currentIndex == 1) {
            songTitle.setText("Red Swan - Yoshiki ft Hyde");
            imageView.setImageResource(R.drawable.redswan);
        } else if (currentIndex == 2) {
            songTitle.setText("Blue - BIGBANG");
            imageView.setImageResource(R.drawable.blue_bigbang);
        } else if (currentIndex == 3) {
            songTitle.setText("Just The Way You Are - Bruno Mars");
            imageView.setImageResource(R.drawable.justthewayyouare);
        } else if (currentIndex == 4) {
            songTitle.setText("August - Taylor Swift");
            imageView.setImageResource(R.drawable.august);
        }

        //Seekbar duration
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                musicSeekBartime.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
            }
        });

        musicSeekBartime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    musicSeekBartime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @SuppressLint({"Handler Leak", "HandlerLeak"})
    Handler handler = new Handler (){
        @Override
        public void handleMessage (Message msg) {
            musicSeekBartime.setProgress(msg.what);
        }
    };
}
