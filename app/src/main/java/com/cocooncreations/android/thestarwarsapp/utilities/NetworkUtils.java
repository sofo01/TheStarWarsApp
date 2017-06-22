package com.cocooncreations.android.thestarwarsapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the SWAPI servers
 */
public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /**
     * Build the URL used to talk to the SWAPI server.
     *
     * @param preference
     *              1 - for movies
     *              2 - for characters
     * @param character_id
     *              0 - for no characters
     * @return The URL to use to query the SWAPI server
     */
    public static URL buildUrl(int preference, int character_id) {
        Uri.Builder builder = new Uri.Builder();
        switch (preference) {
            case 1:
                builder.scheme("http")
                        .authority("swapi.co")
                        .appendPath("api")
                        .appendPath("films");
                builder.build();
                break;
            case 2:
                builder.scheme("http")
                        .authority("swapi.co")
                        .appendPath("api")
                        .appendPath("people")
                        .appendPath(String.valueOf(character_id));
                builder.build();
                break;
        }

        URL url = null;

        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Build URL " + url);

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The content of the HTTP session.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
