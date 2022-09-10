package com.rpcframework.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Info implements Parcelable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    private String value;
    private int other;

    public Info() {}

    protected Info(Parcel in) {
        name = in.readString();
        value = in.readString();
        other = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
        dest.writeInt(other);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    @Override
    public String toString() {
        return "Info{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", other=" + other +
                '}';
    }
}
