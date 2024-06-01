package com.example.greenfoodsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

import com.example.greenfoodsapp.Adapter.CartAdapter;
import com.example.greenfoodsapp.Model.Bill;
import com.example.greenfoodsapp.Model.Cart;
import com.example.greenfoodsapp.Model.ProductTop;
import com.example.greenfoodsapp.Model.Voucher;
import com.example.greenfoodsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Nguyễn Đức Huy - 20145449
public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCart;
    private List<Cart> list;
    private List<ProductTop> listTop = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private CartAdapter adapter;
    private TextView tvTotalPrice, tvEmptyProduct,tv1, tvHide1, tvHide2, tvVoucher;
    private Button btn_senBill, btnEmptyProduct;
    private List<Bill> listBill;
    private Spinner spinner;
    private List<Voucher> listVoucher = new ArrayList<>();
    private String[] arr = {"Không có ưu đãi","Giảm 50%","Giảm 30%", "Giảm 20%"};
    private NumberFormat numberFormat = new DecimalFormat("#,##0");
    private String voucher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#73DE4E")));
        getSupportActionBar().setTitle("Giỏ hàng");
        unitUi();

        // Đặt sự kiện click cho nút gửi hóa đơn
        btn_senBill.setOnClickListener(view -> {
            addBill(); // Thêm hóa đơn
            for (int i = 0; i < list.size(); i++) {
                addProductTop(list.get(i).getIdProduct(),list.get(i).getNumberProduct(),list.get(i).getIdCategory()); // Cập nhật sản phẩm lên top
            }
            deleteCart(); // Xóa giỏ hàng
        });
    }

    // Khởi tạo giao diện người dùng
    public void unitUi(){
        getVoucher();
        getProductTop();
        spinner = findViewById(R.id.spinner_voucher_cart);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,arr);
        spinner.setAdapter(adapter1);
        voucher = spinner.getSelectedItem().toString();

        // Đặt sự kiện chọn item cho spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                voucher = spinner.getSelectedItem().toString();
                getCartProduct(); // Lấy danh sách sản phẩm trong giỏ hàng
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        rvCart = findViewById(R.id.recyclerView_CartActivity_listCart);
        list = getCartProduct();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCart.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(list);
        rvCart.setAdapter(adapter);
        tvTotalPrice = findViewById(R.id.tv_CartActivity_totalPrice);
        tv1 = findViewById(R.id.tv1_CartActivity_totalPrice);
        btn_senBill = findViewById(R.id.btn_CartActivity_btnPay);
        tvEmptyProduct = findViewById(R.id.tv_CartActivity_emptyProduct);
        btnEmptyProduct = findViewById(R.id.btn_CartActivity_emptyProduct);
        listBill = getAllBill();
        tvHide1 = findViewById(R.id.tvHide1);
        tvHide2 = findViewById(R.id.tvHide2);
        tvVoucher = findViewById(R.id.tvVoucher);
    }

    // Lấy danh sách voucher từ Firebase
    public void getVoucher(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Voucher");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listVoucher.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Voucher voucher = snap.getValue(Voucher.class);
                    listVoucher.add(voucher);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Lấy danh sách sản phẩm trong giỏ hàng từ Firebase
    public List<Cart> getCartProduct(){
        SharedPreferences preferences = getSharedPreferences("My_User",MODE_PRIVATE);
        String user = preferences.getString("username","");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Cart");
        List<Cart> list1 = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Cart cart = snap.getValue(Cart.class);
                    if(cart!=null && cart.getUserClient().equals(user)){
                        list1.add(cart);
                    }
                }
                int sum = 0;
                for (Cart cart : list1) {
                    sum += cart.getTotalPrice();
                }
                // Áp dụng voucher giảm giá
                if (voucher.equals("Giảm 50%")){
                    sum = (int) (sum - sum * 0.5);
                }else if (voucher.equals("Giảm 30%")){
                    sum = (int) (sum - sum * 0.3);
                }else if (voucher.equals("Giảm 20%")){
                    sum = (int) (sum - sum * 0.2);
                }

                tvTotalPrice.setText(numberFormat.format(sum));
                tv1.setText("" + sum);

                if(list1.isEmpty()){
                    // Hiển thị thông báo khi giỏ hàng trống
                    tvHide1.setVisibility(View.GONE);
                    tvHide2.setVisibility(View.GONE);
                    btn_senBill.setVisibility(View.GONE);
                    tvTotalPrice.setVisibility(View.GONE);
                    tvEmptyProduct.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    btnEmptyProduct.setVisibility(View.VISIBLE);
                    tvVoucher.setVisibility(View.GONE);
                    btnEmptyProduct.setOnClickListener(view -> {
                        startActivity(new Intent(CartActivity.this, MainActivity.class));
                        finish();
                    });
                    rvCart.setVisibility(View.INVISIBLE);
                }else {
                    // Hiển thị giỏ hàng
                    tvHide1.setVisibility(View.VISIBLE);
                    tvHide2.setVisibility(View.VISIBLE);
                    tvVoucher.setVisibility(View.VISIBLE);
                    btn_senBill.setVisibility(View.VISIBLE);
                    tvEmptyProduct.setVisibility(View.INVISIBLE);
                    btnEmptyProduct.setVisibility(View.INVISIBLE);
                    rvCart.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return list1;
    }

    // Thêm hóa đơn vào Firebase
    public void addBill(){
        Bill bill = new Bill();
        SharedPreferences preferences = getSharedPreferences("My_User",MODE_PRIVATE);
        String user = preferences.getString("username","");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Bill");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("k:mm");
        String time = timeFormat.format(Calendar.getInstance().getTime());

        if (listBill.isEmpty()){
            bill.setIdBill(1);
        } else {
            int id = listBill.get(listBill.size() - 1).getIdBill() + 1;
            bill.setIdBill(id);
        }

        bill.setIdClient(user);
        bill.setDayOut(date);
        bill.setTimeOut(time);
        bill.setIdPartner(list.get(0).getIdPartner());
        bill.setTotal(Integer.parseInt(tv1.getText().toString()));
        bill.setStatus(user.equals("admin") ? "Yes" : "No");
        reference.child("" + bill.getIdBill()).setValue(bill);
        reference.child("" + bill.getIdBill()).child("Cart").setValue(list);
    }

    // Xóa giỏ hàng sau khi gửi hóa đơn
    public void deleteCart(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Cart");
        for (Cart cart : list) {
            reference.child("" + cart.getIdCart()).removeValue();
        }
        btn_senBill.setEnabled(!list.isEmpty());
    }

    // Lấy danh sách hóa đơn từ Firebase
    public List<Bill> getAllBill(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Bill");
        List<Bill> list1 = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Bill bill = snap.getValue(Bill.class);
                    list1.add(bill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return list1;
    }

    // Thêm sản phẩm vào danh sách sản phẩm bán chạy
    public void addProductTop(int id, int amount, int category){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("ProductTop");
        ProductTop top = new ProductTop();
        top.setIdProduct(id);
        top.setIdCategory(category);
        top.setAmountProduct(amount);

        if (listTop.isEmpty()){
            reference.child("" + id).setValue(top);
        } else {
            for (ProductTop productTop : listTop) {
                if (id == productTop.getIdProduct()) {
                    int newAmount = productTop.getAmountProduct() + amount;
                    reference.child("" + id).child("amountProduct").setValue(newAmount);
                    return;
                }
            }
            reference.child("" + id).setValue(top);
        }
    }

    // Lấy danh sách sản phẩm bán chạy từ Firebase
    public void getProductTop(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("ProductTop");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTop.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    ProductTop top = snapshot1.getValue(ProductTop.class);
                    listTop.add(top);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
