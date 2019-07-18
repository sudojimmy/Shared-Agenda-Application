package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.util.AppHelper;

/**
 *
 */
public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // *check
                AppHelper.checkApp();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                        startActivity(intent);
                        LaunchActivity.this.finish();
                    }
                });
            }
        }).start();
    }
}
