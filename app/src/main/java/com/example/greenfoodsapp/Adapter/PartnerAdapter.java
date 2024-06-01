package com.example.greenfoodsapp.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfoodsapp.Fragment.PartnerFragment;
import com.example.greenfoodsapp.Model.Partner;
import com.example.greenfoodsapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
// Lê Nguyễn Toàn Tâm - 21110797
public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.viewHolder> {
    List<Partner> list;
    PartnerFragment fragment;

    // Constructor nhận danh sách đối tác và fragment
    public PartnerAdapter(List<Partner> list, PartnerFragment fragment) {
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    // Tạo viewHolder để hiển thị một mục (item) của danh sách đối tác
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner, parent, false);
        return new viewHolder(view);
    }

    @Override
    // Gắn dữ liệu vào viewHolder
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Partner partner = list.get(position);
        holder.tvCodePartner_adapter.setText("Mã ĐT: " + partner.getIdPartner());
        holder.tvNamePartner_adapter.setText("Tên ĐT: " + partner.getNamePartner());
        holder.tvAddressPartner_adapter.setText("Địa chỉ: " + partner.getAddressPartner());
        holder.tvUserPartner_adapter.setText("Tài khoản: " + partner.getUserPartner());
        holder.tvPasswordPartnre_adapter.setText("Mật khẩu: " + partner.getPasswordPartner());

        // Thiết lập hành vi khi nhấn vào mục đối tác để hiển thị hoặc ẩn nút xóa và cập nhật
        holder.itemPartner.setOnClickListener(view -> {
            if (holder.btn_UpdatePartner.getVisibility() == View.VISIBLE || holder.btn_DeletePartner.getVisibility() == View.VISIBLE) {
                holder.btn_DeletePartner.setVisibility(View.GONE);
                holder.btn_UpdatePartner.setVisibility(View.GONE);
            } else {
                holder.btn_DeletePartner.setVisibility(View.VISIBLE);
                holder.btn_UpdatePartner.setVisibility(View.VISIBLE);
            }
        });

        // Thiết lập hành vi khi nhấn nút xóa để hiển thị hộp thoại xác nhận xóa
        holder.btn_DeletePartner.setOnClickListener(view -> {
            showDialog(partner);
        });
    }

    @Override
    // Trả về số lượng đối tác trong danh sách
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    // viewHolder chứa các view của item đối tác
    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvCodePartner_adapter, tvNamePartner_adapter, tvAddressPartner_adapter,
                tvUserPartner_adapter, tvPasswordPartnre_adapter;
        private Button btn_UpdatePartner, btn_DeletePartner;
        private CardView itemPartner;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodePartner_adapter = itemView.findViewById(R.id.tv_CodePartner_item);
            tvNamePartner_adapter = itemView.findViewById(R.id.tv_NamePartner_item);
            tvAddressPartner_adapter = itemView.findViewById(R.id.tv_AddressPartner_item);
            tvUserPartner_adapter = itemView.findViewById(R.id.tv_UserPartner_item);
            tvPasswordPartnre_adapter = itemView.findViewById(R.id.tv_PasswordPartner_item);
            btn_DeletePartner = itemView.findViewById(R.id.btn_DeletePartner);
            btn_UpdatePartner = itemView.findViewById(R.id.btn_UpdatePartner);
            itemPartner = itemView.findViewById(R.id.itemPartner);
        }
    }

    // Hiển thị hộp thoại xác nhận xóa đối tác
    private void showDialog(Partner partner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setMessage("Bạn có chắc muốn xóa đối tác");
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePartner(partner);
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Không làm gì khi chọn "No"
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Hàm xóa đối tác khỏi cơ sở dữ liệu Firebase
    public void deletePartner(Partner partner) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Partner");
        reference.child("" + partner.getIdPartner()).removeValue();
    }
}
