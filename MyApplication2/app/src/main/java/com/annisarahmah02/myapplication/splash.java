package com.annisarahmah02.myapplication;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
// ... (import statements and class declaration)

public class splash extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ImageView splashImage = findViewById(R.id.splash_image);
        AnimationDrawable bufferingAnimation = (AnimationDrawable) splashImage.getDrawable();

        // Start the animation
        bufferingAnimation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop the animation and proceed to the next activity
                bufferingAnimation.stop();
                Intent intent = new Intent(splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}