package com.example.talkdok.ui.checkup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CheckupViewModel extends ViewModel
{
    private final MutableLiveData<String> mText;

    public CheckupViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("This is checkup fragment");
    }

    public LiveData<String> getText()
    {
        return mText;
    }
}