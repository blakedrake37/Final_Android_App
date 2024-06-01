package com.example.greenfoodsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.greenfoodsapp.Model.Voucher;
import com.example.greenfoodsapp.R;

import java.util.List;
// Lê Nguyễn Toàn Tâm - 21110797
public class SpinerAdapter extends ArrayAdapter<Voucher> {
    private Context context;
    private List<Voucher> list;
    TextView name, id;

    // Constructor nhận context và danh sách voucher
    public SpinerAdapter(@NonNull Context context, List<Voucher> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    // Hàm getView để tạo view hiển thị cho spinner
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_voucher, null);
        }
        name = convertView.findViewById(R.id.tvCodeVoucher);
        id = convertView.findViewById(R.id.idVoucher);

        Voucher voucher = list.get(position);
        name.setText(String.valueOf(voucher.getCodeVoucher()));
        id.setText(String.valueOf(voucher.getIdVoucher()));

        return convertView;
    }

    @Override
    // Hàm getDropDownView để tạo view hiển thị cho dropdown của spinner
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_voucher, null);
        }
        name = convertView.findViewById(R.id.tvCodeVoucher);
        id = convertView.findViewById(R.id.idVoucher);

        Voucher voucher = list.get(position);
        name.setText(String.valueOf(voucher.getCodeVoucher()));
        id.setText(String.valueOf(voucher.getIdVoucher()));

        return convertView;
    }
}
