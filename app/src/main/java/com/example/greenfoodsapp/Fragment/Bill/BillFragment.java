package com.example.greenfoodsapp.Fragment.Bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.greenfoodsapp.R;
import com.example.greenfoodsapp.databinding.FragmentBillBinding;
// Lê Nguyễn Toàn Tâm - 21110797
public class BillFragment extends Fragment {

    private FragmentBillBinding binding;

    // Hàm này được gọi để tạo và trả về View phân cấp liên kết với Fragment
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Sử dụng View Binding để inflate layout cho fragment
        binding = FragmentBillBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Tạo một OrderFragment mới và sử dụng FragmentTransaction để thay thế fragment hiện tại
        Fragment newFragment = new OrderFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_bill, newFragment);
        transaction.commit();

        return root;
    }

    // Hàm này được gọi khi view của fragment bị hủy
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Thiết lập binding về null để tránh rò rỉ bộ nhớ
        binding = null;
    }
}
