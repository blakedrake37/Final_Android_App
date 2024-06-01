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
// Ông Vũ Hữu Tài - 21110796
public class VoucherSpinnerAdapter extends ArrayAdapter<Voucher> {
    private Context context;
    private List<Voucher> arrayList;
    private TextView tvCodeVoucher, idVoucher;

    // Constructor nhận context và danh sách voucher
    public VoucherSpinnerAdapter(@NonNull Context context, List<Voucher> arrayList) {
        super(context, 0, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    // Hàm getView để tạo view hiển thị cho spinner
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_voucher, null);
        }
        tvCodeVoucher = convertView.findViewById(R.id.tvCodeVoucher);
        idVoucher = convertView.findViewById(R.id.idVoucher);

        Voucher voucher = arrayList.get(position);
        tvCodeVoucher.setText(String.valueOf(voucher.getCodeVoucher()));
        idVoucher.setText(String.valueOf(voucher.getIdVoucher()));
        return convertView;
    }

    @Override
    // Hàm getDropDownView để tạo view hiển thị cho dropdown của spinner
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_voucher, null);
        }
        tvCodeVoucher = convertView.findViewById(R.id.tvCodeVoucher);
        idVoucher = convertView.findViewById(R.id.idVoucher);

        Voucher voucher = arrayList.get(position);
        tvCodeVoucher.setText(String.valueOf(voucher.getCodeVoucher()));
        idVoucher.setText(String.valueOf(voucher.getIdVoucher()));
        return convertView;
    }
}
