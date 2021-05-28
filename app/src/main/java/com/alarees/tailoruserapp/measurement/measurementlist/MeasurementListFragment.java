package com.alarees.tailoruserapp.measurement.measurementlist;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.TitledFragment;
import com.alarees.tailoruserapp.measurement.MyDetialFragment;
import com.alarees.tailoruserapp.measurement.ParentMeasurementDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MeasurementListFragment extends Fragment implements TitledFragment {
    RecyclerView recyclerView;
    ArrayList<ChildModel> models;
    TextView parentmeasuresment;
    FragmentManager manager;
    LinearLayout addnewmeasurement;
    DatabaseReference mRef;
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measurement_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        models = new ArrayList<>();
        manager = getFragmentManager();
        parentmeasuresment = view.findViewById(R.id.id_parentmeasurement);
        parentmeasuresment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentMeasurementDetail fragment = new ParentMeasurementDetail();
                loadFragment(fragment);
            }
        });
        addnewmeasurement = view.findViewById(R.id.add_new_measurement);
        addnewmeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ChildInputFragment());
            }
        });
        try {
            mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
            mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ChildMeasurementParams").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1:snapshot.getChildren()) {
                        String key = snapshot1.getKey();
                        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                        mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ChildMeasurementParams").child(key).child("ChildProfile").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ChildModel model = snapshot.getValue(ChildModel.class);
                                models.add(model);
                                recyclerView = view.findViewById(R.id.measurement_childlist);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(new MAdabter(getContext(),manager, models));
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void loadFragment(Fragment fragment) {
        manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack("MoreItemFragment");
        transaction.commit();
    }

    @Override
    public String getTitle() {
        return getResources().getString(R.string.my_measurement);
    }
}