package com.example.greenfoodsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.greenfoodsapp.R;
// Lê Nguyễn Toàn Tâm - 21110797
public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    // Constructor nhận context của ứng dụng
    public SliderAdapter(Context context) {
        this.context = context;
    }

    // Mảng chứa các hình ảnh của slider
    private int[] image = {
            R.drawable.intro1,
            R.drawable.intro2,
            R.drawable.intro4,
    };

    // Mảng chứa các tiêu đề của slider
    private int[] headings = {
            R.string.slide1_heading,
            R.string.slide2_heading,
            R.string.slide3_heading,
    };

    // Mảng chứa các thông tin của slider
    private int[] infomation = {
            R.string.slide1_infor,
            R.string.slide2_infor,
            R.string.slide3_infor,
    };

    @Override
    public int getCount() {
        // Trả về số lượng trang của slider
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        // Xác định xem view có phải là đối tượng hiện tại hay không
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    // Tạo và trả về một trang (view) cho vị trí cụ thể
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false);

        ImageView imageView = view.findViewById(R.id.slide_image);
        TextView heading = view.findViewById(R.id.slide_heading);
        TextView info = view.findViewById(R.id.slide_info);

        // Đặt hình ảnh, tiêu đề và thông tin cho view tại vị trí hiện tại
        imageView.setImageResource(image[position]);
        heading.setText(headings[position]);
        info.setText(infomation[position]);

        container.addView(view);

        return view;
    }

    @Override
    // Xóa trang (view) khỏi container
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
