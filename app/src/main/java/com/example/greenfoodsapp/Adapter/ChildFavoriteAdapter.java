package com.example.greenfoodsapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfoodsapp.R;

import java.util.List;
// Nguyễn Đức Huy - 20145449
public class ChildFavoriteAdapter extends RecyclerView.Adapter<ChildFavoriteAdapter.ChildFavoriteHolder> {

    private List<String> topList;

    // Constructor nhận danh sách các sản phẩm yêu thích
    public ChildFavoriteAdapter(List<String> topList) {
        this.topList = topList;
    }

    @NonNull
    @Override
    // Tạo viewHolder để hiển thị một mục (item) của danh sách yêu thích
    public ChildFavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_favorite_list, parent, false);
        return new ChildFavoriteHolder(view);
    }

    @Override
    // Gắn dữ liệu vào viewHolder
    public void onBindViewHolder(@NonNull ChildFavoriteHolder holder, int position) {
        holder.name_top_product.setText(String.valueOf(topList.get(position)));
    }

    @Override
    // Trả về số lượng sản phẩm trong danh sách yêu thích
    public int getItemCount() {
        if (topList != null) {
            return topList.size();
        }
        return 0;
    }

    // viewHolder chứa các view của item yêu thích
    public class ChildFavoriteHolder extends RecyclerView.ViewHolder {
        private TextView name_top_product, amount_top_product;
        private ImageView img_top_product;

        public ChildFavoriteHolder(@NonNull View itemView) {
            super(itemView);
//            img_top_product = itemView.findViewById(R.id.img_top_product);
            name_top_product = itemView.findViewById(R.id.name_top_product);
            amount_top_product = itemView.findViewById(R.id.amount_top_product);
        }
    }
}
