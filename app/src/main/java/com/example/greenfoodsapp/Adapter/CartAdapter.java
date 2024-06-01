package com.example.greenfoodsapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfoodsapp.Model.Cart;
import com.example.greenfoodsapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import android.util.Base64;
import java.util.List;

// Nguyễn Đức Huy - 20145449
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder> {
    private List<Cart> list;

    public CartAdapter(List<Cart> list) {
        this.list = list;
    }

    NumberFormat numberFormat = new DecimalFormat("#,##0");

    @NonNull
    @Override
    // Tạo viewHolder để hiển thị một mục (item) của giỏ hàng
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    // Gắn dữ liệu vào viewHolder
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Cart cart = list.get(position);
        byte[] imgByte = Base64.decode(cart.getImgProduct(),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        holder.img_ItemCart_imgProduct.setImageBitmap(bitmap);
        holder.tv_ItemCart_nameProduct.setText(String.valueOf(cart.getNameProduct()));
        holder.tv_ItemCart_priceProduct.setText("Giá: " + numberFormat.format(cart.getPriceProduct()) + " đ");
        holder.tvAmountProduct.setText(String.valueOf(cart.getNumberProduct()));
        holder.tvTotalProduct.setText("Tổng: " + numberFormat.format(cart.getNumberProduct() * cart.getPriceProduct()) + " đ");

        // Tăng số lượng sản phẩm khi nhấn nút "+"
        holder.imgPlus.setOnClickListener(view -> {
            int amount = Integer.parseInt(holder.tvAmountProduct.getText().toString()) + 1;
            holder.tvAmountProduct.setText(String.valueOf(amount));
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Cart");
            reference.child("" + cart.getIdCart()).child("numberProduct").setValue(amount);
            reference.child("" + cart.getIdCart()).child("totalPrice").setValue(amount*cart.getPriceProduct());
        });

        // Giảm số lượng sản phẩm khi nhấn nút "-"
        holder.imgMinus.setOnClickListener(view -> {
            int amount = Integer.parseInt(holder.tvAmountProduct.getText().toString()) - 1;
            if (amount == 0) {
                deleteProduct(String.valueOf(cart.getIdCart())); // Xóa sản phẩm nếu số lượng là 0
            } else {
                holder.tvAmountProduct.setText(String.valueOf(amount));
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Cart");
                reference.child("" + cart.getIdCart()).child("numberProduct").setValue(amount);
                reference.child("" + cart.getIdCart()).child("totalPrice").setValue(amount*cart.getPriceProduct());
            }
        });

        // Xóa sản phẩm khi nhấn nút xóa
        holder.imgDelete.setOnClickListener(view -> {
            deleteProduct(String.valueOf(cart.getIdCart()));
        });
    }

    @Override
    // Trả về số lượng sản phẩm trong giỏ hàng
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    // viewHolder chứa các view của item giỏ hàng
    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView tv_ItemCart_nameProduct, tv_ItemCart_priceProduct, tvAmountProduct, tvTotalProduct;
        private ImageView img_ItemCart_imgProduct, imgPlus, imgMinus, imgDelete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmountProduct = itemView.findViewById(R.id.tv_ItemCart_numberProduct);
            tvTotalProduct = itemView.findViewById(R.id.tv_ItemCart_totalPrice);
            tv_ItemCart_nameProduct = itemView.findViewById(R.id.tv_ItemCart_nameProduct);
            tv_ItemCart_priceProduct = itemView.findViewById(R.id.tv_ItemCart_priceProduct);
            img_ItemCart_imgProduct = itemView.findViewById(R.id.img_ItemCart_imgProduct);
            imgPlus = itemView.findViewById(R.id.btn_ItemCart_plus);
            imgMinus = itemView.findViewById(R.id.btn_ItemCart_minus);
            imgDelete = itemView.findViewById(R.id.btn_ItemCart_deleteProduct);
        }
    }

    // Hàm xóa sản phẩm khỏi giỏ hàng
    public void deleteProduct(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Cart");
        reference.child("" + id).removeValue();
    }
}
