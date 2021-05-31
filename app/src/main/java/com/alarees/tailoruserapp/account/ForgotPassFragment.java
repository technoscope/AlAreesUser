package com.alarees.tailoruserapp.account;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;


public class ForgotPassFragment extends Fragment {
    FirebaseAuth mAuth;
    EditText textInputEmail;
    Button sendbutton;
    FragmentManager manager;
    ImageView logoimage;
    LinearLayout container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager=getFragmentManager();
        textInputEmail=view.findViewById(R.id.id_reset_email);
        sendbutton=view.findViewById(R.id.send_email);
        logoimage= view.findViewById(R.id.forgotpass_logo);
        container=view.findViewById(R.id.forgotpass_container);
        int nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                container.setBackgroundResource(R.drawable.background);
                logoimage.setBackgroundResource(R.drawable.ic_logo1);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                container.setBackgroundResource(R.drawable.background_white);
                logoimage.setBackgroundResource(R.drawable.logonew);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                //doStuff();
                break;
        }
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.sendPasswordResetEmail(textInputEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "please check your email account, If you want to reset a password", Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.nav_host_fragment, new SigninFragment());
                        ft.addToBackStack("SigninFragment");
                        ft.commit();
                    }
                });
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
        ft.replace(R.id.nav_host_fragment, new SigninFragment());
        ft.addToBackStack("SigninFragment");
        ft.commit();
    }

}