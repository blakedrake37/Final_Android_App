package com.example.greenfoodsapp.Fragment.BottomNav;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.greenfoodsapp.Adapter.Partner_FoodAdapter;
import com.example.greenfoodsapp.Model.Partner;
import com.example.greenfoodsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Lê Nguyễn Toàn Tâm - 21110797
public class PartnerFoodFragment extends Fragment implements Partner_FoodAdapter.ItemClickListener {
    private RecyclerView recyclerView_Partner_Food;
    private LinearLayoutManager linearLayoutManager;
    private List<Partner> list;
    private Partner_FoodAdapter partner_foodAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partner_food, container, false);

        // Khởi tạo RecyclerView và thiết lập LayoutManager
        recyclerView_Partner_Food = view.findViewById(R.id.recyclerView_Partner_Food);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_Partner_Food.setLayoutManager(linearLayoutManager);

        // Lấy danh sách đối tác và thiết lập Adapter
        list = getAllPartner();
        partner_foodAdapter = new Partner_FoodAdapter(list, this);
        recyclerView_Partner_Food.setAdapter(partner_foodAdapter);

        return view;
    }

    // Hàm lấy danh sách đối tác từ Firebase
    public List<Partner> getAllPartner() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Partner");
        List<Partner> list1 = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Partner partner = snap.getValue(Partner.class);
                    list1.add(partner);
                }
                partner_foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi hủy lấy dữ liệu từ Firebase
            }
        });
        return list1;
    }

    // Hàm xử lý sự kiện khi một đối tác được click
    @Override
    public void onItemClick(Partner partner) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Partner", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("partner", partner.getUserPartner());
        editor.apply();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_Home, new Food_Of_PartnerFragment(), null).addToBackStack(null).commit();
    }
}
