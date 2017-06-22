package com.cocooncreations.android.thestarwarsapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cocooncreations.android.thestarwarsapp.R;
import com.cocooncreations.android.thestarwarsapp.activities.CharacterActivity;
import com.cocooncreations.android.thestarwarsapp.activities.CharacterListActivity;
import com.cocooncreations.android.thestarwarsapp.activities.MoviesListActivity;
import com.cocooncreations.android.thestarwarsapp.models.Character;
import com.cocooncreations.android.thestarwarsapp.models.Movie;
import com.cocooncreations.android.thestarwarsapp.utilities.NetworkUtils;
import com.cocooncreations.android.thestarwarsapp.utilities.SWAPIJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment view for the list of characters for each movie
 */
public class CharacterListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Character>>{

    private static final String ARG_MOVIE = "movie";

    private Movie mMovie;
    private RecyclerView mRecyclerView;
    private CharacterAdapter mCharacterAdapter;
    private TextView mErrorMessageIndicator;
    private ProgressBar mLoadingIndicator;
    public static final int CHARACTERS_LOADER_ID = 0;
    private static final int REQUEST_CODE_MOVIE = 0;

    /**
     * Create a new instance of the CharacterListFragment
     *
     * @param movie the movie passed as an extra
     * @return a new fragment instance
     */
    public static CharacterListFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);

        CharacterListFragment fragment = new CharacterListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * The onCreate method will retrieve the extra arguments and instantiate our movie object
     * @param savedInstaceState
     */
    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        Movie movie = (Movie) getArguments().getSerializable(ARG_MOVIE);
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
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);



        mErrorMessageIndicator = (TextView) view.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /**
         * This ID will uniquely identify the Loader. We can use it, for example, to get a handle
         * on our Loader at a later point in time through the support LoaderManager.
         */
        int loaderId = CHARACTERS_LOADER_ID;
        /**
         * We have implemented the LoaderCallbacks interface with the type of
         * List charaacter. (implements LoaderCallbacks<List<Character>>) The variable callback is passed
         * to the call to initLoader below. This means that whenever the loaderManager has
         * something to notify us of, it will do so through this callback.
         */
        LoaderManager.LoaderCallbacks<List<Character>> callbacks = CharacterListFragment.this;
        /**
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getLoaderManager().initLoader(loaderId, null, callbacks);

        return  view;
    }

    /**
     * A failed attempt to receive results back from the child activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_MOVIE) {
            if (data == null) {
                return;
            }
            mMovie = (Movie) data.getSerializableExtra(ARG_MOVIE);
        }
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible
     */
    private void showCharactersDataView(){
        mErrorMessageIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the
     * movie view
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible
     */
    private void showErrorMessage(){
        mErrorMessageIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Once data received set the Recycler's View adapter
     * @param data
     */
    private void updateUI(List<Character> data){
        mCharacterAdapter = new CharacterAdapter(data);
        mRecyclerView.setAdapter(mCharacterAdapter);
    }

    /**
     * Initialize and return a new Loader for the given ID.
     * @param id The ID whose Loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<Character>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Character>>(this.getContext()) {

            List<Character> mCharacters = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */
            @Override
            protected void onStartLoading(){
                if (mCharacters != null) {
                    deliverResult(mCharacters);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             * in the background
             *
             * @return Movie data as a List of Character objects.
             *          null if an error occurs
             */
            @Override
            public List<Character> loadInBackground() {
                List<Character> characters = new ArrayList<>();
                for (int i = 0; i < mMovie.getCharacters_id().length; i++) {
                    URL characterRequestUrl = NetworkUtils.
                            buildUrl(2, mMovie.getCharacters_id()[i]);
                    String jsonCharacterResponse = null;
                    try {
                        jsonCharacterResponse = NetworkUtils.
                                getResponseFromHttpUrl(characterRequestUrl);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        characters.add(SWAPIJsonUtils.
                                getSimpleCharacterFromJson(jsonCharacterResponse));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return characters;
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            @Override
            public void deliverResult(List<Character> data) {
                mCharacters = data;
                super.deliverResult(mCharacters);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<List<Character>> loader, List<Character> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        updateUI(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showCharactersDataView();
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable. The application should at this point
     * remove any references it has to the Loader's data.
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<Character>> loader) {}


    private class CharacterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private Character mCharacter;

        public CharacterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_character, parent, false));
            itemView.setOnClickListener(this);
            mTextView = (TextView) itemView.findViewById(R.id.character_tv);
        }

        /**
         * Bind the view's data
         * @param character
         */
        public void bind(Character character) {
            mCharacter = character;
            mTextView.setText(mCharacter.getName());
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            Intent intent = CharacterActivity.newIntent(getActivity(), mCharacter, mMovie);
            startActivityForResult(intent, REQUEST_CODE_MOVIE);
        }
    }


    private class CharacterAdapter extends RecyclerView.Adapter<CharacterHolder> {

        private List<Character> mCharacters;

        private CharacterAdapter(List<Character> characters) {
            mCharacters = characters;
        }

        /**
         * This gets called when each new ViewHolder is created. This happens when the RecyclerView
         * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
         *
         * @param parent The ViewGroup that these ViewHolders are contained within.
         * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
         *                  can use this viewType integer to provide a different layout. See
         * @return A new CharacterAdapterViewHolder that holds the View for each list item
         */
        public CharacterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CharacterHolder(layoutInflater, parent);
        }

        /**
         * OnBindViewHolder is called by the RecyclerView to display the data at the specified
         * position. In this method, we update the contents of the ViewHolder to display the movie
         * details for this particular position, using the "position" argument that is conveniently
         * passed into us.
         *
         * @param holder The ViewHolder which should be updated to represent the
         *                                  contents of the item at the given position in the data set.
         * @param position                  The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(CharacterHolder holder, int position) {
            Character character = mCharacters.get(position);
            holder.bind(character);
        }

        /**
         * This method simply returns the number of items to display. It is used behind the scenes
         * to help layout our Views and for animations.
         *
         * @return The number of items available in our movie list
         */
        @Override
        public int getItemCount() {
            return mCharacters.size();
        }
    }
}
