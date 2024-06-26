package com.example.greenfoodsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.greenfoodsapp.Adapter.ProductAdapter;
import com.example.greenfoodsapp.Fragment.ProductFragments.ProductFragment;
import com.example.greenfoodsapp.Model.Product;
import com.example.greenfoodsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Nguyễn Đức Huy - 20145449
public class SearchActivity extends AppCompatActivity {
    private List<Product> listProduct = new ArrayList<>();
    private RecyclerView rvProduct;
    private LinearLayoutManager linearLayoutManager;
    private ProductAdapter adapter;
    private ProductFragment fragment = new ProductFragment();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Tìm kiếm sản phẩm");
        initUI();
    }

    // Hàm khởi tạo giao diện người dùng
    public void initUI() {
        searchView = findViewById(R.id.searchProduct);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // Xử lý khi nội dung tìm kiếm thay đổi
            @Override
            public boolean onQueryTextChange(String newText) {
                searchProduct(newText);
                return true;
            }
        });
        getVegetableProduct();
        rvProduct = findViewById(R.id.rvSearch);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvProduct.setLayoutManager(linearLayoutManager);
        adapter = new ProductAdapter(listProduct, fragment, getApplicationContext());
        rvProduct.setAdapter(adapter);
        rvProduct.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }

    // Hàm tìm kiếm sản phẩm dựa trên từ khóa
    private void searchProduct(String str) {
        List<Product> searchList = new ArrayList<>();
        for (Product product : listProduct) {
            if (product.getNameProduct().toLowerCase().contains(str.toLowerCase())) {
                searchList.add(product);
            }
        }
        if (searchList.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Không có sản phẩm", Toast.LENGTH_SHORT).show();
        }
        adapter = new ProductAdapter(searchList, fragment, getApplicationContext());
        rvProduct.setAdapter(adapter);

        if (str.equals("")) {
            adapter = new ProductAdapter(listProduct, fragment, getApplicationContext());
            rvProduct.setAdapter(adapter);
        }
    }

    // Hàm lấy danh sách sản phẩm từ Firebase
    public void getVegetableProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Product product = snap.getValue(Product.class);
                    listProduct.add(product);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
