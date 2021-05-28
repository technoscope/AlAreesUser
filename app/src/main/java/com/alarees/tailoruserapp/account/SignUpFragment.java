package com.alarees.tailoruserapp.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.measurement.HowToFragment;
import com.alarees.tailoruserapp.TitledFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment implements TitledFragment {
    @BindView(R.id.ed_fname)
    EditText fname;
    @BindView(R.id.ed_lname)
    EditText lname;
    @BindView(R.id.ed_height)
    EditText height;
    @BindView(R.id.ed_weight)
    EditText weight;
    @BindView(R.id.ed_email)
    EditText ename;
    @BindView(R.id.ed_password)
    EditText epassowrd;
    @BindView(R.id.ed_mobile)
    EditText mobileno;
    @BindView(R.id.ed_address)
    EditText address;
    @BindView(R.id.btn_singup)
    Button singup;
    Unbinder unbinder;
    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    DatabaseReference mRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.getEmail();
            reload();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        FirebaseUser user = mAuth.getCurrentUser();
        if (PhoneAuth.authflag == 1) {
            if (getArguments().getInt("phoneauth") == 10) {
                mobileno.setText(user.getPhoneNumber());
            }
        } else if (PhoneAuth.authflag == 2) {
            ename.setText(user.getEmail());

        }
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PhoneAuth.authflag == 1) {
                    updateUI(user);
                    if (getArguments().getInt("phoneauth") == 10) {
                    }
                } else if (PhoneAuth.authflag == 2) {
                    updateUI(user);

                } else {
                    createAccount(ename.getText().toString(), epassowrd.getText().toString());
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                updateUI(user);
                            } else {

                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
        // [END send_email_verification]
    }

    private void reload() {
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
            AccountModel accountModel = new AccountModel();
            accountModel.setAddress(address.getText().toString());
            accountModel.setEmail(ename.getText().toString());
            accountModel.setMobileno(mobileno.getText().toString());
            accountModel.setName(fname.getText().toString() + " " + lname.getText().toString());
            accountModel.setUserid(user.getUid());
            accountModel.setHeight(height.getText().toString());
            accountModel.setWeight(weight.getText().toString());
            mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserProfile").setValue(accountModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Account created!", Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, new HowToFragment());
                    ft.addToBackStack("SignUpFragment");
                    ft.commit();
                }
            });
        }
    }

    @Override
    public String getTitle() {
        return "singup";
    }
}