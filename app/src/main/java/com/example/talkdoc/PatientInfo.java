package com.example.talkdoc;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientInfo implements Parcelable
{
    private String name;
    private String number;
    private int score;

    public PatientInfo(String name, String number)
    {
        this.name = name;
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public String getNumber()
    {
        return number;
    }

    // Parcelable 구현
    protected PatientInfo(Parcel in) {
        name = in.readString();
        number = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PatientInfo> CREATOR = new Creator<PatientInfo>() {
        @Override
        public PatientInfo createFromParcel(Parcel in) {
            return new PatientInfo(in);
        }

        @Override
        public PatientInfo[] newArray(int size) {
            return new PatientInfo[size];
        }
    };
}
