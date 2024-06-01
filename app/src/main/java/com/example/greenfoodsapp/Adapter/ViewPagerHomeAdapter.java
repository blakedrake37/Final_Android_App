package com.example.greenfoodsapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.greenfoodsapp.Fragment.BottomNav.HomePageFragment;
import com.example.greenfoodsapp.Fragment.Bill.OrderFragment;
import com.example.greenfoodsapp.Fragment.BottomNav.PersonalFragment;
import com.example.greenfoodsapp.Fragment.BottomNav.VoucherFragment;
// Nguyễn Đức Huy - 20145449
public class ViewPagerHomeAdapter extends FragmentStatePagerAdapter {

    // Constructor nhận FragmentManager và hành vi của adapter
    public ViewPagerHomeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    // Trả về Fragment tương ứng với vị trí (position)
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomePageFragment();
            case 1:
                return new VoucherFragment();
            case 2:
                return new OrderFragment();
            case 3:
                return new PersonalFragment();
            default:
                return new HomePageFragment(); // Trường hợp mặc định nếu position không khớp
        }
    }

    @Override
    // Trả về số lượng các Fragment
    public int getCount() {
        return 4;
    }
}
