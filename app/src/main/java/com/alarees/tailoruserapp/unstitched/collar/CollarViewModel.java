package com.alarees.tailoruserapp.unstitched.collar;


import com.alarees.tailoruserapp.unstitched.model.TypeModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CollarViewModel extends ViewModel {

    MutableLiveData<TypeModel> livemodel;
    public CollarViewModel() {
        livemodel=new MutableLiveData<>();
    }

    public MutableLiveData<TypeModel> getLivemodel() {
        return livemodel;
    }

    public void setLivemodel(TypeModel models) {
        this.livemodel.setValue(models);
    }

}
