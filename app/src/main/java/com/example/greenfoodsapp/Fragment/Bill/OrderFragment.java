package com.example.greenfoodsapp.Fragment.Bill;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.greenfoodsapp.Adapter.OderViewPagerAdapter;
import com.example.greenfoodsapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

// Nguyễn Đức Huy - 20145449
public class OrderFragment extends Fragment {

    private TabLayout tabLayout_Order;
    private ViewPager2 viewPager_Order;
    private OderViewPagerAdapter orderViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        // Khởi tạo TabLayout và ViewPager2
        tabLayout_Order = view.findViewById(R.id.tabLayout_Order);
        viewPager_Order = view.findViewById(R.id.viewPager_Order);

        // Tạo adapter cho ViewPager2
        orderViewPagerAdapter = new OderViewPagerAdapter(this);
        viewPager_Order.setAdapter(orderViewPagerAdapter);

        // Thiết lập TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout_Order, viewPager_Order, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Đơn hàng hiện tại");
                        break;
                    case 1:
                        tab.setText("Lịch sử");
                        break;
                }
            }
        }).attach();

        return view;
    }
}
