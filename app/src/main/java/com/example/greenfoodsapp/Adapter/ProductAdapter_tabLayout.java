package com.example.greenfoodsapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.greenfoodsapp.Fragment.ProductFragments.FoodFragment;
import com.example.greenfoodsapp.Fragment.ProductFragments.FruitFragment;
import com.example.greenfoodsapp.Fragment.ProductFragments.MeatFragment;
import com.example.greenfoodsapp.Fragment.ProductFragments.VegetableFragment;
// Lê Nguyễn Toàn Tâm - 21110797
public class ProductAdapter_tabLayout extends FragmentStateAdapter {
    public ProductAdapter_tabLayout(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new VegetableFragment();
            case 1: return new FruitFragment();
            case 2: return new MeatFragment();
            default: return new FoodFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
