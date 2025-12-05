package com.example.movierecommendation;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner genreSpinner;
    private ListView recommendationsListView;
    private TextView movieDetailsText;
    private TextView recommendationsTitle;

    // Hardcoded movie data
    private List<Movie> allMovies;
    private List<Movie> filteredMovies;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeMovieData();
        setupSpinner();
        setupListViewClickListener();
    }

    private void initializeViews() {
        genreSpinner = findViewById(R.id.genreSpinner);
        recommendationsListView = findViewById(R.id.recommendationsListView);
        movieDetailsText = findViewById(R.id.movieDetailsText);
        recommendationsTitle = findViewById(R.id.recommendationsTitle);
    }

    private void initializeMovieData() {
        allMovies = Arrays.asList(
                new Movie("The Dark Knight", "Action", "2008", "8.9/10", "A vigilante battles the Joker in Gotham City"),
                new Movie("John Wick", "Action", "2014", "7.4/10", "An ex-hitman comes out of retirement to track down the gangsters that killed his dog"),
                new Movie("Mad Max: Fury Road", "Action", "2015", "8.1/10", "In a post-apocalyptic wasteland, a woman rebels against a tyrannical ruler"),
                new Movie("The Godfather", "Drama", "1972", "9.2/10", "The aging patriarch of an organized crime dynasty transfers control to his reluctant son"),
                new Movie("Forrest Gump", "Drama", "1994", "8.8/10", "The presidencies of Kennedy and Johnson through the eyes of an Alabama man"),
                new Movie("The Shawshank Redemption", "Drama", "1994", "9.3/10", "Two imprisoned men bond over a number of years, finding solace and eventual redemption"),
                new Movie("Inception", "Sci-Fi", "2010", "8.8/10", "A thief who steals corporate secrets through dream-sharing technology"),
                new Movie("The Matrix", "Sci-Fi", "1999", "8.7/10", "A computer hacker learns from mysterious rebels about the true nature of his reality"),
                new Movie("Interstellar", "Sci-Fi", "2014", "8.6/10", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival"),
                new Movie("Pulp Fiction", "Crime", "1994", "8.9/10", "The lives of two mob hitmen, a boxer, a gangster and his wife intertwine"),
                new Movie("Goodfellas", "Crime", "1990", "8.7/10", "The story of Henry Hill and his life in the mob"),
                new Movie("The Departed", "Crime", "2006", "8.5/10", "An undercover cop and a mole in the police attempt to identify each other"),
                new Movie("The Conjuring", "Horror", "2013", "7.5/10", "Paranormal investigators Ed and Lorraine Warren work to help a family terrorized by a dark presence"),
                new Movie("Get Out", "Horror", "2017", "7.7/10", "A young African-American visits his white girlfriend's parents for the weekend"),
                new Movie("La La Land", "Romance", "2016", "8.0/10", "While navigating their careers in Los Angeles, a pianist and an actress fall in love"),
                new Movie("The Notebook", "Romance", "2004", "7.8/10", "A poor yet passionate young man falls in love with a rich young woman")
        );

        filteredMovies = new ArrayList<>();
    }

    private void setupSpinner() {
        // Genre options
        String[] genres = {"Select Genre", "Action", "Drama", "Sci-Fi", "Crime", "Horror", "Romance", "All Genres"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, genres);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(spinnerAdapter);

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGenre = genres[position];
                if (position > 0) { // Not "Select Genre"
                    filterAndShowMovies(selectedGenre);
                } else {
                    clearRecommendations();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                clearRecommendations();
            }
        });
    }

    private void setupListViewClickListener() {
        recommendationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < filteredMovies.size()) {
                    Movie selectedMovie = filteredMovies.get(position);
                    showMovieDetails(selectedMovie);
                }
            }
        });
    }

    private void filterAndShowMovies(String selectedGenre) {
        filteredMovies.clear();

        if (selectedGenre.equals("All Genres")) {
            filteredMovies.addAll(allMovies);
        } else {
            for (Movie movie : allMovies) {
                if (movie.getGenre().equals(selectedGenre)) {
                    filteredMovies.add(movie);
                }
            }
        }

        showFilteredRecommendations();

        // Show message if no movies found
        if (filteredMovies.isEmpty()) {
            Toast.makeText(this, "No movies found for " + selectedGenre + " genre", Toast.LENGTH_SHORT).show();
        } else {
            String message = "Found " + filteredMovies.size() + " movies in " + selectedGenre;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showFilteredRecommendations() {
        List<String> movieDisplayList = new ArrayList<>();
        for (Movie movie : filteredMovies) {
            String displayText = movie.getTitle() + " (" + movie.getYear() + ") - " + movie.getRating();
            movieDisplayList.add(displayText);
        }

        listAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, movieDisplayList);
        recommendationsListView.setAdapter(listAdapter);

        // Update recommendations title
        if (!filteredMovies.isEmpty()) {
            String selectedGenre = (String) genreSpinner.getSelectedItem();
            recommendationsTitle.setText("Recommended " + selectedGenre + " Movies (" + filteredMovies.size() + "):");
        }
    }

    private void showMovieDetails(Movie movie) {
        String details = "🎬 " + movie.getTitle() +
                "\n\n📅 Year: " + movie.getYear() +
                "\n🎭 Genre: " + movie.getGenre() +
                "\n⭐ Rating: " + movie.getRating() +
                "\n\n📖 Plot: " + movie.getDescription();

        movieDetailsText.setText(details);
    }

    private void clearRecommendations() {
        filteredMovies.clear();
        recommendationsListView.setAdapter(null);
        recommendationsTitle.setText("Recommended Movies:");
        movieDetailsText.setText("Select a genre to see movie recommendations");
    }

    // Movie class to hold movie data
    private static class Movie {
        private String title;
        private String genre;
        private String year;
        private String rating;
        private String description;

        public Movie(String title, String genre, String year, String rating, String description) {
            this.title = title;
            this.genre = genre;
            this.year = year;
            this.rating = rating;
            this.description = description;
        }

        public String getTitle() { return title; }
        public String getGenre() { return genre; }
        public String getYear() { return year; }
        public String getRating() { return rating; }
        public String getDescription() { return description; }
    }
}