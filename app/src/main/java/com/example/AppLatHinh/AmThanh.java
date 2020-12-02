package com.example.AppLatHinh;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class AmThanh  {
    Context context;
    int correct_sound,error_sound,flip_sound,win_sound, gameover_sound,click_sound;
    AudioAttributes attrs;
    SoundPool sp;

    public AmThanh(Context context){
        this.context = context;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void loadAmThanh(){
        attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sp = new SoundPool.Builder()
                .setMaxStreams(10)//tối đa 10 luồng phát một lúc
//                .setAudioAttributes(attrs)
                .build();
        correct_sound = sp.load(context, R.raw.correct, 1);
        error_sound = sp.load(context, R.raw.error, 1);
        flip_sound = sp.load(context, R.raw.flip, 1);
        win_sound = sp.load(context, R.raw.win, 1);
        gameover_sound = sp.load(context, R.raw.gameover, 1);
        click_sound = sp.load(context,R.raw.click,1);
    }
    public void play_flip_sound(){
        sp.play(flip_sound,1,1,1,0,1);
    }
    public void play_error_sound(){
        sp.play(error_sound,1,1,1,0,1);
    }
    public void play_correct_sound(){
        sp.play(correct_sound,1,1,1,0,1);
    }
    public void play_win_sound(){
        sp.play(win_sound,1,1,1,0,1);
    }
    public void play_gameover_sound(){
        sp.play(gameover_sound,1,1,1,0,1);
    }
    public void play_click_sound(){
        sp.play(click_sound,1,1,1,0,1);
    }
    public void pause(){
        sp.autoPause();
        sp.release();
    }

}
