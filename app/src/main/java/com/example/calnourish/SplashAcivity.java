package com.example.calnourish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

public class SplashAcivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashAcivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
