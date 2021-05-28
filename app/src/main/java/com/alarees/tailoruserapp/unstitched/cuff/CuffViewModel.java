package com.alarees.tailoruserapp.unstitched.cuff;


import com.alarees.tailoruserapp.unstitched.model.TypeModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CuffViewModel extends ViewModel {

    MutableLiveData<TypeModel> livemodel;
    public CuffViewModel() {
        livemodel=new MutableLiveData<>();
    }

    public MutableLiveData<TypeModel> getLivemodel() {
        return livemodel;
    }

    public void setLivemodel(TypeModel models) {
        this.livemodel.setValue(models);
    }

}
