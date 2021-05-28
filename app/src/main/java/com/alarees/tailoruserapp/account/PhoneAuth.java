package com.alarees.tailoruserapp.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.alarees.tailoruserapp.DashboardActivity;
import com.alarees.tailoruserapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class PhoneAuth extends AppCompatActivity {
    private int RC_SIGN_IN = 399;
    public static int authflag = 0;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        //authentication through phone
        try {
            phoneUI();
        } catch (Exception e) {
            Toast.makeText(this, "Line 32" + e, Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                try {
                    Intent myIntent = new Intent(PhoneAuth.this, DashboardActivity.class);
                    myIntent.putExtra("flag", "2");
                    startActivity(myIntent);
                    PhoneAuth.this.finish();
                    // ...
                } catch (Exception e) {
                    Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
                }

            } else {
                if (response != null) {
                    Toast.makeText(this, "" + response.getError(), Toast.LENGTH_SHORT).show();
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...}
                } else {

                    Toast.makeText(this, "Enter Your Contact Number", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        authflag = 1;
        // Intent intent = new Intent(PhoneAuth.this, PhoneAuth.class);
        //startActivity(intent);

    }

//    }


}


