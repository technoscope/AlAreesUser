package com.alarees.tailoruserapp.measurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.alarees.tailoruserapp.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;

public class FrontActivity extends AppCompatActivity implements IPickResult {
    public static String frontPhoto;
    Button front_take;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        front_take = findViewById(R.id.front_take);
        getSupportActionBar().hide();
        front_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(FrontActivity.this);
            }
        });
    }

    @Override
    public void onPickResult(PickResult r) {
        BitMapToString(r.getBitmap());
        Intent intent=new Intent(new Intent(FrontActivity.this,SideActivity.class));
        startActivity(intent);
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        frontPhoto = Base64.encodeToString(b, Base64.DEFAULT);

        return frontPhoto;
    }

}