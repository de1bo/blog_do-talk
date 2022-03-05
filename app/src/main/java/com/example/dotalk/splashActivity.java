package com.example.dotalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class splashActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_splash);


        ImageView gif_image = (ImageView) findViewById(R.id.talk_splash);
        Glide.with(this).load(R.drawable.talk_splash).into(gif_image);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(splashActivity.this, kakaoLogin.class);
                startActivity(main);
                finish();
            }
        }, 1500); // 1.5초 후(1500) 스플래시 화면을 닫습니다 (보통 사용하는 시간)

    }

}