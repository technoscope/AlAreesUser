package com.alarees.tailoruserapp.measurement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ms_android_sdk.CallBack;
import com.alarees.tailoruserapp.TitledFragment;
import com.alarees.tailoruserapp.account.SigninFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParentMeasurementDetail extends Fragment implements TitledFragment {
    DatabaseReference mRef;
    SharedPreferences sharedpreferences;
    @BindView(R.id.id_upperchestgirth)
    TextView upperchestgirth;
    @BindView(R.id.id_upperbicepgirth)
    TextView upperbicepgirth;
    @BindView(R.id.id_neck)
    TextView neck;
    @BindView(R.id.id_shoulders)
    TextView shoulders;
    @BindView(R.id.waist)
    TextView waist;
    @BindView(R.id.id_underarmlength)
    TextView underarmlength;
    @BindView(R.id.id_back_shoulder_width)
    TextView backshoulderwidth;
    @BindView(R.id.id_upperhipheight)
    TextView upperhipheight;
    @BindView(R.id.showmore_detials)
    LinearLayout showmore;
    private FirebaseAuth mAuth;
    @BindView(R.id.view__hide)
    LinearLayout epandableview;
    //for side length
    @BindView(R.id.body_area_percentage)
    TextView body_area_percentage;
    @BindView(R.id.side_upper_hip_level_to_knee)
    TextView side_upper_hip_level_to_knee;
    @BindView(R.id.side_neck_point_to_upper_hip)
    TextView side_neck_point_to_upper_hip;
    @BindView(R.id.neck_to_chest)
    TextView neck_to_chest;
    @BindView(R.id.chest_to_waist)
    TextView chest_to_waist;
    @BindView(R.id.waist_to_ankle)
    TextView waist_to_ankle;
    @BindView(R.id.shoulders_to_knees)
    TextView shoulders_to_knees;
    //for front length
    @BindView(R.id.body_height)
    TextView body_height;
    @BindView(R.id.sleeve_length)
    TextView sleeve_length;
    @BindView(R.id.underarm_length)
    TextView  underarm_length;
    @BindView(R.id.shoulder_length)
    TextView  shoulder_length;
    @BindView(R.id.upper_arm_length)
    TextView upper_arm_length;
    @BindView(R.id.lower_arm_length)
    TextView  lower_arm_length;
    @BindView(R.id.neck_length)
    TextView  neck_length;
    @BindView(R.id.rise)
    TextView  rise;
    @BindView(R.id.id_progress_wait)
    ProgressBar progressBar;
    @BindView(R.id.scrollcontainer)
    ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent_measurement_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            progressBar.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            sharedpreferences = getActivity().getSharedPreferences("3dlookUserurl", Context.MODE_PRIVATE);
            String url = sharedpreferences.getString("geturl", "0");
            if (!url.equals("0")) {
                showmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                        if (mRef != null) {
                            mRef.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        epandableview.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);

                                    } else {
                                        Toast.makeText(getContext(), "Please complete your first order", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Please complete your first order", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                get_measurement(new CallBack() {
                    @Override
                    public void onSuccess(String s) {
                        //Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(VolleyError volleyError) {
                        Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new HowToFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else {
            Toast.makeText(getContext(), "First create your account", Toast.LENGTH_SHORT).show();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new SigninFragment());
            ft.addToBackStack("SigninFragment");
            ft.commit();
        }
    }

    public void get_measurement(final CallBack callBack) {
        String api;
        JSONObject jsonObject = new JSONObject();
        if (ProgressFragment.getURL != null) {
            api = ProgressFragment.getURL;//+sharedpreferences.getString("userid","0");

        } else {
            api = sharedpreferences.getString("geturl", "0");

        }
        //api="https://saia.3dlook.me/api/v2/queue/1319aaf0-f14b-4608-a5cf-3f51a07cf7fe/";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, api, jsonObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                try {
                    scrollView.setVisibility(View.VISIBLE);
                    Front_params front_params = gson.fromJson(response.getJSONObject("front_params").toString(), Front_params.class);
                    body_height.setText(front_params.getBody_height()+" cm");
                    sleeve_length.setText(front_params.getSleeve_length()+" cm");
                    underarm_length.setText(front_params.getUnderarm_length()+ " cm");
                    shoulder_length.setText(front_params.getShoulder_length()+" cm");
                    upper_arm_length.setText(front_params.getUnderarm_length()+" cm");
                    lower_arm_length.setText(front_params.getLower_arm_length()+" cm");
                    neck_length.setText(front_params.getNeck_length()+" cm");
                    rise.setText(front_params.getRise()+" cm");
                    mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                    mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MeasurementParams").child("FrontParams").setValue(front_params);
                    Volume_params volume_params = gson.fromJson(response.getJSONObject("volume_params").toString(), Volume_params.class);
                    mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                    mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MeasurementParams").child("VolumeParams").setValue(volume_params);
                    upperchestgirth.setText(String.valueOf(volume_params.getUpper_chest_girth()) + " cm");
                    Side_params side_params = gson.fromJson(response.getJSONObject("side_params").toString(), Side_params.class);
                    body_area_percentage.setText(side_params.getBody_area_percentage()+" cm");
                    side_upper_hip_level_to_knee.setText(side_params.getSide_upper_hip_level_to_knee()+" cm");
                    side_neck_point_to_upper_hip.setText(side_params.getSide_neck_point_to_upper_hip()+" cm");
                    neck_to_chest.setText(side_params.getNeck_to_chest()+" cm");
                    chest_to_waist.setText(side_params.getChest_to_waist()+" cm");
                    waist_to_ankle.setText(side_params.getWaist_to_ankle()+" cm");
                    shoulders_to_knees.setText(side_params.getShoulders_to_knees()+" cm");

                    upperbicepgirth.setText(String.valueOf(volume_params.getUpper_bicep_girth()) + " cm");
                    neck.setText(String.valueOf(volume_params.getNeck()) + " cm");
                    shoulders.setText(String.valueOf(front_params.getShoulders()) + " cm");
                    waist.setText(String.valueOf(front_params.getWaist()) + " cm");
                    underarmlength.setText(String.valueOf(front_params.getUnderarm_length()) + " cm");
                    backshoulderwidth.setText(String.valueOf(front_params.getBack_shoulder_width()) + " cm");
                    upperhipheight.setText(String.valueOf(front_params.getUpper_hip_height()) + " cm");
                    mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                    mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MeasurementParams").child("SideParams").setValue(side_params);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "APIKey a81c478a60cff15d5b60b32d03158965f4dd9b0b");
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(300000, 0, 1.0F));
        Volley.newRequestQueue(getContext()).add(jsonObjReq);

    }

    @Override
    public String getTitle() {
        return "My Measurement";
    }
}