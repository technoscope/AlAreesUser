package com.alarees.tailoruserapp.unstitched.collar;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.unstitched.placket.PlacketFragment;
import com.bumptech.glide.Glide;
import com.alarees.tailoruserapp.unstitched.UnstitchedItemsFragment;
import com.alarees.tailoruserapp.unstitched.model.TypeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CollarFragment extends Fragment {

    CollarViewModel viewModel;
    @BindView(R.id.id_c_pocket_photo)
    ImageView pocketimage;
    @BindView(R.id.id_c_collar_photo)
    ImageView collarimage;
    @BindView(R.id.id_c_cuff_photo)
    ImageView cuffimage;
    @BindView(R.id.id_c_placket_photo)
    ImageView placketimage;
    @BindView(R.id.collar_grid)
    GridView gridView;
    @BindView(R.id.id_collar_back)
    LinearLayout imgback;
    @BindView(R.id.id_collar_next)
    LinearLayout imgforward;
    Unbinder unbinder;
    CollarAdapter adapter;
    TypeModel typeModel;
    ArrayList<TypeModel> models;
    DatabaseReference mRef;
    FragmentManager manager;
    TypeModel selectedModel;
    public static String collarImaeBaseUrl, collarName,collarID,itemprice,itemimgg,itemname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(CollarViewModel.class);
        viewModel.getLivemodel().observe(this, new Observer<TypeModel>() {
            @Override
            public void onChanged(TypeModel typeModel) {
                selectedModel = new TypeModel();
                selectedModel = typeModel;
                Glide.with(getActivity())
                        .load(Uri.parse(typeModel.getImgBaseUrl()))
                        .into(collarimage);
                collarImaeBaseUrl = typeModel.getImgBaseUrl();
                collarName = typeModel.getTypeName();
                collarID=typeModel.getTypeID();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        try {
            itemprice = getArguments().getString("itemprice");
            itemimgg = getArguments().getString("itemimg");
            itemname = getArguments().getString("itemname");
        }catch (Exception e){
            Log.d("collar","price is null from bundle");
        }finally {

        }
        manager = getFragmentManager();
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        mRef.child("CollarType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    typeModel = new TypeModel();
                    typeModel = data.getValue(TypeModel.class);
                    models.add(typeModel);
                }
                adapter = new CollarAdapter(getContext(), models);
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
                ft.replace(R.id.nav_host_fragment, new UnstitchedItemsFragment());
                ft.addToBackStack("UnsticheditemsFragment");
                ft.commit();
            }
        });
        imgforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = manager.beginTransaction();
                Bundle bundle = new Bundle();
                try {
                    bundle.putString(selectedModel.getImgBaseUrl(), "collarbaseurl");
                    bundle.putString(selectedModel.getTypeName(), "collarname");
                    bundle.putString(selectedModel.getTypeID(), "collarID");
                    PlacketFragment pf = new PlacketFragment();
                    pf.setArguments(bundle);
                    ft.replace(R.id.nav_host_fragment, pf);
                    ft.addToBackStack("CollarFragment");
                    ft.commit();
                }catch (Exception e){
                    Toast.makeText(getContext(), "Select Collar First", Toast.LENGTH_SHORT).show();
                }

            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    backtoparent();
                    return true;
                }
                return false;
            }
        });
    }

    private void backtoparent() {
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, new UnstitchedItemsFragment());
        ft.addToBackStack("UnstitchedItemsFragment");
        ft.commit();
    }

}