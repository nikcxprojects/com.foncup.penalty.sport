package com.foncup.penalty.sport;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SwipeListener implements View.OnTouchListener {
    //create gesture detector variable
    GestureDetector gestureDetector;

    //create constructor
    static final String logTag = "ActivitySwipeDetector";
    private GameActivity activity;
    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;
    double timeStart, timeFinish;

    // private MainActivity mMainActivity;

    public SwipeListener(GameActivity gameActivity) {
        activity = gameActivity;
    }

    public void onRightToLeftSwipe() {
        Log.i(logTag, "RightToLeftSwipe!");
        Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onLeftToRightSwipe() {
        Log.i(logTag, "LeftToRightSwipe!");
        Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onTopToBottomSwipe(float angle, float time) {
       // Log.i(logTag, "onTopToBottomSwipe!");
       // Toast.makeText(activity, "onTopToBottomSwipe, on "+time, Toast.LENGTH_SHORT).show();
        activity.StartBall(3000,angle,time);
    }

    public void onBottomToTopSwipe() {
        Log.i(logTag, "onBottomToTopSwipe!");
        Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                timeStart = System.currentTimeMillis();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe vertical?
                if (Math.abs(deltaY) > MIN_DISTANCE&&GameActivity.time==0) {
                    // top or down
                    float time = (float) ((System.currentTimeMillis()-timeStart)/100);
                    if (deltaY > 0) {
                        if (deltaX < 0&&time<2f) {
                            this.onTopToBottomSwipe(deltaX/deltaY,time);
                            return true;
                        }
                        if (deltaX > 0&&time<2f) {
                            this.onTopToBottomSwipe(deltaX/deltaY,time);
                            return true;
                        }
                        return true;
                    }
                    if (deltaY < 0) {
                        //this.onBottomToTopSwipe();
                        return true;
                    }
                } else {
                   // Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                    // return false; // We don't consume the event
                }

                return false; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

}
