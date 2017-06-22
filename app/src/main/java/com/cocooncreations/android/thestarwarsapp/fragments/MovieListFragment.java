package com.cocooncreations.android.thestarwarsapp.fragments;

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
import com.cocooncreations.android.thestarwarsapp.activities.CharacterListActivity;
import com.cocooncreations.android.thestarwarsapp.models.Movie;
import com.cocooncreations.android.thestarwarsapp.utilities.NetworkUtils;
import com.cocooncreations.android.thestarwarsapp.utilities.SWAPIJsonUtils;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * A fragment view for the list of movies
 */
public class MovieListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private RecyclerView mMovieRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    public static final int MOVIES_LOADER_ID = 0;

    /**
     * The onCreate method will retrieve the extra arguments and instantiate our movie object
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the container view as well as all child view objects
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return The inflated root view
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mErrorMessageDisplay = (TextView) view.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);

        mMovieRecyclerView = (RecyclerView) view.findViewById(R.id.movie_recycler_view);
        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /**
         * This ID will uniquely identify the Loader. We can use it, for example, to get a handle
         * on our Loader at a later point in time through the support LoaderManager.
         */
        int loaderId = MOVIES_LOADER_ID;
        /**
         * We have implemented the LoaderCallbacks interface with the type of
         * List movie. (implements LoaderCallbacks<List<Movie>>) The variable callback is passed
         * to the call to initLoader below. This means that whenever the loaderManager has
         * something to notify us of, it will do so through this callback.
         */
        LoaderManager.LoaderCallbacks<List<Movie>> callbacks = MovieListFragment.this;
        /**
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getLoaderManager().initLoader(loaderId,null,callbacks);

        return view;
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible
     */
    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMovieRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the
     * movie view
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible
     */
    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mMovieRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Once data received set the Recycler's View adapter
     * @param data
     */
    private void updateUI(List<Movie> data){
        List<Movie> movies = data;
        mMovieAdapter = new MovieAdapter(movies);
        mMovieRecyclerView.setAdapter(mMovieAdapter);
    }

    /**
     * Initialize and return a new Loader for the given ID.
     * @param id The ID whose Loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this.getContext()) {

            List<Movie> mMovies = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */
            @Override
            protected void onStartLoading(){
                if (mMovies != null) {
                    deliverResult(mMovies);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             * in the background
             *
             * @return Movie data as a List of Movie objects.
             *          null if an error occurs
             */
            @Override
            public List<Movie> loadInBackground() {
                URL moviesRequestUrl = NetworkUtils.buildUrl(1, 0);
                String jsonMovieResponse = null;
                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    List<Movie> simpleJsonMovieData = SWAPIJsonUtils.
                            getSimpleMoviesFromJson(jsonMovieResponse);
                    return simpleJsonMovieData;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            @Override
            public void deliverResult(List<Movie> data) {
                mMovies = data;
                super.deliverResult(mMovies);
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
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        updateUI(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showMoviesDataView();
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable. The application should at this point
     * remove any references it has to the Loader's data.
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {}

    private class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mButton;
        private Movie mMovie;

        public MovieHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_movie, parent, false));
            itemView.setOnClickListener(this);
            mButton = (TextView) itemView.findViewById(R.id.tv_movie);
        }

        /**
         * Bind the view's data
         * @param movie
         */
        public void bind(Movie movie) {
            mMovie = movie;
            mButton.setText("Episode " + mMovie.getEpisodeId() + " - " + mMovie.getName());
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            Intent intent = CharacterListActivity.newIntent(getActivity(), mMovie);
            startActivity(intent);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private List<Movie> mMovies;

        private MovieAdapter(List<Movie> movies) {
            mMovies = movies;
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
        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MovieHolder(layoutInflater, parent);
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
        public void onBindViewHolder(MovieHolder holder, int position) {
            Movie movie = mMovies.get(position);
            holder.bind(movie);
        }

        /**
         * This method simply returns the number of items to display. It is used behind the scenes
         * to help layout our Views and for animations.
         *
         * @return The number of items available in our movie list
         */
        @Override
        public int getItemCount() {
            return mMovies.size();
        }
    }
}
