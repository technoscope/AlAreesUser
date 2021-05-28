package com.alarees.tailoruserapp.unstitched.pocket;



import com.alarees.tailoruserapp.unstitched.model.TypeModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PocketViewModel extends ViewModel {

    MutableLiveData<TypeModel> livemodel;
    public PocketViewModel() {
        livemodel=new MutableLiveData<>();
    }

    public MutableLiveData<TypeModel> getLivemodel() {
        return livemodel;
    }

    public void setLivemodel(TypeModel models) {
        this.livemodel.setValue(models);
    }

}
