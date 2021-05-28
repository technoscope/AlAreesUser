package com.alarees.tailoruserapp.paymentgetway;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alarees.tailoruserapp.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckoutActivityJava extends AppCompatActivity {
    // 10.0.2.2 is the Android emulator's alias to localhost
    private static final String BACKEND_URL = "http://ahmed.botsolutions.org/";
    private final OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private TextView waiting;
    ProgressBar waitbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_java);
        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        waitbar = findViewById(R.id.progwait);
        waiting = findViewById(R.id.waiting);
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51Ib0dfEPtOtpUpfzXLN8NxSZRWKvQ32ahtMzoyOPSaQi00TNmChCIZ1FIP7jUjpdp0WaTqxVeUZqh9pOz4BfkxkG00GKppSzRI")
        );
        startCheckout();
    }

    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        float amount=Float.parseFloat(getIntent().getStringExtra("price").trim())*100;
       // int amount =60;// (int) Math.ceil(famount);//Integer.parseInt(getIntent().getStringExtra("price").trim());
        MediaType mediaType = MediaType.get("application/json");
        Map<String, Object> payMap = new HashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        payMap.put("currency", "usd"); //dont change currency in testing phase otherwise it won't work
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("id", "photo_subscription");
        itemMap.put("amount", amount);
        itemList.add(itemMap);
        payMap.put("items", itemList);
        String json = new Gson().toJson(payMap);
        RequestBody body = RequestBody.create(json, mediaType);
        //Toast.makeText(this, "" + body.toString(), Toast.LENGTH_SHORT).show();
////////////////////////////////////////////////////////////////////////////////////
        String api = BACKEND_URL+ "index.php";
        try {
            JSONObject jsonObject1=new JSONObject(json);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(1, api, jsonObject1, new com.android.volley.Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    try {
                        paymentIntentClientSecret=response.getString("clientSecret");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "hhhhhhhhhh" + error, Toast.LENGTH_SHORT).show();
                    // callBack.onError(error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    super.getHeaders();
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(300000, 0, 1.0F));
            Volley.newRequestQueue(this).add(jsonObjReq);
        } catch (Exception e) {
        }


        //////////////////////////////////////////////////////////////////////////
//        Request request = new Request.Builder()
//                .url(BACKEND_URL + "index.php")
//                .post(body)
//                .build();
//        httpClient.newCall(request)
//                .enqueue(new PayCallback(this));
        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            waitbar.setVisibility(View.VISIBLE);
            waiting.setVisibility(View.VISIBLE);

            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }
        });
    }

//    public void volleyrequest(){
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("gender", "male");
//            jsonObject.put("height", height);
//            jsonObject.put("weight", weight);
//            jsonObject.put("front_image", frontimage);
//            jsonObject.put("side_image", sideimage);
//
//        } catch (Exception var20) {
//            var20.printStackTrace();
//        }
//        if (jsonObject.length() > 0) {
//
//        }
//    }


    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        Intent intent = new Intent(CheckoutActivityJava.this, ConfirmationActivity.class);
        intent.putExtra("PaymentDetails", message);
        startActivity(intent);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setTitle(title)
//                .setMessage(message);
//        builder.setPositiveButton("Ok", null);
//        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> responseMap = gson.fromJson(Objects.requireNonNull(response.body()).string(), type);
        paymentIntentClientSecret = responseMap.get("clientSecret");
//        Toast.makeText(this, "" + paymentIntentClientSecret, Toast.LENGTH_SHORT).show();
    }

    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<CheckoutActivityJava> activityRef;

        PayCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "ErrorAHmD: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "ErrorActual: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<CheckoutActivityJava> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent)
                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
}