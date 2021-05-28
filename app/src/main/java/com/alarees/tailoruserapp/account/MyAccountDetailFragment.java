package com.alarees.tailoruserapp.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.home.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountDetailFragment extends Fragment{
    @BindView(R.id.ed_fname)
    EditText fname;
    @BindView(R.id.ed_lname)
    EditText lname;
    @BindView(R.id.ed_email)
    EditText ename;
    @BindView(R.id.ed_weight)
    EditText eWeight;
    @BindView(R.id.ed_height)
    EditText eHeight;
    @BindView(R.id.ed_password)
    TextView epassowrd;
    @BindView(R.id.ed_mobile)
    EditText mobileno;
    @BindView(R.id.ed_address)
    EditText address;
    @BindView(R.id.btn_singup)
    Button singup;
    Unbinder unbinder;
    private FirebaseAuth mAuth;
    // [END declare_auth]
    DatabaseReference mRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
       // Toast.makeText(getContext(), ""+FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserProfile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AccountModel model = snapshot.getValue(AccountModel.class);
                String[] splited = model.getName().split("\\s+");
                try {
                    fname.setText(splited[0]);
                    lname.setText(splited[1]);
                } catch (Exception e) {
                }
                address.setText(model.getAddress());
                ename.setText(model.getEmail());
                mobileno.setText(model.getMobileno());
                epassowrd.setText("xxxxxxxxxxxx");
                eHeight.setText(model.getHeight());
                eWeight.setText(model.getWeight());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI(FirebaseAuth.getInstance().getCurrentUser());
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        AccountModel accountModel = new AccountModel();
        accountModel.setAddress(address.getText().toString());
        accountModel.setEmail(ename.getText().toString());
        accountModel.setMobileno(mobileno.getText().toString());
        accountModel.setName(fname.getText().toString() + " " + lname.getText().toString());
        accountModel.setUserid(user.getUid());
        accountModel.setWeight(eWeight.getText().toString());
        accountModel.setHeight(eHeight.getText().toString());
        mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserProfile").setValue(accountModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Account updated!", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new HomeFragment());
                ft.addToBackStack("HomeFragment");
                ft.commit();
            }
        });
    }

}