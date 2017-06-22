package com.cocooncreations.android.thestarwarsapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cocooncreations.android.thestarwarsapp.R;
import com.cocooncreations.android.thestarwarsapp.models.Character;
import com.cocooncreations.android.thestarwarsapp.models.Movie;

public class CharacterFragment extends Fragment {

    private static final String ARG_CHARACTER = "character";
    private static final String ARG_MOVIE = "movie";

    private Character mCharacter;
    private Movie mMovie;

    private TextView mHeight;
    private TextView mMass;
    private TextView mHairColor;
    private TextView mSkinColor;
    private TextView mEyeColor;
    private TextView mBirthYear;
    private TextView mGender;


    /**
     * Create a new instance of the ChracterFragment
     * @param character the character passed as an extra
     * @param movie the movie passed as na extra
     * @return a new CharacterFragment instance
     */
    public static CharacterFragment newInstance(Character character, Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHARACTER, character);
        args.putSerializable(ARG_MOVIE, movie);

        CharacterFragment fragment = new CharacterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * The onCreate method will retrieve the extra arguments
     * and instantiate our movie and character object
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Character character = (Character) getArguments().getSerializable(ARG_CHARACTER);
        Movie movie = (Movie) getArguments().getSerializable(ARG_MOVIE);
        mCharacter = character;
        mMovie = movie;
    }

    /**
     * Inflates the container view as well as all child view objects
     * @param inflater
     * @param container
     * @param savedInstance
     * @return The inflated root View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance){

        View view = inflater.inflate(R.layout.character_fragment, container, false);

        mHeight = (TextView) view.findViewById(R.id.height);
        mHeight.setText("Height: " + mCharacter.getHeight());

        mMass = (TextView) view.findViewById(R.id.mass);
        mMass.setText("Mass: " + mCharacter.getMass());

        mHairColor = (TextView) view.findViewById(R.id.hair_color);
        mHairColor.setText("Hair Color: " + mCharacter.getHairColor());

        mSkinColor = (TextView) view.findViewById(R.id.skin_color);
        mSkinColor.setText("Skin Color: " + mCharacter.getSkinColor());

        mEyeColor = (TextView) view.findViewById(R.id.eye_color);
        mEyeColor.setText("Eye Color: " + mCharacter.getEyeColor());

        mBirthYear = (TextView) view.findViewById(R.id.birth_year);
        mBirthYear.setText("Birth Year: " + mCharacter.getBirthYear());

        mGender = (TextView) view.findViewById(R.id.gender);
        mGender.setText("Gender: " + mCharacter.getGender());

        returnResult();

        return view;
    }

    /**
     * Send result back to parent activity
     */
    public void returnResult() {
        Intent data = new Intent();
        data.putExtra(ARG_MOVIE, mMovie);
        getActivity().setResult(Activity.RESULT_OK, data);
    }
}
