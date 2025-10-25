package com.midterm.bt3_22_8;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private MutableLiveData<Integer> number;

    public LiveData<Integer> getNumber(){
        if (number == null) {
            number = new MutableLiveData<>();
            number.setValue(0);
        }
        return number;
    }

    public void increaseNumber(){
        number.setValue(number.getValue() + 1);
    }
}
