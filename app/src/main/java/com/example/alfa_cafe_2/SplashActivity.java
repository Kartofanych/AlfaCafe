package com.example.alfa_cafe_2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatDelegate;

import static java.security.AccessController.getContext;

public class SplashActivity extends Activity {
    private RelativeLayout alpha;
    private Animation anim1, anim2;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "null";
    public static final String APP_PREFERENCES_KOL = "zero";
    public SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.getString(APP_PREFERENCES_KOL, "zero").equals("zero")){
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_KOL, "one");
            editor.putString(APP_PREFERENCES_NAME, String.valueOf(Math.random()));
            editor.apply();
        }
        Log.d("1", mSettings.getString(APP_PREFERENCES_NAME, "null"));

        alpha = findViewById(R.id.alpha);
        anim1 = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim2 = AnimationUtils.loadAnimation(this,R.anim.alpha_to_zero);
        alpha.setVisibility(View.VISIBLE);
        alpha.setAnimation(anim1);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 2000);


    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |                                   //скрываем нижнюю панель с кнопками
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |                    //скрываем нижнюю панель с кнопками
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |                         //скрываем нижнюю панель с кнопками
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |                           //скрываем нижнюю панель с кнопками
                        View.SYSTEM_UI_FLAG_FULLSCREEN |                                //скрываем нижнюю панель с кнопками
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //скрываем нижнюю панель с кнопками
    }
}
