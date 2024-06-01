package com.example.greenfoodsapp.Fragment.ProductFragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.greenfoodsapp.Adapter.ProductAdapter_tabLayout;
import com.example.greenfoodsapp.Model.Product;
import com.example.greenfoodsapp.R;
import com.example.greenfoodsapp.databinding.FragmentProductBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import android.util.Base64;
import java.util.List;

// Lê Nguyễn Toàn Tâm - 21110797
public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;
    private TabLayout tabLayoutProduct;
    private ViewPager2 viewPagerProduct;
    private ProductAdapter_tabLayout adapter_tabLayout;
    private FloatingActionButton fab_addProduct;
    private List<Product> listProduct = new ArrayList<>();
    private TextInputLayout til_nameProduct, til_priceProduct;
    private TextView tvErrorImg;
    private ImageView img_Product, img_addImageCamera, img_addImageDevice;
    private String nameProduct, imgProduct, userPartner, priceProduct, role;
    private int codeCategory;
    private Button btn_addVegetable, btn_cancleVegetable;
    private Spinner sp_nameCategory;
    private String[] arr = {"Rau củ", "Hoa quả", "Thịt"};
    private ArrayAdapter<String> adapterSpiner;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        tabLayout();
        initUI();
        fab_addProduct.setOnClickListener(view1 -> {
            dialogProduct(new Product(), 0, getContext());
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Thiết lập tab layout cho sản phẩm
    public void tabLayout() {
        tabLayoutProduct = binding.tabProductFragment;
        viewPagerProduct = binding.viewPagerProductFragment;
        adapter_tabLayout = new ProductAdapter_tabLayout(this);
        viewPagerProduct.setAdapter(adapter_tabLayout);
        new TabLayoutMediator(tabLayoutProduct, viewPagerProduct, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Rau củ");
                        break;
                    case 1:
                        tab.setText("Hoa quả");
                        break;
                    case 2:
                        tab.setText("Thịt");
                        break;
                    case 3:
                        tab.setText("Đồ ăn");
                        break;
                }
            }
        }).attach();
    }

    // Khởi tạo các thành phần UI
    public void initUI() {
        getAllProducts();
        fab_addProduct = binding.fabAddProductFragment;
    }

    // Hiển thị dialog thêm sản phẩm
    public void dialogProduct(Product product, int type, Context context) {
        sharedPreferences = context.getSharedPreferences("My_User", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm sản phẩm");
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_product, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        initUiDialog(view, context);
        if (type == 0) {
            img_addImageCamera.setOnClickListener(view1 -> {
                requestPermissionCamera();
            });
            img_addImageDevice.setOnClickListener(view1 -> {
                requestPermissionDevice();
            });
            btn_addVegetable.setOnClickListener(view1 -> {
                getData(context);
                validate(0);
            });
            btn_cancleVegetable.setOnClickListener(view1 -> {
                alertDialog.dismiss();
            });
        }
        if (type == 1) {
            setData(product);
            img_addImageCamera.setVisibility(View.GONE);
            img_addImageDevice.setVisibility(View.GONE);
            btn_addVegetable.setOnClickListener(view1 -> {
                getData(context);
                if (validate(1) == 1) {
                    product.setNameProduct(nameProduct);
                    product.setPriceProduct(Integer.parseInt(priceProduct));
                    product.setCodeCategory(codeCategory);
                    updateProduct(product);
                }
                alertDialog.dismiss();
            });
            btn_cancleVegetable.setOnClickListener(view1 -> {
                alertDialog.dismiss();
            });
        }
    }

    // Khởi tạo các thành phần UI trong dialog
    public void initUiDialog(View view, Context context) {
        tvErrorImg = view.findViewById(R.id.error_img);
        img_Product = view.findViewById(R.id.imgProduct_dialog);
        img_addImageCamera = view.findViewById(R.id.img_addImageCamera_dialog);
        img_addImageDevice = view.findViewById(R.id.img_addImageDevice_dialog);
        til_nameProduct = view.findViewById(R.id.til_NameProduct_dialog);
        til_priceProduct = view.findViewById(R.id.til_PriceProduct_dialog);
        btn_addVegetable = view.findViewById(R.id.btn_addVegetable_dialog);
        btn_cancleVegetable = view.findViewById(R.id.btn_cancleVegetable_dialog);
        sp_nameCategory = view.findViewById(R.id.sp_nameCategory);
        adapterSpiner = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, arr);
        sp_nameCategory.setAdapter(adapterSpiner);
        if (!role.equals("admin")) {
            sp_nameCategory.setVisibility(View.GONE);
        }
    }

    // Thiết lập dữ liệu cho sản phẩm
    public void setData(Product product) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            byte[] imgByte = Base64.decode(product.getImgProduct(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            img_Product.setImageBitmap(bitmap);
            til_nameProduct.getEditText().setText(product.getNameProduct());
            til_priceProduct.getEditText().setText(String.valueOf(product.getPriceProduct()));
        }
    }

    // Lấy dữ liệu từ các thành phần UI trong dialog
    public void getData(Context context) {
        nameProduct = til_nameProduct.getEditText().getText().toString();
        try {
            Bitmap bitmap = ((BitmapDrawable) img_Product.getDrawable()).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imgByte = outputStream.toByteArray();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imgProduct = Base64.encodeToString(imgByte, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("My_User", Context.MODE_PRIVATE);
        userPartner = sharedPreferences.getString("username", "");
        String category = sp_nameCategory.getSelectedItem().toString();
        if (category.equals("Rau củ")) {
            codeCategory = 1;
        } else if (category.equals("Hoa quả")) {
            codeCategory = 2;
        } else if (category.equals("Thịt")) {
            codeCategory = 3;
        }
        priceProduct = til_priceProduct.getEditText().getText().toString();
    }

    // Thiết lập dữ liệu cho sản phẩm mới
    public void setDataProduct() {
        Product product = new Product();
        product.setUserPartner(userPartner);
        product.setCodeCategory(codeCategory);
        product.setNameProduct(nameProduct);
        product.setPriceProduct(Integer.parseInt(priceProduct));
        product.setImgProduct(imgProduct);
        addProduct(product);
    }

    // Chụp ảnh sản phẩm
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, 10);
    }

    // Mở thư viện ảnh
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        this.startActivityForResult(gallery, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.img_Product.setImageBitmap(bp);
                Uri imageUri = data.getData();
                img_Product.setImageURI(imageUri);
            } else if (resultCode == RESULT_CANCELED) {
                // Xử lý khi không chụp ảnh
            } else if (data != null) {
                // Xử lý khi có dữ liệu khác
            }
        }
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                this.img_Product.setImageURI(imageUri);
            }
        }
    }

    // Lấy tất cả sản phẩm từ Firebase
    public void getAllProducts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Product product = snap.getValue(Product.class);
                    listProduct.add(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi truy xuất dữ liệu thất bại
            }
        });
    }

    // Thêm sản phẩm vào Firebase
    public void addProduct(Product product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product");
        if (listProduct.size() == 0) {
            product.setCodeProduct(1);
            reference.child("1").setValue(product);
        } else {
            int i = listProduct.size() - 1;
            int id = listProduct.get(i).getCodeProduct() + 1;
            product.setCodeProduct(id);
            reference.child("" + id).setValue(product);
        }
    }

    // Cập nhật sản phẩm trên Firebase
    public void updateProduct(Product product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product");
        reference.child("" + product.getCodeProduct()).child("nameProduct").setValue(product.getNameProduct());
        reference.child("" + product.getCodeProduct()).child("priceProduct").setValue(product.getPriceProduct());
    }

    // Xóa sản phẩm khỏi Firebase
    public void deleteProduct(Product product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product");
        reference.child("" + product.getCodeProduct()).removeValue();
    }

    // Kiểm tra trường dữ liệu không được để trống
    public boolean isEmptys(String str, TextInputLayout til) {
        if (str.isEmpty()) {
            til.setError("Không được để trống");
            Log.d("ProductFragment", "lỗi");
            return false;
        } else til.setError(null);
        return true;
    }

    // Kiểm tra lỗi ảnh
    public boolean errorImg(String str, TextView tv) {
        if (str != null) {
            tv.setText("");
            return true;
        } else {
            tv.setText("Ảnh không được để trống");
            return false;
        }
    }

    // Kiểm tra dữ liệu nhập vào
    public int validate(int type) {
        if (type == 0) {
            if (isEmptys(nameProduct, til_nameProduct) && isEmptys(priceProduct, til_priceProduct) && errorImg(imgProduct, tvErrorImg)) {
                setDataProduct();
                removeAll();
                return 0;
            }
        } else if (type == 1) {
            if (isEmptys(nameProduct, til_nameProduct) && isEmptys(priceProduct, til_priceProduct) && errorImg(imgProduct, tvErrorImg)) {
                return 1;
            }
        }
        return 2;
    }

    // Xóa dữ liệu sau khi thêm hoặc cập nhật sản phẩm
    public void removeAll() {
        til_nameProduct.getEditText().setText("");
        til_priceProduct.getEditText().setText("");
        img_Product.setImageResource(R.drawable.ic_menu_camera1);
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
                .setDeniedMessage("Nếu bạn không cấp quyền, bạn sẽ không thể tải ảnh lên\n\nVui lòng vào [Cài đặt] > [Quyền] và cấp quyền để sử dụng")
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
                .setDeniedMessage("Nếu bạn không cấp quyền, bạn sẽ không thể tải ảnh lên\n\nVui lòng vào [Cài đặt] > [Quyền] và cấp quyền để sử dụng")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

}
