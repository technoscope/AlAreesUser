package com.alarees.tailoruserapp.paymentgetway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.DashboardActivity;
import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.cart.CartUnstitchedModel;
import com.alarees.tailoruserapp.database.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;

public class ConfirmationActivity extends AppCompatActivity {
    DatabaseReference myRef;
    ArrayList<CartUnstitchedModel> ordermodels;
    DatabaseHelper databaseHelper;
    ImageView back, statusimagee;
    TextView status, congrats;
    Button contin;
    LinearLayout container;
    String orderid;
    String totalpayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        getSupportActionBar().hide();
        container=findViewById(R.id.confirmation_container);
        int nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                container.setBackgroundResource(R.drawable.background);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                container.setBackgroundResource(R.drawable.background_white);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                //doStuff();
                break;
        }
        status = findViewById(R.id.status);
        congrats = findViewById(R.id.congrates);
        back = findViewById(R.id.back_confirmation);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmationActivity.this, DashboardActivity.class));
            }
        });
        contin = findViewById(R.id.continue_shopping);
        contin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmationActivity.this, DashboardActivity.class));
            }
        });
        //Getting Intent
        try {
            String res = getIntent().getStringExtra("PaymentDetails");
            JSONObject jsonDetails = new JSONObject(res);
            //Displaying payment details
            showDetails(jsonDetails);
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showDetails(JSONObject jsonDetails) throws JSONException {
        //Views
        TextView textViewId = findViewById(R.id.paymentId);
        TextView textViewStatus = findViewById(R.id.paymentStatus);
        TextView textViewAmount = findViewById(R.id.paymentAmount);
        statusimagee = findViewById(R.id.statusimage);
        //Showing the details from json object
        myOrder();
        if (jsonDetails.getString("status").equals("Succeeded")) {
            textViewId.setText(jsonDetails.getString("id"));
            textViewStatus.setText(jsonDetails.getString("status"));
            textViewAmount.setText(Float.parseFloat(jsonDetails.getString("amount")) / 100 + " USD");
            totalpayment = String.valueOf(Float.parseFloat(jsonDetails.getString("amount")) / 100);
            congrats.setText("Congratulation");
            status.setText("Your order was succesfully processed \n         Payment details is below:");
            myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
            for (CartUnstitchedModel m : ordermodels) {
                if (m.getCollarType() == null) {
                    myRef.child("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(orderid).child("Readymade").setValue(m);
                } else {
                    myRef.child("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(orderid).child("Unstitched").setValue(m);
                }
            }
        } else {
            congrats.setText("Oops");
            status.setText("Something went wrong:");
            statusimagee.setImageResource(R.drawable.oops);
        }
    }

    public void myOrder() {
        myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        orderid = myRef.child("Orders").push().getKey();
        ordermodels = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        Cursor res = databaseHelper.getUnstitchedData();
        if (res != null) {
            if (res.getCount() != 0) {
                while (res.moveToNext()) {
                    CartUnstitchedModel rModel = new CartUnstitchedModel();
                    rModel.setUserId(res.getString(1));
                    rModel.setOrderId(orderid);
                    rModel.setClothName(res.getString(3));
                    rModel.setPrice(res.getString(4));
                    rModel.setCuffType(res.getString(5));
                    rModel.setPlacketType(res.getString(6));
                    rModel.setPocketType(res.getString(7));
                    rModel.setCollarType(res.getString(8));
                    rModel.setQuantity(res.getString(9));
                    rModel.setClothimg(res.getString(10));
                    rModel.setMeasurementId(res.getString(11));
                    rModel.setSize(res.getString(12));
                    rModel.setOrderdate(getTodayDate());
                    rModel.setTotalpayment(totalpayment);
                    ordermodels.add(rModel);
                }
            }
        }
    }

    public String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getOrderDateFormat(String date) {
        final String NEW_FORMAT = "yyyy-MM-dd";
        String oldDateString = date;
        String newDateString;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(oldDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);
        return newDateString;
    }
}