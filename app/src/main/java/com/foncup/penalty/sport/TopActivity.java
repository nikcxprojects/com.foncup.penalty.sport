package com.foncup.penalty.sport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopActivity extends AppCompatActivity {

    TextView back_text;
    RecyclerView recycler_top;
    List<Integer> recordsList = new ArrayList<>();

    TinyDB tinydb;

    boolean sound,music,vibra;

    SoundPool sound_Level;
    int sound_click;
    final int MAX_STREAMS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sound = sharedPreferences.getBoolean("sound",true);
        music = sharedPreferences.getBoolean("music",true);
        vibra = sharedPreferences.getBoolean("vibra",true);

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_click = sound_Level.load(getApplicationContext(),R.raw.click,1); //1,2,4,7

        tinydb = new TinyDB(getApplicationContext());
        recordsList.addAll(tinydb.getListInt("records"));

        back_text = findViewById(R.id.back_text);
        recycler_top = findViewById(R.id.recycler_top);

        TopAdapter adapter = new TopAdapter(TopActivity.this, recordsList);
        recycler_top.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
                Intent intent = new Intent(TopActivity.this, MainActivity.class);
                startActivity(intent); finish();
            }
        });

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