package com.cocooncreations.android.thestarwarsapp.models;

import java.io.Serializable;

/**
 * Movie model class with appropriate getter and setter methods
 */
public class Movie implements Serializable{
    private String mName;
    private int mEpisodeId;
    private int[] mCharacters_id;

    public Movie(String name, int episodeId,int[] characters_id) {
        mName = name;
        mEpisodeId = episodeId;
        mCharacters_id = characters_id;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getEpisodeId() {
        return mEpisodeId;
    }

    public void setEpisodeId(int episodeId) {
        mEpisodeId = episodeId;
    }

    public int[] getCharacters_id() {
        return mCharacters_id;
    }

    public void setCharacters_id(int[] characters_id) {
        mCharacters_id = characters_id;
    }
}
