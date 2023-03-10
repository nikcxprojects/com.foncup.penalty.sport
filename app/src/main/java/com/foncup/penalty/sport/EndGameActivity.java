package com.foncup.penalty.sport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndGameActivity extends AppCompatActivity {

    TextView points_text, menu_text;
    LinearLayout linear_again;

    int points;
    String records;

    List<Integer> recordsList = new ArrayList<>();

    TinyDB tinydb;

    boolean sound,music,vibra;

    SoundPool sound_Level;
    int sound_click;
    final int MAX_STREAMS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sound = sharedPreferences.getBoolean("sound",true);
        music = sharedPreferences.getBoolean("music",true);
        vibra = sharedPreferences.getBoolean("vibra",true);

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_click = sound_Level.load(getApplicationContext(),R.raw.click,1); //1,2,4,7

        tinydb = new TinyDB(getApplicationContext());
        recordsList.addAll(tinydb.getListInt("records"));

        points_text = findViewById(R.id.points_text);
        menu_text = findViewById(R.id.menu_text);
        linear_again = findViewById(R.id.linearAgain);

        points = getIntent().getIntExtra("points",0);
        points_text.setText(""+points);

        if (!recordsList.contains(points)) { recordsList.add(points); }
        Collections.sort(recordsList);
        Collections.reverse(recordsList);
        if (recordsList.size()>5) { recordsList.remove(5); }

        Log.d("recordsList",""+recordsList);

        menu_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
                Intent intent = new Intent(EndGameActivity.this, MainActivity.class);
                startActivity(intent); finish();
            }
        });

        linear_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
                Intent intent = new Intent(EndGameActivity.this, GameActivity.class);
                startActivity(intent); finish();
            }
        });

        setRecords(recordsList);

    }

    private void setRecords(List<Integer> recordsList) {
        tinydb.putListInt("records", recordsList);
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