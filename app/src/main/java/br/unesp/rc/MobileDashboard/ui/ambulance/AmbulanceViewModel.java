package br.unesp.rc.MobileDashboard.ui.ambulance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AmbulanceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AmbulanceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}