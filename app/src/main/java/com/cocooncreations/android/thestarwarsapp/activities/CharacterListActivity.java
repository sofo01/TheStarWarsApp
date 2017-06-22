package com.cocooncreations.android.thestarwarsapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.cocooncreations.android.thestarwarsapp.fragments.CharacterListFragment;
import com.cocooncreations.android.thestarwarsapp.models.Movie;

public class CharacterListActivity extends SingleFragmentActivity {

    private static final String EXTRA_MOVIE = "com_cocooncreations.adroid.thestarwardapp.movie";

    /**
     * Creates a new Intent
     * The method serves ecapsulation purposes
     * @param packageContext
     * @param movie
     * @return The new Intent
     */
    public static Intent newIntent(Context packageContext, Movie movie) {
        Intent intent = new Intent(packageContext, CharacterListActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    /**
     * Overrides The abstract method from SingleFragmentActivity
     * @return a new CharacterListFragment object
     */
    @Override
    protected Fragment createFragment() {
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        setTitle(movie.getName());
        return CharacterListFragment.newInstance(movie);
    }
}