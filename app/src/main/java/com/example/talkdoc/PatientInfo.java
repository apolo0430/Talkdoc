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
    private int score;

    public PatientInfo(String name, String address, String email, String birth, String phone, Bitmap image, String number) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
        this.image = image;
        this.number = number;
    }

    // Parcelable 구현
    protected PatientInfo(Parcel in)
    {
        name = in.readString();
        number = in.readString();
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
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(birth);
        dest.writeString(phone);

        // 이미지를 byte 배열로 변환하여 Parcel에 쓰기
        /*if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            dest.writeByteArray(byteArray);
        }
        else {
            dest.writeByteArray(null);
        }*/

        dest.writeString(number);
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
