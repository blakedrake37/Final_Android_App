package com.example.greenfoodsapp.Fragment.ProductFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfoodsapp.Adapter.ProductAdapter;
import com.example.greenfoodsapp.Model.Product;
import com.example.greenfoodsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Lê Nguyễn Toàn Tâm - 21110797
public class MeatFragment extends Fragment {

    private List<Product> listMeat; // Danh sách sản phẩm thịt
    private RecyclerView rvMeat; // RecyclerView để hiển thị danh sách sản phẩm thịt
    private LinearLayoutManager linearLayoutManager; // LayoutManager cho RecyclerView
    private ProductAdapter adapter; // Adapter cho RecyclerView
    private View view; // View của Fragment
    private ProductFragment fragment = new ProductFragment(); // Fragment sản phẩm


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khởi tạo view và các UI components
        view = inflater.inflate(R.layout.fragment_meat, container, false);
        initUI();
        return view;
    }

    // Khởi tạo UI
    public void initUI() {
        listMeat = getVegetableProduct();
        rvMeat = view.findViewById(R.id.rvMeat);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvMeat.setLayoutManager(linearLayoutManager);
        adapter = new ProductAdapter(listMeat, fragment, getActivity());
        rvMeat.setAdapter(adapter);
        rvMeat.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    // Lấy danh sách sản phẩm thịt từ Firebase
    public List<Product> getVegetableProduct() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Vui lòng đợi ...");
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product");
        List<Product> list1 = new ArrayList<>();

        // Hiển thị ProgressDialog khi đang load dữ liệu từ Firebase
        progressDialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss(); // Tắt ProgressDialog khi load dữ liệu xong
                list1.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Product product = snap.getValue(Product.class);
                    if (product != null) {
                        if (product.getCodeCategory() == 3) { // Kiểm tra nếu sản phẩm thuộc danh mục thịt
                            list1.add(product); // Thêm sản phẩm vào danh sách
                        }
                    }
                }
                adapter.notifyDataSetChanged(); // Thông báo cho adapter biết dữ liệu đã thay đổi
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi truy xuất dữ liệu thất bại
            }
        });
        return list1;
    }
}
