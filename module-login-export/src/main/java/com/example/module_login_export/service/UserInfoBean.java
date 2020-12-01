package com.example.module_login_export.service;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfoBean implements Parcelable {
    private String name;
    private String desc;

    public UserInfoBean(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    protected UserInfoBean(Parcel in) {
        name = in.readString();
        desc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel in) {
            return new UserInfoBean(in);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }


}
