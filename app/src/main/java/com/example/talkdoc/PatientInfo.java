package com.example.talkdoc;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

public class PatientInfo implements Parcelable
{
    private String name;
    private String address;
    private String email;
    private String birth;
    private String phone;
    private Bitmap image;
    private String number;
    private String score;

    public PatientInfo(String name, String address, String email, String birth, String phone, String score, Bitmap image, String number) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
        this.score = score;
        this.image = image;
    }

    // Parcelable 구현
    protected PatientInfo(Parcel in)
    {
        name = in.readString();
        number = in.readString();
        address = in.readString();
        email = in.readString();
        birth = in.readString();
        phone = in.readString();
        score = in.readString();
    }

    public String getName()
    {
        return name;
    }

    public String getAddress()
    {
        return address;
    }

    public String getEmail()
    {
        return email;
    }

    public String getBirth()
    {
        return birth;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getScore()
    {
        return score;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public String getNumber()
    {
        return number;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(birth);
        dest.writeString(phone);
        dest.writeString(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PatientInfo> CREATOR = new Creator<PatientInfo>()
    {
        @Override
        public PatientInfo createFromParcel(Parcel in)
        {
            return new PatientInfo(in);
        }

        @Override
        public PatientInfo[] newArray(int size)
        {
            return new PatientInfo[size];
        }
    };
}
