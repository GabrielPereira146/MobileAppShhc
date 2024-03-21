package br.unesp.rc.MobileDashboard.ui.Doctor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DoctorViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DoctorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}