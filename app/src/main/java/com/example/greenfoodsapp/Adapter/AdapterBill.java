package com.example.greenfoodsapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfoodsapp.Model.Bill;
import com.example.greenfoodsapp.Model.Cart;
import com.example.greenfoodsapp.Model.User;
import com.example.greenfoodsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
// Nguyễn Đức Huy - 20145449
public class AdapterBill extends RecyclerView.Adapter<AdapterBill.viewHolder> {
    private List<Bill> list;
    private Context context;
    private AdapterItemBill adapterItemBill;
    private LinearLayoutManager linearLayoutManager;

    // Constructor nhận danh sách hóa đơn và context
    public AdapterBill(List<Bill> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    // Tạo viewHolder để hiển thị một mục (item) của danh sách hóa đơn
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new viewHolder(view);
    }

    @Override
    // Gắn dữ liệu vào viewHolder
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SharedPreferences preferences = context.getSharedPreferences("My_User", Context.MODE_PRIVATE);
        String role = preferences.getString("role", "");
        Bill bill = list.get(position);
        List<Cart> listCart = getAllCart(position);
        getUser(bill.getIdClient(), holder.tvNameClient);
        adapterItemBill = new AdapterItemBill(listCart);
        linearLayoutManager = new LinearLayoutManager(context);
        holder.rvItemOrder.setLayoutManager(linearLayoutManager);
        holder.rvItemOrder.setAdapter(adapterItemBill);
        holder.tvidBill.setText("Mã HD: " + bill.getIdBill());
        holder.tvPhone.setText("Số điện thoại: " + bill.getIdClient());
        holder.tvTime.setText("Thời gian: " + bill.getTimeOut());
        holder.tvDay.setText(String.valueOf(bill.getDayOut()));
        holder.tvTotal.setText(String.valueOf(bill.getTotal()));

        // Thiết lập hành vi khi nhấn vào layout để hiển thị hoặc ẩn danh sách sản phẩm trong hóa đơn
        holder.linearLayout_item_product.setOnClickListener(view -> {
            if (holder.rvItemOrder.getVisibility() == View.GONE) {
                holder.rvItemOrder.setVisibility(View.VISIBLE);
                holder.img_drop_up.setImageResource(R.drawable.ic_arrow_drop_down);
            } else {
                holder.rvItemOrder.setVisibility(View.GONE);
                holder.img_drop_up.setImageResource(R.drawable.ic_arrow_drop_up);
            }
        });

        // Thiết lập hành vi khi nhấn vào card của hóa đơn để hiển thị hoặc ẩn nút cập nhật trạng thái (dành cho admin và partner)
        holder.card_bill.setOnClickListener(view -> {
            if (!role.equals("user")) {
                if (holder.btn_updateStatusBill.getVisibility() == View.GONE) {
                    holder.btn_updateStatusBill.setVisibility(View.VISIBLE);
                } else {
                    holder.btn_updateStatusBill.setVisibility(View.GONE);
                }
            }
            if (bill.getStatus().equals("Yes")) {
                holder.btn_updateStatusBill.setVisibility(View.GONE);
            }
        });

        // Thiết lập hành vi khi nhấn nút cập nhật trạng thái hóa đơn
        holder.btn_updateStatusBill.setOnClickListener(view -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Bill");
            reference.child(bill.getIdBill() + "/status").setValue("Yes");
        });
    }

    @Override
    // Trả về số lượng hóa đơn trong danh sách
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    // viewHolder chứa các view của item hóa đơn
    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvidBill, tvNameClient, tvTotal, tvTime, tvDay, tvPhone;
        private LinearLayout linearLayout_item_product;
        private ImageView img_drop_up;
        private Button btn_updateStatusBill;
        private RecyclerView rvItemOrder;
        private CardView card_bill;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvidBill = itemView.findViewById(R.id.tv_idBill_item);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvTotal = itemView.findViewById(R.id.tv_totalOrder_item);
            linearLayout_item_product = itemView.findViewById(R.id.linear_layout_item_product);
            img_drop_up = itemView.findViewById(R.id.img_drop_up);
            btn_updateStatusBill = itemView.findViewById(R.id.btn_updateStatusBill_item);
            rvItemOrder = itemView.findViewById(R.id.rv_order);
            card_bill = itemView.findViewById(R.id.card_bill);
            tvTime = itemView.findViewById(R.id.tv_time_item);
            tvDay = itemView.findViewById(R.id.tv_day_item);
            tvNameClient = itemView.findViewById(R.id.tv_name_client_item);
        }
    }

    // Hàm lấy danh sách giỏ hàng từ cơ sở dữ liệu Firebase cho một hóa đơn cụ thể
    private List<Cart> getAllCart(int i) {
        List<Cart> list1 = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Bill");
        reference.child("" + list.get(i).getIdBill()).child("Cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Cart cart = snap.getValue(Cart.class);
                    list1.add(cart);
                }
                adapterItemBill.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi hủy lấy dữ liệu từ Firebase
            }
        });
        return list1;
    }

    // Hàm lấy thông tin người dùng từ cơ sở dữ liệu Firebase
    private void getUser(String userId, TextView tvName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    if (userId.equals(user.getId())) {
                        tvName.setText("Tên khách hàng: " + user.getName());
                    }
                }
                adapterItemBill.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi hủy lấy dữ liệu từ Firebase
            }
        });
    }
}
