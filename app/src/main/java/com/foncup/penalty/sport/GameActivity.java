package com.foncup.penalty.sport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    ImageView imageFootball,imageBall,imageGoalkeeper,imageNet;
    TextView textFinish;
    public static CountDownTimer timer;
    public static int time=0;

    float posX, posY;
    float startX, startY;

    float poleY;
    float scale;
    int maxHeight;

    float marginLeft;
    float posKeeperX;
    float posNetX,posNetY;

    float posBallXStart,posBallXEnd;
    float posBallYStart,posBallYEnd;
    float posNetTop,posNetBottom;
    float posNetLeft,posNetRight;
    float posKeepXStart,posKeepXEnd;
    float posKeepYStart,posKeepYEnd;

    int points=0;
    TextView textPoints;

    TextView textTime;
    CountDownTimer timerGame;
    int timeGame=90;
    boolean isPause=false;

    ConstraintLayout constraint_pause;
    TextView menu_text;
    ImageView imagePause;
    LinearLayout linearGo;

    boolean sound,music,vibra;

    SoundPool sound_Level;
    int sound_ball;
    int sound_referee1;
    int sound_referee2;
    int sound_click;
    final int MAX_STREAMS = 5;

    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sound = sharedPreferences.getBoolean("sound",true);
        music = sharedPreferences.getBoolean("music",true);
        vibra = sharedPreferences.getBoolean("vibra",true);

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_ball = sound_Level.load(getApplicationContext(),R.raw.sound33,1);  //33
        sound_referee1 = sound_Level.load(getApplicationContext(),R.raw.sound7,1); //1,2,4,7
        sound_referee2 = sound_Level.load(getApplicationContext(),R.raw.sound1,1); //1,2,4,7
        sound_click = sound_Level.load(getApplicationContext(),R.raw.click,1); //1,2,4,7

        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.atmoshere); //загрузка пеера
        mPlayer.setLooping(true);//зацикливание
        mPlayer.setVolume(0.5f, 0.5f);

        imageFootball = findViewById(R.id.imageFootball);
        imageBall = findViewById(R.id.imageBall);
        imageGoalkeeper = findViewById(R.id.imageGoalkeeper);
        imageNet = findViewById(R.id.imageNet);
        textPoints = findViewById(R.id.textPoints);
        textTime = findViewById(R.id.textTime);
        textFinish = findViewById(R.id.textFinish);
        imagePause = findViewById(R.id.imagePause);
        menu_text = findViewById(R.id.menu_text);
        linearGo = findViewById(R.id.linearGo);
        constraint_pause = findViewById(R.id.constraint_pause);
        constraint_pause.setVisibility(View.GONE);

        textPoints.setText(""+points);
        textTime.setText(""+timeGame+"s");

        scale = imageBall.getScaleX();
        time=0;

        StartTimer();

        imageFootball.setOnTouchListener(new SwipeListener(this));

        imagePause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerGame!=null) { timerGame.cancel();}
                constraint_pause.setVisibility(View.VISIBLE);
                imageFootball.setEnabled(false);
                isPause = true;
                if (mPlayer != null&&music) { mPlayer.pause(); }
                if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
            }
        });

        linearGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerGame!=null&&isPause) { timerGame.start(); }
                constraint_pause.setVisibility(View.GONE);
                imageFootball.setEnabled(true);
                isPause = false;
                if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
                if (!mPlayer.isPlaying()&&music){ mPlayer.start(); }
            }
        });

        menu_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerGame!=null) { timerGame.cancel();}
                if (sound) { sound_Level.play(sound_click, 0.8f, 0.8f, 0, 0, 1); }
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent); finish();
            }
        });
    }

    private void StartTimer() {
        timerGame = new CountDownTimer(timeGame*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeGame--;
                textTime.setText(""+timeGame+"s");
                if (timeGame==0){
                    timerGame.cancel();
                    Intent intent = new Intent(GameActivity.this, EndGameActivity.class);
                    intent.putExtra("points", points);
                    startActivity(intent); finish();
                }
            }

            @Override
            public void onFinish() {
                timeGame = 0;
                textTime.setText(""+timeGame+"s");
                Intent intent = new Intent(GameActivity.this, EndGameActivity.class);
                intent.putExtra("points", points);
                startActivity(intent); finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        if (timerGame!=null) { timerGame.cancel(); }
        constraint_pause.setVisibility(View.VISIBLE);
        if (mPlayer != null&&music) { mPlayer.pause(); }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null&&music) {
            mPlayer.pause();//пауза плеера
        }
        if (timerGame!=null) { timerGame.cancel(); }
        imageFootball.setEnabled(false);
        isPause = true;
    }

    public void StartBall(int t, float angle, float timeSpeed) {
        time=t;
        poleY = imageFootball.getY();
        float speed= (float) (10*(2-timeSpeed));
        maxHeight = (int) (poleY-100-8*speed);

        marginLeft = imageGoalkeeper.getX();
        posKeeperX = imageGoalkeeper.getX();
        posNetX = imageNet.getX();
        posNetY = imageNet.getY();

        posX = imageBall.getX();
        posY = imageBall.getY();
        startX = imageBall.getX();
        startY = imageBall.getY();

        Random randSide = new Random();
        int side = randSide.nextInt(2);

        textFinish.setVisibility(View.GONE);

        if (sound) { sound_Level.play(sound_ball, 0.8f, 0.8f, 0, 0, 1); }

        timer = new CountDownTimer(time,10) {
            @Override
            public void onTick(long millisUntilFinished) {

                if (side==0){
                    posKeeperX-=0.1*speed;
                    imageGoalkeeper.setX(posKeeperX);
                }
                else if (side==1){
                    posKeeperX+=0.1*speed;
                    imageGoalkeeper.setX(posKeeperX);
                }

                time-=10;
                posY-=speed;
                posX += -angle * speed;
                if (posY>maxHeight) {
                    imageBall.setY(posY);
                    imageBall.setX(posX);
                    float newScale = (float) (0.25+(scale-maxHeight/posY));
                    imageBall.setScaleX(newScale);
                    imageBall.setScaleY(newScale);
                }
                else{
                    timer.cancel();
                    CheckGoal();
                }
            }

            @Override
            public void onFinish() {
                CheckGoal();
            }
        }.start();

    }

    private void CheckGoal() {

        posBallXStart = (imageBall.getX()+(imageBall.getWidth()*(1-imageBall.getScaleX()))/2);
        posBallXEnd = (imageBall.getX()+(imageBall.getWidth()*(1-imageBall.getScaleX()))/2+imageBall.getWidth()*imageBall.getScaleX());
        posBallYStart = (imageBall.getY()+(imageBall.getHeight()*(1-imageBall.getScaleY()))/2);
        posBallYEnd = (imageBall.getY()+(imageBall.getHeight()*(1-imageBall.getScaleY()))/2+imageBall.getHeight()*imageBall.getScaleY());

        posNetTop = imageNet.getY();
        posNetBottom = poleY;
        posNetLeft = imageNet.getX();
        posNetRight = imageNet.getX()+imageNet.getWidth();

        posKeepXStart = imageGoalkeeper.getX();
        posKeepXEnd = imageGoalkeeper.getX()+imageGoalkeeper.getWidth();
        posKeepYStart = imageGoalkeeper.getY();
        posKeepYEnd = imageGoalkeeper.getY()+imageGoalkeeper.getHeight();

        Log.d("finishBall",""+posBallXStart+" "+posKeepXEnd);
        if (posBallXStart>posNetLeft&&posBallXEnd<posNetRight&&posBallYStart>posNetTop&&posBallYEnd<posNetBottom&&
                (posBallXStart>posKeepXEnd||posBallXEnd<posKeepXStart)){
            textFinish.setVisibility(View.VISIBLE);
            textFinish.setText("GOAL!");
            if (sound) { sound_Level.play(sound_referee1, 0.6f, 0.6f, 0, 0, 1); }
            points++;
            textPoints.setText(""+points);}
        else{
            textFinish.setVisibility(View.VISIBLE);
            if (sound) { sound_Level.play(sound_referee2, 0.6f, 0.6f, 0, 0, 1); }

            if (vibra) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }

            textFinish.setText("SHOT"); }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time=0;
                imageBall.setY(startY);
                imageBall.setX(startX);
                imageBall.setScaleX(scale);
                imageBall.setScaleY(scale);
                imageGoalkeeper.setX(marginLeft);
                textFinish.setVisibility(View.GONE);
            }
        }, 400);

    }


    @Override
    protected void onResume() {
        super.onResume();
        final Window w = getWindow();

        if (!mPlayer.isPlaying()&&music&&!isPause){ mPlayer.start(); }

        if (isPause) { constraint_pause.setVisibility(View.VISIBLE); }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}