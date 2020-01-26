package com.e.findingmemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    private int time =3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //setelah loading, makan akan pindah ke main activity
                Intent loginAct = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(loginAct);
                finish();
            }
        }, time);
    }
}
