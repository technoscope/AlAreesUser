package com.alarees.tailoruserapp.account;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.TitledFragment;
import com.alarees.tailoruserapp.home.HomeFragment;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.alarees.tailoruserapp.account.PhoneAuth.authflag;

public class SigninFragment extends Fragment implements TitledFragment {
    @BindView(R.id.ed_email)
    EditText email;
    @BindView(R.id.ed_password)
    EditText password;
    @BindView(R.id.btn_login)
    Button loginbtn;
    @BindView(R.id.txt_siginup)
    TextView signup;
    @BindView(R.id.btn_phone_signup)
    TextView phonesignup;
    @BindView(R.id.txt_forgot)
    TextView txtforgot;
    @BindView(R.id.id_google_signin)
    LinearLayout googlesignin;
    @BindView(R.id.container_signin)
    LinearLayout container;
    @BindView(R.id.logo)
    ImageView logo;
    FragmentManager manager;
    private int RC_SIGN_IN = 9902;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    public ProgressBar progressBar;
    DatabaseReference mref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //User Profile
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //container = view.findViewById(R.id.container_signin);
        int nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                container.setBackgroundResource(R.drawable.background);
                logo.setBackgroundResource(R.drawable.ic_logo1);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                container.setBackgroundResource(R.drawable.background_white);
                logo.setBackgroundResource(R.drawable.logonew);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                container.setBackgroundResource(R.drawable.background_white);
                logo.setBackgroundResource(R.drawable.logonew);
                break;
        }
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().isEmpty()) {
                    if (!password.getText().toString().isEmpty()) {
                        progressBar = new ProgressBar(getContext());
                        progressBar.setVisibility(View.VISIBLE);
                        DoubleBounce doubleBounce = new DoubleBounce();
                        doubleBounce.setBounds(0, 0, 100, 100);
                        doubleBounce.setColor(R.color.green_900);
                        progressBar.setIndeterminateDrawable(doubleBounce);
                        signIn(email.getText().toString(), password.getText().toString());
                    } else {
                        password.setError("Enter your Passowrd");
                    }
                } else {
                    email.setError("Enter your Email");
                }

            }
        });
        manager = getFragmentManager();
        txtforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, new ForgotPassFragment());
                ft.disallowAddToBackStack();
                ft.commit();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new SignUpFragment());
                ft.addToBackStack("SigninFragment");
                ft.commit();

            }
        });

        phonesignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PhoneAuth.class));
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });
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
        ft.replace(R.id.nav_host_fragment, new HomeFragment());
        ft.addToBackStack("HomeFragment");
        ft.commit();
    }

    private void signIn(String email1, String password1) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getContext(), "Sign in Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.nav_host_fragment, new HomeFragment());
                            ft.addToBackStack("HomeFragment");
                            ft.commit();
                            FirebaseUser user = mAuth.getCurrentUser();

                            //updateUI(user);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            email.setError("Wrong Credentials");
                            password.setError("Wrong Credentials");
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //  Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                // Toast.makeText(getContext(), ""+account.getId(), Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Toast.makeText(getContext(), ""+user.getUid(), Toast.LENGTH_SHORT).show();
                            authflag = 2;
                            mref = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                            mref.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild("UserProfile")) {
                                        FragmentTransaction ft = manager.beginTransaction();
                                        ft.replace(R.id.nav_host_fragment, new MyAccountDetailFragment());
                                        ft.addToBackStack("HomeFragment");
                                        ft.commit();
                                    } else {
                                        FragmentTransaction ft = manager.beginTransaction();
                                        ft.replace(R.id.nav_host_fragment, new SignUpFragment());
                                        ft.addToBackStack("SignUpFragment");
                                        ft.commit();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                            Toast.makeText(getContext(), "signin Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public String getTitle() {
        return "Account";
    }
}