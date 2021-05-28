package com.alarees.tailoruserapp.unstitched.placket;


import com.alarees.tailoruserapp.unstitched.model.TypeModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlacketViewModel extends ViewModel {

    MutableLiveData<TypeModel> livemodel;
    public PlacketViewModel() {
        livemodel=new MutableLiveData<>();
    }

    public MutableLiveData<TypeModel> getLivemodel() {
        return livemodel;
    }

    public void setLivemodel(TypeModel models) {
        this.livemodel.setValue(models);
    }

}
