package com.alarees.tailoruserapp.measurement.measurementlist;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.measurement.HowToFragment;

public class ChildInputFragment extends Fragment {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.input_child_height)
    TextView height;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.input_child_weight)
    TextView weight;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.input_child_name)
    TextView name;
    @BindView(R.id.id_okay_btn)
    Button okay_btn;
    @BindView(R.id.container)
    LinearLayout container;

    public static int childheight, childweight;
    public static String childname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
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
                container.setBackgroundResource(R.drawable.background_white);
                break;
        }
        okay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty()) {
                    childname = name.getText().toString();
                    if (!height.getText().toString().isEmpty()) {
                        childheight = Integer.parseInt(height.getText().toString());
                        if (!weight.getText().toString().isEmpty()) {
                            childweight = Integer.parseInt(weight.getText().toString());
                            HowToFragment fragment = new HowToFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("child", 1);
                            fragment.setArguments(bundle);
                            loadFragment(fragment);
                        } else {
                            weight.setError("empty weight");
                        }
                    } else {
                        height.setError("empty height");
                    }
                } else {
                    name.setError("empty name");
                }

            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack("MeasurementListFragment");
        transaction.commit();
    }

}