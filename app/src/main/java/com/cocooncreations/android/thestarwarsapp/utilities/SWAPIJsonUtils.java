package com.cocooncreations.android.thestarwarsapp.utilities;

import com.cocooncreations.android.thestarwarsapp.models.Movie;
import com.cocooncreations.android.thestarwarsapp.models.Character;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions to handle SWAPIDB JSON data
 */
public class SWAPIJsonUtils {

    /**
     * This method parses JSON from a web response and returns a List of Movies
     * @param moviesJsonStr JSON response form server
     * @return List of Movies describing movie data
     * @throws JSONException If JSON data cannot be properly parsed.
     */
    public static List<Movie> getSimpleMoviesFromJson(String moviesJsonStr) throws JSONException {
        JSONObject movieJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = movieJson.getJSONArray("results");
        List<Movie> parseMovieData = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            List<Integer> movieCharactersId = new ArrayList<>();
            JSONObject movie = moviesArray.getJSONObject(i);

            String name = movie.getString("title");
            int episode_id = movie.getInt("episode_id");

            JSONArray characters = movie.getJSONArray("characters");
            for (int x = 0; x < characters.length(); x++)  {
                int characterId = getCharacterId(characters.getString(x));
                movieCharactersId.add(characterId);
            }

            int[] movieCharacter = new int[movieCharactersId.size()];

            for(int x = 0; x < movieCharactersId.size(); x++){
                movieCharacter[x] = movieCharactersId.get(x);
            }

            Movie movie_ = new Movie(name, episode_id, movieCharacter);
            parseMovieData.add(movie_);
        }

        List<Movie> sortedMovies = new ArrayList<>();

        for (int i = 1; i <= parseMovieData.size(); i++) {
            for(Movie movie : parseMovieData) {
                if (movie.getEpisodeId() == i) {
                    sortedMovies.add(movie);
                }
            }
        }

        return sortedMovies;
    }

    /**
     * This method parses JSON from a web response and returns a Character object
     * @param characterJsonStr
     * @return A Character object
     * @throws JSONException
     */
    public static Character getSimpleCharacterFromJson(String characterJsonStr) throws
            JSONException {
        JSONObject characterJson = new JSONObject(characterJsonStr);
        String name = characterJson.getString("name");
        String height = characterJson.getString("height");
        String mass = characterJson.getString("mass");
        String hairColor = characterJson.getString("hair_color");
        String skinColor = characterJson.getString("skin_color");
        String eyeColor = characterJson.getString("eye_color");
        String birthYear = characterJson.getString("birth_year");
        String gender = characterJson.getString("gender");

        Character character = new Character(
                name,height,mass,hairColor,skinColor,eyeColor,birthYear,gender);

        return character;
    }

    /**
     * Substring the character URL
     * @param url The character URL
     * @return The character id as an integer
     */
    public static int getCharacterId(String url) {
        int length = url.length();
        String id = url.substring(27,(length-1));
        return Integer.parseInt(id);
    }
}
