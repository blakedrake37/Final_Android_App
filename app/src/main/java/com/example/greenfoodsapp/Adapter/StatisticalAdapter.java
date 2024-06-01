package com.example.greenfoodsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfoodsapp.Model.Bill;
import com.example.greenfoodsapp.Model.Cart;
import com.example.greenfoodsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
// Nguyễn Đức Huy - 20145449
public class StatisticalAdapter extends RecyclerView.Adapter<StatisticalAdapter.ViewHolder> {
    private List<Bill> list;
    private Context context;
    List<Cart> listCart;
    AdapterItemBill adapterItemBill;
    LinearLayoutManager linearLayoutManager;

    // Constructor nhận danh sách hóa đơn và context
    public StatisticalAdapter(List<Bill> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    // Tạo viewHolder để hiển thị một mục (item) của danh sách hóa đơn
    public StatisticalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistical, parent, false);
        return new StatisticalAdapter.ViewHolder(view);
    }

    @Override
    // Gắn dữ liệu vào viewHolder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = list.get(position);
        listCart = getAllCart(position);
        adapterItemBill = new AdapterItemBill(listCart);
        linearLayoutManager = new LinearLayoutManager(context);
        holder.rvItemOrder.setLayoutManager(linearLayoutManager);
        holder.rvItemOrder.setAdapter(adapterItemBill);
        holder.tvidBill.setText("Mã HD: " + bill.getIdBill());
        holder.tvTimeOut.setText("Thời gian: " + bill.getTimeOut());
        holder.tvDayOut.setText(String.valueOf(bill.getDayOut()));
        NumberFormat numberFormat = new DecimalFormat("#,##0");
        holder.tvTotal.setText(numberFormat.format(bill.getTotal()) + " đ");

        // Thiết lập hành vi khi nhấn vào layout để hiển thị hoặc ẩn danh sách sản phẩm trong hóa đơn
        holder.linearLayout_itemProducts.setOnClickListener(view -> {
            if (holder.rvItemOrder.getVisibility() == View.GONE) {
                holder.rvItemOrder.setVisibility(View.VISIBLE);
                holder.img_dropDown.setImageResource(R.drawable.ic_arrow_drop_up);
            } else {
                holder.rvItemOrder.setVisibility(View.GONE);
                holder.img_dropDown.setImageResource(R.drawable.ic_arrow_drop_down);
            }
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvidBill, tvTotal, tvDayOut, tvTimeOut;
        private LinearLayout linearLayout_itemProducts;
        private ImageView img_dropDown;
        private RecyclerView rvItemOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvidBill = itemView.findViewById(R.id.tv_ItemStatistical_idBill);
            tvTotal = itemView.findViewById(R.id.tv_ItemStatistical_totalOrder);
            tvDayOut = itemView.findViewById(R.id.tv_ItemStatistical_dayOut);
            tvTimeOut = itemView.findViewById(R.id.tv_ItemStatistical_timeOut);
            linearLayout_itemProducts = itemView.findViewById(R.id.linearLayout_ItemStatistical_itemProducts);
            img_dropDown = itemView.findViewById(R.id.img_ItemStatistical_dropDown);
            rvItemOrder = itemView.findViewById(R.id.recyclerView_ItemStatistical_products);
        }
    }

    // Hàm lấy danh sách giỏ hàng từ cơ sở dữ liệu Firebase cho một hóa đơn cụ thể
    private List<Cart> getAllCart(int i) {
        List<Cart> listCart = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Bill");
        reference.child("" + list.get(i).getIdBill()).child("Cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCart.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Cart cart = snap.getValue(Cart.class);
                    listCart.add(cart);
                }
                adapterItemBill.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi hủy lấy dữ liệu từ Firebase
            }
        });
        return listCart;
    }
}
