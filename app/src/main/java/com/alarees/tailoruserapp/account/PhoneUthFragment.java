package com.alarees.tailoruserapp.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PhoneUthFragment extends Fragment {
    private int RC_SIGN_IN = 399;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_uth, container, false);
        //authentication through phone

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            phoneUI();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Line 32" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneUI() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        //   .setTheme(R.style.AppTheme)

                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                try {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, new SignUpFragment());
                    ft.addToBackStack("SigninFragment");
                    ft.commit();
                    // ...
                } catch (Exception e) {
                    Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                }

            } else {
                if (response != null) {
                    Toast.makeText(getContext(), "" + response.getError(), Toast.LENGTH_SHORT).show();
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...}
                } else {

                    Toast.makeText(getContext(), "Enter Your Contact Number", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();


        // Intent intent = new Intent(PhoneAuth.this, PhoneAuth.class);
        //startActivity(intent);

    }

}