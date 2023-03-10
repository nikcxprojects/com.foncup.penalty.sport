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
import android.widget.CompoundButton;
import android.widget.TextView;

public class OptionsActivity extends AppCompatActivity {

    TextView back_text;

    TextView music_on_off, sound_on_off, vibra_on_off;

    boolean sound,music,vibra;

    SoundPool sound_Level;
    int sound_click;
    int sound_on_off_click;
    final int MAX_STREAMS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sound = sharedPreferences.getBoolean("sound",true);
        music = sharedPreferences.getBoolean("music",true);
        vibra = sharedPreferences.getBoolean("vibra",true);

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_click = sound_Level.load(getApplicationContext(),R.raw.click,1); //1,2,4,7
        sound_on_off_click = sound_Level.load(getApplicationContext(),R.raw.click4,1); //1,2,4,7

        back_text = findViewById(R.id.back_text);
        music_on_off = findViewById(R.id.music_on_off);
        sound_on_off = findViewById(R.id.sound_on_off);
        vibra_on_off = findViewById(R.id.vibra_on_off);

        updateSettings();

        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
                Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                startActivity(intent); finish();
            }
        });

        music_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound) { sound_Level.play(sound_on_off_click, 0.8f, 0.8f, 0, 0, 1); }
                music=!music;
                editor.putBoolean("music",music).apply();
                updateSettings();
            }
        });

        sound_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound) { sound_Level.play(sound_on_off_click, 0.8f, 0.8f, 0, 0, 1); }
                sound=!sound;
                editor.putBoolean("sound",sound).apply();
                updateSettings();
            }
        });

        vibra_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound) { sound_Level.play(sound_on_off_click, 0.8f, 0.8f, 0, 0, 1); }
                vibra=!vibra;
                editor.putBoolean("vibra",vibra).apply();
                updateSettings();
            }
        });

    }

    private void updateSettings() {
        if (music){ music_on_off.setText("ON"); }
        else { music_on_off.setText("OFF"); }

        if (sound){ sound_on_off.setText("ON"); }
        else { sound_on_off.setText("OFF"); }

        if (vibra){ vibra_on_off.setText("ON"); }
        else { vibra_on_off.setText("OFF"); }
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