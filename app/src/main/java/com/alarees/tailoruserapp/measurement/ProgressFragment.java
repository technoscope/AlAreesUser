package com.alarees.tailoruserapp.measurement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.measurement.measurementlist.ChildInputFragment;
import com.alarees.tailoruserapp.measurement.measurementlist.ChildMeasurementDetail;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ms_android_sdk.CallBack;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.alarees.tailoruserapp.measurement.FrontActivity.frontPhoto;
import static com.alarees.tailoruserapp.measurement.HowToFragment.childflag;
import static com.alarees.tailoruserapp.measurement.SideActivity.sidePhoto;
import static com.alarees.tailoruserapp.measurement.SideActivity.height;
import static com.alarees.tailoruserapp.measurement.SideActivity.weight;


public class ProgressFragment extends Fragment {
    public static String user3dlookId;
    public static String getURL;
    DatabaseReference myRef;
    Handler handler;
    TextView progresstxt;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_porgress, container, false);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences("3dlookUserurl", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        progresstxt = view.findViewById(R.id.progress_text);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progresstxt.setText("Processing your data... ");
                MS_measurement_Process(getContext(), "male", height, weight, frontPhoto, sidePhoto, new CallBack() {
                    @Override
                    public void onSuccess(String s) {
                        getURL = s;
                        progresstxt.setText("Processing your Images... ");
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(childflag==1){
                                    progresstxt.setText("Success");
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.nav_host_fragment, new ChildMeasurementDetail());
                                    ft.addToBackStack("ProgressFragment");
                                    ft.commit();
                                }else {
                                    progresstxt.setText("Success");
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.nav_host_fragment, new ParentMeasurementDetail());
                                    ft.addToBackStack("ProgressFragment");
                                    ft.commit();
                                }
                            }
                        }, 12000);

                    }

                    @Override
                    public void onError(VolleyError volleyError) {
                        Toast.makeText(getContext(), "" + volleyError, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 2000);

    }

    public void MS_measurement_Process(Context context, String gender, int height, float weight, String frontimage, String sideimage, final CallBack callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gender", "male");
            jsonObject.put("height", height);
            jsonObject.put("weight", weight);
            jsonObject.put("front_image", frontimage);
            jsonObject.put("side_image", sideimage);

        } catch (Exception var20) {
            var20.printStackTrace();
        }
        if (jsonObject.length() > 0) {
            String api = "https://saia.3dlook.me/api/v2/persons/?measurements_type=all";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(1, api, jsonObject, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    try {
                        if(childflag==1){
                            String res = response.getString("task_set_url");
                            editor.putString("geturlchild", res);
                            editor.apply();
                            callBack.onSuccess(res);
                        }else {
                            String res = response.getString("task_set_url");
                            editor.putString("geturl", res);
                            editor.apply();
                            callBack.onSuccess(res);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                    // callBack.onError(error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    super.getHeaders();
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "APIKey a81c478a60cff15d5b60b32d03158965f4dd9b0b");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(300000, 0, 1.0F));
            Volley.newRequestQueue(context).add(jsonObjReq);
        }
    }

}