package com.example.greenfoodsapp.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfoodsapp.Model.FavoviteModel;
import com.example.greenfoodsapp.Model.ProductTop;
import com.example.greenfoodsapp.R;

import java.util.ArrayList;
import java.util.List;
// Nguyễn Đức Huy - 20145449
public class FavoriteItemAdapter extends RecyclerView.Adapter<FavoriteItemAdapter.FavoriteItemViewHolder> {

    private List<FavoviteModel> favoviteModelList;
    private List<ProductTop> list = new ArrayList<>();

    // Constructor nhận danh sách các mục yêu thích
    public FavoriteItemAdapter(List<FavoviteModel> favoviteModelList) {
        this.favoviteModelList = favoviteModelList;
    }

    @NonNull
    @Override
    // Tạo viewHolder để hiển thị một mục (item) của danh sách yêu thích
    public FavoriteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item_favorite_home, parent, false);
        return new FavoriteItemViewHolder(view);
    }

    @Override
    // Gắn dữ liệu vào viewHolder
    public void onBindViewHolder(@NonNull FavoriteItemViewHolder holder, int position) {

        FavoviteModel model = favoviteModelList.get(position);
        holder.favoriteItem_tv.setText(model.getItemText());

        // Kiểm tra trạng thái ẩn/hiện của layout con
        boolean isHidden = model.isHidden();
        holder.hiddenLayout.setVisibility(isHidden ? View.VISIBLE : View.GONE);
        if (isHidden) {
            holder.arrowImage.setImageResource(R.drawable.ic_arrow_drop_up);
        } else {
            holder.arrowImage.setImageResource(R.drawable.ic_arrow_drop_down);
        }

        // Đặt màu nền cho các mục yêu thích dựa trên vị trí
        if (position == 0) {
            holder.cardView_favItem.setCardBackgroundColor(Color.parseColor("#33A338"));
        } else if (position == 1) {
            holder.cardView_favItem.setCardBackgroundColor(Color.parseColor("#2589D8"));
        } else if (position == 2) {
            holder.cardView_favItem.setCardBackgroundColor(Color.parseColor("#FA8F6E"));
        } else {
            holder.cardView_favItem.setCardBackgroundColor(Color.parseColor("#F16D64"));
        }

        // Cài đặt hành vi khi nhấn vào mục yêu thích
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setHidden(!model.isHidden());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    // Trả về số lượng mục yêu thích trong danh sách
    public int getItemCount() {
        return favoviteModelList.size();
    }

    // viewHolder chứa các view của item yêu thích
    public class FavoriteItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;
        private RelativeLayout hiddenLayout;
        private TextView favoriteItem_tv;
        private ImageView arrowImage;
        private RecyclerView child_favorite_rcView;
        private CardView cardView_favItem;

        public FavoriteItemViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            hiddenLayout = itemView.findViewById(R.id.hidden_layout);
            favoriteItem_tv = itemView.findViewById(R.id.itemTv);
            arrowImage = itemView.findViewById(R.id.arrow_imageview);
            child_favorite_rcView = itemView.findViewById(R.id.child_favorite_rcView);
            cardView_favItem = itemView.findViewById(R.id.cardView_favItem);
        }
    }
}
