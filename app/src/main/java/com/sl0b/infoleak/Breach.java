package com.sl0b.infoleak;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Breach implements Parcelable {
    private static final String IMG_BASE_URL = "https://haveibeenpwned.com/Content/Images/PwnedLogos/";
    private static final String SVG = "svg";

    @SerializedName("Title")
    private String mTitle;

    @SerializedName("Name")
    private String mName;

    @SerializedName("Domain")
    private String mDomain;

    @SerializedName("BreachDate")
    private String mDate;

    @SerializedName("Description")
    private String mDescription;

    @SerializedName("DataClasses")
    private String[] mDataClasses;

    @SerializedName("LogoType")
    private String mLogoType;

    public Breach(String title, String name, String domain, String date, String description, String[] data, String type) {
        mTitle = title;
        mName = name;
        mDomain = domain;
        mDate = date;
        mDescription = description;
        mDataClasses = data;
        mLogoType = type;
    }

    @NonNull
    public String getTitle() {
        return TextUtils.isEmpty(mTitle) ? "" : mTitle;
    }

    @NonNull
    public String getDomain() {
        return TextUtils.isEmpty(mDomain) ? "" : mDomain;
    }

    @NonNull
    public String getLogoUrl() {
        return TextUtils.isEmpty(mName) ? "" : IMG_BASE_URL + mName + "." + mLogoType;
    }

    @NonNull
    public String getBreachDate() {
        String formatedDate = mDate;
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date oldFormat = curFormater.parse(mDate);
            SimpleDateFormat postFormater = new SimpleDateFormat("yyyy, dd MMMM", Locale.getDefault());
            formatedDate = postFormater.format(oldFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    @NonNull
    public String getDescription() {
        return TextUtils.isEmpty(mDescription) ? "" : mDescription;
    }

    public boolean isLogoAnSvg() {
        return mLogoType.equals(SVG);
    }

    @NonNull
    public String getDataClasses() {
        String leakedDataString = "";
        if (mDataClasses == null) { return leakedDataString; }
        for (int i = 0; i < mDataClasses.length; i++) {
            if (i == mDataClasses.length - 1) {
                leakedDataString += mDataClasses[i];
            } else {
                leakedDataString += mDataClasses[i] + "\n";
            }
        }
        return leakedDataString;
    }

    // Parcelable stuff
    private Breach(Parcel in) {
        this.mTitle = in.readString();
        this.mName = in.readString();
        this.mDomain = in.readString();
        this.mDate = in.readString();
        this.mDescription = in.readString();
        this.mDataClasses = in.createStringArray();
        this.mLogoType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mName);
        dest.writeString(mDomain);
        dest.writeString(mDate);
        dest.writeString(mDescription);
        dest.writeStringArray(mDataClasses);
        dest.writeString(mLogoType);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    static final Parcelable.Creator<Breach> CREATOR = new Parcelable.Creator<Breach>() {

        public Breach createFromParcel(Parcel in) {
            return new Breach(in);
        }

        public Breach[] newArray(int size) {
            return new Breach[size];
        }
    };
}
