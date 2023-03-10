package com.foncup.penalty.sport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    boolean sound,music,vibra;

    SoundPool sound_Level;
    int sound_click;
    final int MAX_STREAMS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sound = sharedPreferences.getBoolean("sound",true);
        music = sharedPreferences.getBoolean("music",true);
        vibra = sharedPreferences.getBoolean("vibra",true);

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_click = sound_Level.load(getApplicationContext(),R.raw.click,1); //1,2,4,7

    }

    public void toPlay(View view) {
        if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent); finish();
    }

    public void toTop(View view) {
        if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
        Intent intent = new Intent(MainActivity.this, TopActivity.class);
        startActivity(intent); finish();
    }

    public void toOptions(View view) {
        if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
        Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
        startActivity(intent); finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Window w = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}