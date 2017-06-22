package com.cocooncreations.android.thestarwarsapp.activities;

import android.support.v4.app.Fragment;
import com.cocooncreations.android.thestarwarsapp.fragments.MovieListFragment;

public class MoviesListActivity extends SingleFragmentActivity {

    /**
     * Overrides The abstract method from SingleFragmentActivity
     * @return a new MovieListFragment object
     */
    @Override
    protected Fragment createFragment() {
        return new MovieListFragment();
    }
}