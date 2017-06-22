package com.cocooncreations.android.thestarwarsapp.models;

import java.io.Serializable;

/**
 * Character model class with appropriate getter and setter methods
 */
public class Character implements Serializable {
    private String mName;
    private String mHeight;
    private String mMass;
    private String mHairColor;
    private String mSkinColor;
    private String mEyeColor;
    private String mBirthYear;
    private String mGender;

    public Character(String name, String height, String mass, String hairColor,
                     String skinColor, String eyeColor, String birthYear, String gender) {
        mName = name;
        mHeight = height;
        mMass = mass;
        mHairColor = hairColor;
        mSkinColor = skinColor;
        mEyeColor = eyeColor;
        mBirthYear = birthYear;
        mGender = gender;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getHeight() {
        return mHeight;
    }

    public void setHeight(String height) {
        mHeight = height;
    }

    public String getMass() {
        return mMass;
    }

    public void setMass(String mass) {
        mMass = mass;
    }

    public String getHairColor() {
        return mHairColor;
    }

    public void setHairColor(String hairColor) {
        mHairColor = hairColor;
    }

    public String getSkinColor() {
        return mSkinColor;
    }

    public void setSkinColor(String skinColor) {
        mSkinColor = skinColor;
    }

    public String getEyeColor() {
        return mEyeColor;
    }

    public void setEyeColor(String eyeColor) {
        mEyeColor = eyeColor;
    }

    public String getBirthYear() {
        return mBirthYear;
    }

    public void setBirthYear(String birthYear) {
        mBirthYear = birthYear;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }
}
