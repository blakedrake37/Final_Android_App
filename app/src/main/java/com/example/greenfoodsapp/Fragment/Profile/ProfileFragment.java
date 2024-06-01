package com.example.greenfoodsapp.Fragment.Profile;

import static com.example.greenfoodsapp.Activity.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.greenfoodsapp.Activity.MainActivity;
import com.example.greenfoodsapp.Model.Partner;
import com.example.greenfoodsapp.Model.User;
import com.example.greenfoodsapp.R;
import com.example.greenfoodsapp.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

// Ông Vũ Hữu Tài - 21110796
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ProfileFragment";
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mStorageReference = mStorage.getReference();
    private User user;
    private Partner partner;

    private TextInputLayout mLayoutName, mLayoutEmail, mLayoutAddress, mLayoutPhoneNumber;
    private Button btnUpdateInfoUser;
    private ImageView ivAvatar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: start");
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        user = profileViewModel.getUser().getValue();
        partner = profileViewModel.getPartner().getValue();
        Log.d(TAG, "onCreate: " + user + "\n" + partner);
        Log.d(TAG, "onCreate: end");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initUI(); // Khởi tạo các thành phần giao diện
        Log.d(TAG, "onCreateView: end");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener(); // Khởi tạo các sự kiện listener
        if (user != null) showUserInformation(); // Hiển thị thông tin người dùng
        else if (partner != null) showPartnerInformation(); // Hiển thị thông tin đối tác
    }

    // Hiển thị thông tin đối tác
    private void showPartnerInformation() {
        Log.d(TAG, "showPartnerInformation: start");
        profileViewModel.getBitmapLiveData().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                Glide.with(requireActivity())
                        .load(profileViewModel.getBitmapLiveData().getValue())
                        .error(R.drawable.ic_avatar_default)
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .into(ivAvatar);
            }
        });
        profileViewModel.getPartner().observe(getViewLifecycleOwner(), new Observer<Partner>() {
            @Override
            public void onChanged(Partner partner) {
                try {
                    mLayoutName.getEditText().setText(partner.getNamePartner());
                    mLayoutAddress.getEditText().setText(partner.getAddressPartner());
                    mLayoutPhoneNumber.getEditText().setText(partner.getUserPartner());
                    byte[] decodeString = Base64.decode(partner.getImgPartner(), Base64.DEFAULT);
                    Glide.with(requireActivity()).load(decodeString)
                            .error(R.drawable.ic_avatar_default)
                            .signature(new ObjectKey(System.currentTimeMillis()))
                            .into(ivAvatar);
                    Log.d(TAG, "onChanged() returned: " + "ảnh được lấy từ storage về");
                    Log.d(TAG, "onChanged: " + partner.toString());
                } catch (Exception e) {
                    Log.e(TAG, "onChanged: ", e);
                }
            }
        });
        Log.d(TAG, "showPartnerInformation: end");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Khởi tạo các thành phần UI
    private void initUI() {
        ivAvatar = binding.getRoot().findViewById(R.id.iv_profile_fragment_avatar);
        btnUpdateInfoUser = binding.getRoot().findViewById(R.id.btn_profile_fragment_update);
        mLayoutName = binding.getRoot().findViewById(R.id.text_input_layout_profile_fragment_full_name);
        mLayoutAddress = binding.getRoot().findViewById(R.id.text_input_layout_profile_fragment_address);
        mLayoutPhoneNumber = binding.getRoot().findViewById(R.id.text_input_layout_profile_fragment_phone_number);
    }

    // Khởi tạo các listener cho các nút bấm
    private void initListener() {
        ivAvatar.setOnClickListener(this::onClick);
        btnUpdateInfoUser.setOnClickListener(this::onClick);
    }

    // Hiển thị thông tin người dùng
    public void showUserInformation() {
        Log.d(TAG, "showUserInformation: start");
        profileViewModel.getBitmapLiveData().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                Glide.with(requireActivity())
                        .load(profileViewModel.getBitmapLiveData().getValue())
                        .error(R.drawable.ic_avatar_default)
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .into(ivAvatar);
            }
        });
        profileViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mLayoutName.getEditText().setText(user.getName());
                mLayoutAddress.getEditText().setText(user.getAddress());
                mLayoutPhoneNumber.getEditText().setText(user.getPhoneNumber());
                if (user.getBitmapAvatar() != null) {
                    Glide.with(requireActivity())
                            .load(user.getBitmapAvatar())
                            .error(R.drawable.ic_avatar_default)
                            .into(ivAvatar);
                    user.setBitmapAvatar(null);
                    Log.d(TAG, "onChanged() returned: " + "ảnh được chọn từ thư mục ảnh");
                } else {
                    Glide.with(requireActivity())
                            .load(user.getStrUriAvatar())
                            .error(R.drawable.ic_avatar_default)
                            .signature(new ObjectKey(Long.toString(System.currentTimeMillis())))
                            .into(ivAvatar);
                    Log.d(TAG, "onChanged() returned: " + "ảnh được lấy từ storage về");
                }
                Log.d(TAG, "onChanged: " + user.toString());
            }
        });
        Log.d(TAG, "showUserInformation: end");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_profile_fragment_avatar:
                Log.d(TAG, "onClick: click imageview");
                onClickRequestPermission(); // Yêu cầu quyền truy cập bộ nhớ
                break;
            case R.id.btn_profile_fragment_update:
                Log.d(TAG, "onClick: click btn update");
                if(user != null) updateUserInfo(); // Cập nhật thông tin người dùng
                else if (partner != null) updatePartnerInfo(); // Cập nhật thông tin đối tác
                else Log.d(TAG, "onClick: không có đối tượng để update");
                break;
        }
    }

    // Cập nhật thông tin đối tác
    private void updatePartnerInfo() {
        Log.d(TAG, "updatePartnerInfo: start");
        partner.setNamePartner(mLayoutName.getEditText().getText().toString());
        partner.setAddressPartner(mLayoutAddress.getEditText().getText().toString());
        partner.setUserPartner(mLayoutPhoneNumber.getEditText().getText().toString());
        Bitmap bitmap = ((BitmapDrawable)ivAvatar.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imgByte = outputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String imgPartner = java.util.Base64.getEncoder().encodeToString(imgByte);
            partner.setImgPartner(imgPartner);
        }
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> userValue = partner.toMap();
        Map<String, Object> userUpdateValue = new HashMap<>();
        userUpdateValue.put("/Partner/" + partner.getIdPartner(), userValue);
        mDatabase.updateChildren(userUpdateValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: ");
                profileViewModel.setPartner(partner);
            }
        });
    }

    // Yêu cầu quyền truy cập bộ nhớ
    private void onClickRequestPermission() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null) {
            Log.d(TAG, "onClickRequestPermission: android 6.0");
            return;
        }
        // Kiểm tra phiên bản < android 6.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mainActivity.openGallery();
            Log.d(TAG, "onClickRequestPermission: android > 6.0");
            return;
        }
        // Kiểm tra quyền khi phiên bản >= android 6.0
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            mainActivity.openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    // Cập nhật thông tin người dùng
    private void updateUserInfo() {
        Log.d(TAG, "updateUserInfo: start");

        user.setName(mLayoutName.getEditText().getText().toString());
        user.setAddress(mLayoutAddress.getEditText().getText().toString());
        user.setPhoneNumber(mLayoutPhoneNumber.getEditText().getText().toString());
        Bitmap bitmap = ((BitmapDrawable) ivAvatar.getDrawable()).getBitmap();

        StorageReference spaceRef = mStorageReference.child("image/" + user.getId() + "_avatar.jpg");
        ivAvatar.setDrawingCacheEnabled(true);
        ivAvatar.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = spaceRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Xử lý khi upload thất bại
                Log.d(TAG, "onFailure: ");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Xử lý khi upload thành công
                spaceRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: " + uri);
                        user.setStrUriAvatar(uri.toString());
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> userValue = user.toMap();
                        Map<String, Object> userUpdateValue = new HashMap<>();
                        userUpdateValue.put("/User/" + user.getId(), userValue);
                        mDatabase.updateChildren(userUpdateValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                profileViewModel.setUser(user);
                            }
                        });
                    }
                });
            }
        });
    }
}
