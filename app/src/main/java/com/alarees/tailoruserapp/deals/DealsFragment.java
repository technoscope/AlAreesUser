package com.alarees.tailoruserapp.deals;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.TitledFragment;

public class DealsFragment extends Fragment implements TitledFragment {
LinearLayout container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container=view.findViewById(R.id.deals_container);
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
    }

    @Override
    public String getTitle() {
        return "Deals";
    }
}