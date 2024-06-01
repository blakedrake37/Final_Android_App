package com.example.greenfoodsapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfoodsapp.Model.Voucher;
import com.example.greenfoodsapp.R;

import java.util.Base64;
import java.util.List;
// Ông Vũ Hữu Tài - 21110796
public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {
    private List<Voucher> list;
    private Context context;
    private ItemClickListener itemClickListener;

    // Constructor nhận danh sách voucher, itemClickListener và context
    public VoucherAdapter(List<Voucher> list, ItemClickListener listener, Context context) {
        this.list = list;
        this.itemClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    // Tạo viewHolder để hiển thị một mục (item) của danh sách voucher
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    // Gắn dữ liệu vào viewHolder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences preferences = context.getSharedPreferences("My_User", Context.MODE_PRIVATE);
        String role = preferences.getString("role", "");

        Voucher voucher = list.get(position);
        byte[] imgByte = Base64.getDecoder().decode(voucher.getImgVoucher());
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        holder.item_imgVoucher.setImageBitmap(bitmap);
        holder.item_codeVoucher.setText(voucher.getCodeVoucher());

        // Thiết lập hành vi khi nhấn vào item voucher
        holder.item_cardView_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role.equals("admin")) {
                    if (holder.btn_delete_voucher.getVisibility() == View.GONE) {
                        holder.btn_delete_voucher.setVisibility(View.VISIBLE);
                    } else {
                        holder.btn_delete_voucher.setVisibility(View.GONE);
                    }
                }
            }
        });

        // Thiết lập hành vi khi nhấn nút xóa
        holder.btn_delete_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClickDeleteVoucher(voucher);
            }
        });
    }

    @Override
    // Trả về số lượng voucher trong danh sách
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    // viewHolder chứa các view của item voucher
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_codeVoucher;
        private ImageView item_imgVoucher;
        private CardView item_cardView_voucher;
        Button btn_delete_voucher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_codeVoucher = itemView.findViewById(R.id.item_codeVoucher);
            item_imgVoucher = itemView.findViewById(R.id.item_imgVoucher);
            item_cardView_voucher = itemView.findViewById(R.id.item_cardView_voucher);
            btn_delete_voucher = itemView.findViewById(R.id.btn_delete_voucher);
        }
    }

    // Interface để xử lý sự kiện khi nhấn nút xóa voucher
    public interface ItemClickListener {
        void onClickDeleteVoucher(Voucher voucher);
    }
}
