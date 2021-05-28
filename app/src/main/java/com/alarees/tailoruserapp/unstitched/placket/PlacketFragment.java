package com.alarees.tailoruserapp.unstitched.placket;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.bumptech.glide.Glide;
import com.alarees.tailoruserapp.unstitched.collar.CollarFragment;
import com.alarees.tailoruserapp.unstitched.model.TypeModel;
import com.alarees.tailoruserapp.unstitched.pocket.PocketFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.alarees.tailoruserapp.unstitched.collar.CollarFragment.collarImaeBaseUrl;


public class PlacketFragment extends Fragment {
    PlacketViewModel viewModel;
    @BindView(R.id.id_p_pocket_photo)
    ImageView pocketimage;
    @BindView(R.id.id_p_collar_photo)
    ImageView collarimage;
    @BindView(R.id.id_p_cuff_photo)
    ImageView cuffimage;
    @BindView(R.id.id_p_placket_photo)
    ImageView placketimage;
    @BindView(R.id.placket_grid)
    GridView gridView;
    @BindView(R.id.id_placket_back)
    LinearLayout imgback;
    @BindView(R.id.id_placket_next)
    LinearLayout imgforward;
    Unbinder unbinder;
    PlacketAdapter adapter;
    TypeModel typeModel;
    ArrayList<TypeModel> models;
    DatabaseReference mRef;
    FragmentManager manager;
    TypeModel selectedModel;
    public static String placketImaeBaseUrl, placketName,placketID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(PlacketViewModel.class);
        viewModel.getLivemodel().observe(this, new Observer<TypeModel>() {
            @Override
            public void onChanged(TypeModel typeModel) {
                Glide.with(getActivity())
                        .load(Uri.parse(typeModel.getImgBaseUrl()))
                        .into(placketimage);
                selectedModel=new TypeModel();
                selectedModel=typeModel;
                placketImaeBaseUrl=typeModel.getImgBaseUrl();
                placketName=typeModel.getTypeName();
                placketID=typeModel.getTypeID();
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_placket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder= ButterKnife.bind(this,view);
        manager= getFragmentManager();
        Glide.with(getActivity())
                .load(Uri.parse(collarImaeBaseUrl))
                .into(collarimage);
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        mRef.child("PlacketType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models=new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    typeModel=new TypeModel();
                    typeModel = data.getValue(TypeModel.class);
                    models.add(typeModel);
                }
                adapter = new PlacketAdapter(getContext(), models);
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
                CollarFragment pf=new CollarFragment();
                ft.replace(R.id.nav_host_fragment, pf);
                ft.addToBackStack("CollarFragment");
                ft.commit();
            }
        });
        imgforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FragmentTransaction ft = manager.beginTransaction();
                    Bundle bundle=new Bundle();
                    bundle.putString(selectedModel.getImgBaseUrl(),"placketbaseurl");
                    bundle.putString(selectedModel.getTypeName(),"placketname");
                    bundle.putString(selectedModel.getTypeID(),"placketID");
                    PocketFragment pf=new PocketFragment();
                    pf.setArguments(bundle);
                    ft.replace(R.id.nav_host_fragment, pf);
                    ft.addToBackStack("PlacketFragment");
                    ft.commit();
                }catch (Exception e){
                    Toast.makeText(getContext(), "Select Placket First", Toast.LENGTH_SHORT).show();
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
        ft.replace(R.id.nav_host_fragment, new CollarFragment());
        ft.addToBackStack("CollarFragment");
        ft.commit();
    }
}