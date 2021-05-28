package com.alarees.tailoruserapp.unstitched.pocket;

import android.annotation.SuppressLint;
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

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.unstitched.placket.PlacketFragment;
import com.bumptech.glide.Glide;
import com.alarees.tailoruserapp.unstitched.cuff.CuffFragment;
import com.alarees.tailoruserapp.unstitched.model.TypeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.alarees.tailoruserapp.unstitched.collar.CollarFragment.collarImaeBaseUrl;

public class PocketFragment extends Fragment {
    PocketViewModel viewModel;
    @BindView(R.id.id_poc_pocket_photo)
    ImageView pocketimage;
    @BindView(R.id.id_poc_collar_photo)
    ImageView collarimage;
    @BindView(R.id.id_poc_cuff_photo)
    ImageView cuffimage;
    @BindView(R.id.id_poc_placket_photo)
    ImageView placketimage;
    @BindView(R.id.pocket_grid)
    GridView gridView;
    @BindView(R.id.id_pocket_back)
    LinearLayout imgback;
    @BindView(R.id.id_pocket_next)
    LinearLayout imgforward;
    Unbinder unbinder;
    PocketAdapter adapter;
    TypeModel typeModel;
    TypeModel selectedModel;
    public static String pocketImaeBaseUrl, pocketName, pocketID;

    ArrayList<TypeModel> models;
    DatabaseReference mRef;
    FragmentManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(PocketViewModel.class);
        viewModel.getLivemodel().observe(this, new Observer<TypeModel>() {
            @Override
            public void onChanged(TypeModel typeModel) {
                Glide.with(getActivity())
                        .load(Uri.parse(typeModel.getImgBaseUrl()))
                        .into(pocketimage);

                selectedModel = new TypeModel();
                selectedModel = typeModel;
                pocketImaeBaseUrl = typeModel.getImgBaseUrl();
                pocketName = typeModel.getTypeName();
                pocketID = typeModel.getTypeID();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pocket, container, false);
    }

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        manager = getFragmentManager();
        Glide.with(getActivity())
                .load(Uri.parse(PlacketFragment.placketImaeBaseUrl))
                .into(placketimage);
        Glide.with(getActivity())
                .load(Uri.parse(collarImaeBaseUrl))
                .into(collarimage);
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        mRef.child("PocketType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    typeModel = new TypeModel();
                    typeModel = data.getValue(TypeModel.class);
                    models.add(typeModel);
                }
                adapter = new PocketAdapter(getContext(), models);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), " g=" + models.get(i).getTypeName(), Toast.LENGTH_SHORT).show();
//            }
//        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to previous
                FragmentTransaction ft = manager.beginTransaction();
                PlacketFragment uf = new PlacketFragment();
                ft.replace(R.id.nav_host_fragment, uf);
                ft.addToBackStack("PlacketFragment");
                ft.commit();
            }
        });
        imgforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FragmentTransaction ft = manager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString(selectedModel.getImgBaseUrl(), "baseurl");
                    bundle.putString(selectedModel.getTypeName(), "name");
                    bundle.putString(selectedModel.getTypeID(), "ID");
                    CuffFragment pf = new CuffFragment();
                    pf.setArguments(bundle);
                    ft.replace(R.id.nav_host_fragment, pf);
                    ft.addToBackStack("PocketFragment");
                    ft.commit();

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Select Pocket First", Toast.LENGTH_SHORT).show();
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
        ft.replace(R.id.nav_host_fragment, new PlacketFragment());
        ft.addToBackStack("PlacketFragment");
        ft.commit();
    }
}