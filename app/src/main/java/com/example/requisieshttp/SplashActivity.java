package com.example.requisieshttp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



      final  Handler handler = new Handler();
      final  Runnable runnable = new Runnable() {
          @Override
          public void run() {
              Intent activity = new Intent(SplashActivity.this, MainActivity.class);
              startActivity(activity);
              overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
              finishAffinity();


          }
      };
      handler.postDelayed(runnable, 2000);
    }
}
