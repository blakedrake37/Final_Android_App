package com.example.greenfoodsapp.Fragment.Profile;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenfoodsapp.Model.Partner;
import com.example.greenfoodsapp.Model.User;

// Ông Vũ Hữu Tài - 21110796
public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<User> user;
    private final MutableLiveData<Partner> partner;
    private final MutableLiveData<Bitmap> bitmapImageAvatar;

    // Khởi tạo các đối tượng LiveData
    public ProfileViewModel() {
        user = new MutableLiveData<>();
        partner = new MutableLiveData<>();
        bitmapImageAvatar = new MutableLiveData<>();
    }

    // Đặt giá trị cho bitmapImageAvatar
    public void setBitmapImageAvatar(Bitmap bitmap) {
        this.bitmapImageAvatar.setValue(bitmap);
    }

    // Lấy giá trị của bitmapImageAvatar
    public LiveData<Bitmap> getBitmapLiveData() {
        return this.bitmapImageAvatar;
    }

    // Đặt giá trị cho user
    public void setUser(User user) {
        this.user.setValue(user);
    }

    // Lấy giá trị của user
    public LiveData<User> getUser() {
        return user;
    }

    // Đặt giá trị cho partner
    public void setPartner(Partner partner) {
        this.partner.setValue(partner);
    }

    // Lấy giá trị của partner
    public LiveData<Partner> getPartner(){
        return this.partner;
    }

    @Override
    public String toString() {
        return "ProfileViewModel{" +
                "user=" + user +
                ", partner=" + partner +
                '}';
    }
}
