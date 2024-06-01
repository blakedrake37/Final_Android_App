package com.example.greenfoodsapp.Fragment.BottomNav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.greenfoodsapp.Fragment.Bill.OrderFragment;
import com.example.greenfoodsapp.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Ông Vũ Hữu Tài - 21110796
public class HomeFragment extends Fragment {

    private FrameLayout frame_Home;
    private BottomNavigationView bottom_nav_Home;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo các thành phần giao diện
        frame_Home = view.findViewById(R.id.frame_Home);
        bottom_nav_Home = view.findViewById(R.id.bottom_nav_Home);

        // Thiết lập fragment mặc định là HomePageFragment
        replaceFragment(new HomePageFragment());

        // Thiết lập sự kiện cho BottomNavigationView
        bottom_nav_Home.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    replaceFragment(new HomePageFragment());
                    break;
                case R.id.action_voucher:
                    replaceFragment(new VoucherFragment());
                    break;
                case R.id.action_order:
                    replaceFragment(new OrderFragment());
                    break;
                case R.id.action_personal:
                    replaceFragment(new PersonalFragment());
                    break;
            }
            return true;
        });
        return view;
    }

    // Hàm thay thế fragment hiện tại bằng fragment mới
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_Home, fragment);
        fragmentTransaction.commit();
    }
}
