package com.example.greenfoodsapp.Fragment.BottomNav;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfoodsapp.Adapter.VoucherAdapter;
import com.example.greenfoodsapp.Model.Voucher;
import com.example.greenfoodsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

// Nguyễn Đức Huy - 20145449
public class VoucherFragment extends Fragment {

    private FloatingActionButton fab_addVoucher;
    private TextView tv_voucher_empty;
    private EditText code_voucher_dialog, double_code_voucher;
    private Button btnSave_voucher_dialog;
    private ImageView img_addImageCamera_voucher, imgVoucher_dialog, img_addImageDevice_voucher;
    private String codeVoucher, imgVoucher;
    private List<Voucher> voucherList;
    private VoucherAdapter voucherAdapter;
    private RecyclerView recyclerView_Voucher;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 10;
    private static final int PICK_IMAGE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_voucher, container, false);
        tv_voucher_empty = view.findViewById(R.id.tv_voucher_empty);
        fab_addVoucher = view.findViewById(R.id.fab_voucher);
        recyclerView_Voucher = view.findViewById(R.id.recyclerView_Voucher);
        initUI();

        SharedPreferences preferences = getContext().getSharedPreferences("My_User", Context.MODE_PRIVATE);
        String role = preferences.getString("role", "");
        if (role.equals("admin")) {
            fab_addVoucher.setVisibility(View.VISIBLE);
        }
        fab_addVoucher.setOnClickListener(view1 -> openDialog());
        return view;
    }

    // Khởi tạo giao diện người dùng
    public void initUI() {
        voucherList = new ArrayList<>();
        getAllVoucher();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_Voucher.setLayoutManager(linearLayoutManager);
        voucherAdapter = new VoucherAdapter(voucherList, this::onClickDelete, getContext());
        recyclerView_Voucher.setAdapter(voucherAdapter);
    }

    // Mở hộp thoại thêm voucher
    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm ưu đãi");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_voucher, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        code_voucher_dialog = dialog.findViewById(R.id.code_voucher_dialog);
        btnSave_voucher_dialog = dialog.findViewById(R.id.btnSave_voucher_dialog);
        img_addImageCamera_voucher = dialog.findViewById(R.id.img_addImageCamera_voucher);
        imgVoucher_dialog = dialog.findViewById(R.id.imgVoucher_dialog);
        img_addImageDevice_voucher = dialog.findViewById(R.id.img_addImageDevice_voucher);

        img_addImageCamera_voucher.setOnClickListener(view1 -> requestPermissionCamera());
        img_addImageDevice_voucher.setOnClickListener(view1 -> requestPermissionDevice());
        btnSave_voucher_dialog.setOnClickListener(view1 -> {
            getValueVoucher();
            validate();
        });
    }

    // Lấy tất cả voucher từ Firebase
    public void getAllVoucher() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Voucher");

        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Vui lòng đợi ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                voucherList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Voucher voucher = snap.getValue(Voucher.class);
                    voucherList.add(voucher);
                }

                if (voucherList.size() <= 0) {
                    tv_voucher_empty.setVisibility(View.VISIBLE);
                    recyclerView_Voucher.setVisibility(View.INVISIBLE);
                } else {
                    tv_voucher_empty.setVisibility(View.INVISIBLE);
                    recyclerView_Voucher.setVisibility(View.VISIBLE);
                }

                voucherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    // Lấy giá trị của voucher
    public void getValueVoucher() {
        try {
            Bitmap bitmap = ((BitmapDrawable) imgVoucher_dialog.getDrawable()).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imgByte = outputStream.toByteArray();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imgVoucher = Base64.getEncoder().encodeToString(imgByte);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        codeVoucher = code_voucher_dialog.getText().toString();
    }

    // Thiết lập dữ liệu voucher và thêm vào Firebase
    public void setDataVoucher() {
        Voucher voucher = new Voucher();
        voucher.setImgVoucher(imgVoucher);
        voucher.setCodeVoucher(codeVoucher);
        addVoucher(voucher);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.imgVoucher_dialog.setImageBitmap(bp);

                Uri imageUri = data.getData();
                imgVoucher_dialog.setImageURI(imageUri);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Bạn chưa thêm ảnh", Toast.LENGTH_LONG).show();
            } else if (data != null) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                this.imgVoucher_dialog.setImageURI(imageUri);
            }
        }
    }

    // Thêm voucher vào Firebase
    private void addVoucher(Voucher voucher) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Voucher");
        if (voucherList.size() == 0) {
            voucher.setIdVoucher(1);
            reference.child("1").setValue(voucher);
        } else {
            int i = voucherList.size() - 1;
            int id = voucherList.get(i).getIdVoucher() + 1;
            voucher.setIdVoucher(id);
            reference.child("" + id).setValue(voucher);
        }
    }

    // Kiểm tra các trường dữ liệu có rỗng hay không
    public boolean isEmptys(String str, EditText edt) {
        if (str.isEmpty()) {
            edt.setError("Không được để trống");
            return false;
        } else edt.setError(null);
        return true;
    }

    // Kiểm tra ảnh có rỗng hay không
    public boolean errorImg(String str) {
        if (str != null) {
            return true;
        } else {
            Toast.makeText(getContext(), "Ảnh không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Xác thực dữ liệu trước khi thêm voucher
    public void validate() {
        if (isEmptys(codeVoucher, code_voucher_dialog) && errorImg(imgVoucher)) {
            setDataVoucher();
            removeAll();
        }
    }

    // Xóa dữ liệu trong các trường nhập
    private void removeAll() {
        code_voucher_dialog.setText(null);
        imgVoucher_dialog.setImageResource(R.drawable.ic_menu_camera1);
    }

    // Yêu cầu quyền truy cập camera
    public void requestPermissionCamera() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                captureImage();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                requestPermissionCamera();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Nếu bạn không cấp quyền,bạn sẽ không thể tải ảnh lên\n\nVui lòng vào [Cài đặt] > [Quyền] và cấp quyền để sử dụng")
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }

    // Yêu cầu quyền truy cập thư viện ảnh
    public void requestPermissionDevice() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                requestPermissionDevice();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Nếu bạn không cấp quyền,bạn sẽ không thể tải ảnh lên\n\nVui lòng vào [Cài đặt] > [Quyền] và cấp quyền để sử dụng")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    // Mở camera để chụp ảnh
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    // Mở thư viện ảnh
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    // Xóa voucher
    private void onClickDelete(Voucher voucher) {
        new AlertDialog.Builder(getActivity()).setTitle("Xóa Voucher")
                .setMessage("Bạn có chắc chắn muốn xóa Voucher?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Voucher");

                        reference.child(String.valueOf(voucher.getIdVoucher())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                        voucherAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
