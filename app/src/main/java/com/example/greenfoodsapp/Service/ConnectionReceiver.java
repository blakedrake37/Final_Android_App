package com.example.greenfoodsapp.Service;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.widget.AppCompatButton;

import com.example.greenfoodsapp.R;

// Ông Vũ Hữu Tài - 21110796
public class ConnectionReceiver extends BroadcastReceiver {

    // Hàm này được gọi khi có sự thay đổi về kết nối mạng
    @Override
    public void onReceive(Context context, Intent intent) {
        // Kiểm tra xem thiết bị có kết nối Internet hay không
        if (!Common.isConnectedToInternet(context)) {
            // Tạo một hộp thoại để thông báo khi không có kết nối Internet
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_disconnect);

            // Đặt nền của hộp thoại trong suốt
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            // Ngăn người dùng đóng hộp thoại bằng cách chạm vào bên ngoài
            dialog.setCanceledOnTouchOutside(false);

            // Lấy tham chiếu đến nút "Thử lại" trong hộp thoại
            AppCompatButton btnRetry = dialog.findViewById(R.id.btnRetry_dialogDisconnect_Retry);

            // Đặt sự kiện click cho nút "Thử lại"
            btnRetry.setOnClickListener(view -> {
                // Đóng hộp thoại và gọi lại hàm onReceive để kiểm tra kết nối lại
                dialog.dismiss();
                onReceive(context, intent);
            });
        }
    }
}
