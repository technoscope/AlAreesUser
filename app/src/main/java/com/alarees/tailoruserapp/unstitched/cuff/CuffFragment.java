package com.alarees.tailoruserapp.unstitched.cuff;

import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.account.AccountModel;
import com.alarees.tailoruserapp.measurement.measurementlist.ChildModel;
import com.alarees.tailoruserapp.measurement.measurementlist.MAdabter;
import com.alarees.tailoruserapp.unstitched.collar.CollarFragment;
import com.alarees.tailoruserapp.unstitched.model.TypeModel;
import com.alarees.tailoruserapp.unstitched.placket.PlacketFragment;
import com.alarees.tailoruserapp.unstitched.pocket.PocketFragment;
import com.alarees.tailoruserapp.unstitched.selector.SelectorAdabter;
import com.bumptech.glide.Glide;
import com.alarees.tailoruserapp.cart.CartFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CuffFragment extends Fragment {

    CuffViewModel viewModel;
    @BindView(R.id.id_cf_pocket_photo)
    ImageView pocketimage;
    @BindView(R.id.id_cf_collar_photo)
    ImageView collarimage;
    @BindView(R.id.id_cf_cuff_photo)
    ImageView cuffimage;
    @BindView(R.id.id_cf_placket_photo)
    ImageView placketimage;
    @BindView(R.id.cuff_grid)
    GridView gridView;
    @BindView(R.id.id_cuff_back)
    LinearLayout imgback;
    @BindView(R.id.id_cuff_next)
    LinearLayout imgforward;
    Unbinder unbinder;
    CuffAdapter adapter;
    TypeModel typeModel;
    ArrayList<TypeModel> models;
    DatabaseReference mRef;
    FragmentManager manager;
    TypeModel selectedModel;
    public static String cuffImaeBaseUrl, cuffName, cuffID;
    ArrayList<ChildModel> childmodes;
    Dialog dialog;
    private static String height;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(CuffViewModel.class);
        viewModel.getLivemodel().observe(this, new Observer<TypeModel>() {
            @Override
            public void onChanged(TypeModel typeModel) {
                selectedModel = new TypeModel();
                selectedModel = typeModel;
                Glide.with(getActivity())
                        .load(Uri.parse(typeModel.getImgBaseUrl()))
                        .into(cuffimage);
                cuffImaeBaseUrl = selectedModel.getImgBaseUrl();
                cuffName = selectedModel.getTypeName();
                cuffID = selectedModel.getTypeID();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cuff, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        manager = getFragmentManager();
        childmodes = new ArrayList<>();
        Glide.with(getActivity())
                .load(Uri.parse(PocketFragment.pocketImaeBaseUrl))
                .into(pocketimage);
        Glide.with(getActivity())
                .load(Uri.parse(PlacketFragment.placketImaeBaseUrl))
                .into(placketimage);
        Glide.with(getActivity())
                .load(Uri.parse(CollarFragment.collarImaeBaseUrl))
                .into(collarimage);
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        mRef.child("CuffType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    typeModel = new TypeModel();
                    typeModel = data.getValue(TypeModel.class);
                    models.add(typeModel);
                }
                adapter = new CuffAdapter(getContext(), models);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), " g=" + models.get(i).getTypeName(), Toast.LENGTH_SHORT).show();
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to previous
                FragmentTransaction ft = manager.beginTransaction();
                PocketFragment pf = new PocketFragment();
                ft.replace(R.id.nav_host_fragment, pf);
                ft.addToBackStack("PocketFragment");
                ft.commit();
            }
        });
        imgforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //how to
                    dialog();

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Select Cuff First", Toast.LENGTH_SHORT).show();
                }

            }
        });
        populateparent();
        populatechild();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    backtoparent();
                    return true;
                }
                return false;
            }
        });
    }

    private void backtoparent() {
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, new PocketFragment());
        ft.addToBackStack("PocketFragment");
        ft.commit();
    }

    public void dialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.measurement_picker);
        RecyclerView recyclerView = dialog.findViewById(R.id.dialog_childlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SelectorAdabter(childmodes, getContext(), manager, dialog));
        TextView parentname = dialog.findViewById(R.id.id_parentmeasurement);
        parentname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadfragment(height);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void populatechild() {
        try {
            mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
            mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ChildMeasurementParams").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key = snapshot1.getKey();
                        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                        mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ChildMeasurementParams").child(key).child("ChildProfile").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ChildModel model = snapshot.getValue(ChildModel.class);
                                childmodes.add(model);
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

    public void populateparent() {
        try {
            mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
            mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserProfile").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    AccountModel accountModel = snapshot.getValue(AccountModel.class);
                    height = accountModel.getHeight();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadfragment(String height) {
        FragmentTransaction ft = manager.beginTransaction();
        CartFragment cf = new CartFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fromUnstitched", "un");
        bundle.putString("nodekey", "parent");
        bundle.putString("height", height);
        cf.setArguments(bundle);
        ft.replace(R.id.nav_host_fragment, cf);
        ft.addToBackStack("CuffFragment");
        ft.commit();
    }
}