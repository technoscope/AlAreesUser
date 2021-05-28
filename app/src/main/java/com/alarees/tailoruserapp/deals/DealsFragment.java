package com.alarees.tailoruserapp.deals;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.TitledFragment;

public class DealsFragment extends Fragment implements TitledFragment {

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
    public String getTitle() {
        return "Deals";
    }
}