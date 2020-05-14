package com.example.astro;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class SharedViewModel extends ViewModel implements Serializable {
    private MutableLiveData<ContentValues> common = new MutableLiveData<ContentValues>();
    private MutableLiveData<ContentValues> present = new MutableLiveData<ContentValues>();
    private MutableLiveData<ContentValues> secondDay = new MutableLiveData<ContentValues>();
    private MutableLiveData<ContentValues> thirdDay = new MutableLiveData<ContentValues>();

    public SharedViewModel() {
    }

    public ContentValues getCommon() {
        return common.getValue();
    }

    public void setCommon(ContentValues contentValues) {
        common.setValue(contentValues);
    }

    public ContentValues getPresent() {
        return present.getValue();
    }

    public void setPresent(ContentValues contentValues) {
        present.setValue(contentValues);
    }

    public ContentValues getSecondDay() {
        return secondDay.getValue();
    }

    public void setSecondDay(ContentValues contentValues) {
        secondDay.setValue(contentValues);
    }

    public ContentValues getThirdDay() {
        return thirdDay.getValue();
    }

    public void setThirdDay(ContentValues contentValues) {
        thirdDay.setValue(contentValues);
    }
}