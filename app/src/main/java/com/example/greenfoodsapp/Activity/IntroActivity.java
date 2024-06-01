package com.example.greenfoodsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.greenfoodsapp.Adapter.SliderAdapter;
import com.example.greenfoodsapp.R;


// Ông Vũ Hữu Tài - 21110796
public class IntroActivity extends AppCompatActivity {

    ViewPager viewPagerIntro;
    LinearLayout dotsLayoutIntro;
    SliderAdapter sliderAdapterIntro;
    TextView[] dots;

    Button btn_started_intro;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPagerIntro = findViewById(R.id.slider_intro);
        dotsLayoutIntro = findViewById(R.id.dots_intro);
        btn_started_intro = findViewById(R.id.btn_started_intro);

        sliderAdapterIntro = new SliderAdapter(this);
        viewPagerIntro.setAdapter(sliderAdapterIntro);
        addDots(0); // Thêm các dấu chấm hiển thị vị trí của slider
        viewPagerIntro.addOnPageChangeListener(changeListener); // Thêm sự kiện lắng nghe khi trang được thay đổi

        btn_started_intro.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class)); // Chuyển sang MainActivity khi bấm nút bắt đầu
        });

        getSupportActionBar().hide(); // Ẩn actionbar
    }

    // Hàm bỏ qua intro và chuyển sang MainActivity
    public void skip(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    // Hàm chuyển sang trang tiếp theo của intro
    public void next(View view){
        viewPagerIntro.setCurrentItem(currentPos + 1);
    }

    // Thêm các dấu chấm để hiển thị vị trí hiện tại của slider
    private void addDots(int position){
        dots = new TextView[3];
        dotsLayoutIntro.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);

            dotsLayoutIntro.addView(dots[i]);
        }

        if (dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.black)); // Đổi màu của dấu chấm tương ứng với trang hiện tại
        }
    }

    // Sự kiện lắng nghe khi trang được thay đổi
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addDots(position); // Cập nhật lại các dấu chấm khi trang thay đổi
            currentPos = position;

            if (position == 0){
                btn_started_intro.setVisibility(View.INVISIBLE); // Ẩn nút bắt đầu khi ở trang đầu tiên
            }else if (position == 1){
                btn_started_intro.setVisibility(View.INVISIBLE); // Ẩn nút bắt đầu khi ở trang thứ hai
            }else{
                btn_started_intro.setVisibility(View.VISIBLE); // Hiển thị nút bắt đầu khi ở trang cuối cùng
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}
