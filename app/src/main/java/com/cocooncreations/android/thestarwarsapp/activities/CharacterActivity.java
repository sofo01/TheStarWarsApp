package com.cocooncreations.android.thestarwarsapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.cocooncreations.android.thestarwarsapp.fragments.CharacterFragment;
import com.cocooncreations.android.thestarwarsapp.models.Character;
import com.cocooncreations.android.thestarwarsapp.models.Movie;

public class CharacterActivity extends SingleFragmentActivity {

    private static final String EXTRA_CHARACTER =
            "com_cocooncreations.adroid.thestarwardapp.character";
    private static final String EXTRA_MOVIE =
            "com_cocooncreations.adroid.thestarwardapp.movie";

    /**
     * Creates a new Intent
     * The method serves encapsulation purposes
     * @param packageContext
     * @param movie
     * @return The new Intent
     */
    public static Intent newIntent(Context packageContext, Character character, Movie movie) {
        Intent intent = new Intent(packageContext, CharacterActivity.class);
        intent.putExtra(EXTRA_CHARACTER, character);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    /**
     * Overrides The abstract method from SingleFragmentActivity
     * @return a new CharacterFragment object
     */
    @Override
    protected Fragment createFragment() {
        Character character = (Character) getIntent().getSerializableExtra(EXTRA_CHARACTER);
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        setTitle(character.getName());
        return CharacterFragment.newInstance(character, movie);
    }
}