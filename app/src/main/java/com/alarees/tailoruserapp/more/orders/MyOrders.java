package com.alarees.tailoruserapp.more.orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.account.SigninFragment;
import com.alarees.tailoruserapp.TitledFragment;
import com.alarees.tailoruserapp.more.MoreItemFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyOrders extends Fragment implements TitledFragment {
    ArrayList<OrderModel> ordermodels;
    RecyclerView recyclerView;
    DatabaseReference myRef;
    String ordernode;
    ImageView myorder;
    FragmentManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myorder = view.findViewById(R.id.back_myorder);
        myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new MoreItemFragment());
                transaction.addToBackStack("MoreItemFragment");
                transaction.commit();

            }
        });
        recyclerView = view.findViewById(R.id.my_orders_list);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase").child("Orders");
            ordermodels = new ArrayList<>();
            myRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        ordernode = data.getKey();
                        myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase").child("Orders").child(FirebaseAuth.getInstance().getUid());
                        myRef.child(ordernode).child("Unstitched").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("orderId")) {
                                    ordermodels.add(snapshot.getValue(OrderModel.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase").child("Orders").child(FirebaseAuth.getInstance().getUid());
                        myRef.child(ordernode).child("Readymade").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("orderId")) {
                                    ordermodels.add(snapshot.getValue(OrderModel.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(new OrdersAdapter(ordermodels));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }else{
            Toast.makeText(getContext(), getResources().getString(R.string.toast_msg_create_account), Toast.LENGTH_SHORT).show();
            FragmentManager manager=getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.nav_host_fragment, new SigninFragment());
            ft.addToBackStack("HomeFragment");
            ft.commit();
        }
    }

    @Override
    public String getTitle() {
        return "My Orders";
    }
}