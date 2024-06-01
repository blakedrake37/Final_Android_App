package com.example.greenfoodsapp.Service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// Ông Vũ Hữu Tài - 21110796
public class Common {

    // Hàm kiểm tra kết nối Internet
    public static boolean isConnectedToInternet(Context context) {
        // Lấy dịch vụ quản lý kết nối từ hệ thống
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Nếu không lấy được dịch vụ quản lý kết nối, trả về false
        if (connectivityManager != null) {
            // Lấy thông tin tất cả các mạng
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            // Nếu có thông tin về mạng
            if (info != null) {
                // Kiểm tra từng mạng
                for (int i = 0; i < info.length; i++) {
                    // Nếu mạng đang kết nối, trả về true
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }

        // Nếu không có mạng nào đang kết nối, trả về false
        return false;
    }
}
