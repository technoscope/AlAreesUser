package com.alarees.tailoruserapp.measurement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.alarees.tailoruserapp.DashboardActivity;
import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.account.AccountModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;

import static com.alarees.tailoruserapp.measurement.HowToFragment.childflag;
import static com.alarees.tailoruserapp.measurement.measurementlist.ChildInputFragment.childheight;
import static com.alarees.tailoruserapp.measurement.measurementlist.ChildInputFragment.childweight;

public class SideActivity extends AppCompatActivity implements IPickResult {
    Button takesidephoto;
    public static int height;
    public static int weight;
    DatabaseReference myRef;
    public static String sidePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side);
        getSupportActionBar().hide();
        if(childflag==0) {
            myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
            myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserProfile").addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    AccountModel model = snapshot.getValue(AccountModel.class);
                    try {
                        height = Integer.parseInt(model.getHeight());
                        weight = Integer.parseInt(model.getWeight());
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(childflag==1){
                height=childheight;
                weight=childweight;
        }
        takesidephoto=findViewById(R.id.take_side_photo);
        takesidephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(SideActivity.this);

            }
        });
    }

    @Override
    public void onPickResult(PickResult r) {
        if(r!=null) {
            BitMapToString(r.getBitmap());
            Intent intent=new Intent(SideActivity.this, DashboardActivity.class);
            intent.putExtra("flag","1");
            startActivity(intent);
        }
    }
    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        sidePhoto = Base64.encodeToString(b, Base64.DEFAULT);
        return sidePhoto;
    }

}