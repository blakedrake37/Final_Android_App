package com.example.greenfoodsapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenfoodsapp.R;


// Lê Nguyễn Toàn Tâm - 21110797
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    SharedPreferences introActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Sử dụng Handler để trì hoãn chuyển sang activity tiếp theo sau 2 giây
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);

        getSupportActionBar().hide(); // Ẩn actionbar
    }

    // Phương thức xác định activity tiếp theo dựa trên việc kiểm tra xem đây có phải là lần đầu mở ứng dụng không
    private void nextActivity() {
        introActivity = getSharedPreferences("introActivity", MODE_PRIVATE);
        boolean isFirstTime = introActivity.getBoolean("firstTime", true);

        if (isFirstTime) {
            // Nếu là lần đầu mở ứng dụng, lưu trạng thái và chuyển đến IntroActivity
            SharedPreferences.Editor editor = introActivity.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();

            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Nếu không phải lần đầu, chuyển trực tiếp đến MainActivity
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finishAffinity(); // Kết thúc tất cả các activity khác để tránh quay lại SplashActivity
        }
    }
}
