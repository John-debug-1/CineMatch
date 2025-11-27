package org.MY_APP.User_Interface;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.MY_APP.Model.Movie;
import org.MY_APP.Services.MovieService;
import javafx.scene.layout.FlowPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CineMatchController {

    //-------- FXML references --------
    @FXML
    private TextField searchField;

    @FXML
    private Label titleLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private TextArea overviewArea;

    @FXML
    private ScrollPane movieScrollPane;

    @FXML
    private javafx.scene.layout.FlowPane movieFlowPane;

    // -------- Service injected from CineMatchApp --------
    private MovieService movieService;


    /**
     Setter που δέχεται το MovieService από το κυρίως App.
     Χρειάζεται για να μπορεί ο controller να κάνει αναζητήσεις ταινιών.
     */
    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
    }

    //----------------
    @FXML
    public void initialize() {

    }

    //-------- Search Movies --------
    @FXML
    private void onSearch() {
        String query = searchField.getText().trim();

        List<Movie> results = movieService.searchMovies(query);

        //Fill the poster grid (Step 2E) <<<
        showMoviesInPosterArea(results);
    }

    @FXML
    private void onTrendingClicked() {

    }

    @FXML
    private void onQuizClicked() {

    }

    @FXML
    private void onUploadContentClicked() {

    }

    @FXML
    private void onWhichActorClicked() {

    }

    @FXML
    private void onSentimentClicked() {

    }

    @FXML
    private void onKpisClicked() {

    }

    /**
     Ανοίγει το παράθυρο αναζήτησης ηθοποιού.
     */
    @FXML
    private void onSearchActorClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/actor-search-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1536, 793);

            Stage stage = (Stage) searchField.getScene().getWindow(); // current window
            stage.setScene(scene);

            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     Ανοίγει το παράθυρο αναζήτησης σκηνοθέτη.
     */
    @FXML
    private void onSearchDirectorClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/director-search-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1536, 793);

            Stage stage = (Stage) searchField.getScene().getWindow(); // current window
            stage.setScene(scene);

            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     Εμφανίζει λεπτομέρειες μίας ταινίας στα labels (τίτλο, έτος, βαθμολογία, overview).
     Εάν το movie είναι null, καθαρίζει τα πεδία.
     */
    private void showMovieDetails(Movie movie) {
        if (movie == null) {
            clearDetails();
            return;
        }

        titleLabel.setText(movie.getTitle() != null ? movie.getTitle() : "");
        yearLabel.setText(movie.getReleaseDate() != null ? movie.getReleaseDate() : "");
        ratingLabel.setText(String.format(Locale.US, "%.1f", movie.getVoteAverage()));
        overviewArea.setText(movie.getOverview() != null ? movie.getOverview() : "");
    }

    /**
     Καθαρίζει τα labels με λεπτομέρειες ταινίας.
     */
    private void clearDetails() {
        titleLabel.setText("");
        yearLabel.setText("");
        ratingLabel.setText("");
        overviewArea.clear();
    }

    /**
     Παίρνει την ημερομηνία και επιστρέφει μόνο τη χρονιά
     */
    private String extractYear(String releaseDate) {
        if (releaseDate == null || releaseDate.isBlank()) {
            return "";
        }
        if (releaseDate.length() >= 4) {
            return releaseDate.substring(0, 4);
        }
        return releaseDate;
    }

    /**
     Δημιουργεί μία “κάρτα ταινίας” (VBox) με poster, τίτλο και βαθμολογία.
     Περιλαμβάνει hover effects και event click για εμφάνιση λεπτομερειών.
     */
    private VBox createSimpleMovieCard(Movie movie) {
        VBox box = new VBox(8);
        box.setPrefWidth(150);
        box.setStyle(
                "-fx-background-color: white; " +
                        "-fx-padding: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);"
        );

        ImageView posterView = new ImageView();
        posterView.setFitWidth(150);
        posterView.setFitHeight(225);
        posterView.setSmooth(true);
        posterView.setPreserveRatio(false);

        if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
            posterView.setImage(new Image(movie.getPosterPath(), true));
        }

        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label ratingLabel = new Label("★ " + movie.getVoteAverage());
        ratingLabel.setStyle("-fx-text-fill: black;");

        box.getChildren().addAll(posterView, titleLabel, ratingLabel);

        box.setOnMouseEntered(e -> {
            box.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-padding: 10; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 12, 0, 0, 5);" +
                            "-fx-scale-x: 1.03; -fx-scale-y: 1.03;"
            );
        });

        box.setOnMouseExited(e -> {
            box.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-padding: 10; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);" +
                            "-fx-scale-x: 1.0; -fx-scale-y: 1.0;"
            );
        });
        box.setOnMouseClicked(e -> showMovieDetails(movie));

        return box;
    }

    /**
     Εμφανίζει όλες τις ταινίες στο FlowPane.
     Καθαρίζει το grid και προσθέτει μια κάρτα για κάθε ταινία.
     */
    private void showMoviesInPosterArea(List<Movie> movies) {

        movieFlowPane.getChildren().clear();

        for (Movie m : movies) {
            movieFlowPane.getChildren().add(createSimpleMovieCard(m));
        }
    }

}

    /**
     Εμφανίζει λεπτομέρειες μίας ταινίας στα labels (τίτλο, έτος, βαθμολογία, overview).
     Εάν το movie είναι null, καθαρίζει τα πεδία.
     */
    private void showMovieDetails(Movie movie) {
        if (movie == null) {
            clearDetails();
            return;
        }

        titleLabel.setText(movie.getTitle() != null ? movie.getTitle() : "");
        yearLabel.setText(movie.getReleaseDate() != null ? movie.getReleaseDate() : "");
        ratingLabel.setText(String.format(Locale.US, "%.1f", movie.getVoteAverage()));
        overviewArea.setText(movie.getOverview() != null ? movie.getOverview() : "");
    }

    /**
     Καθαρίζει τα labels με λεπτομέρειες ταινίας.
     */
    private void clearDetails() {
        titleLabel.setText("");
        yearLabel.setText("");
        ratingLabel.setText("");
        overviewArea.clear();
    }

    /**
     Παίρνει την ημερομηνία και επιστρέφει μόνο τη χρονιά
     */
    private String extractYear(String releaseDate) {
        if (releaseDate == null || releaseDate.isBlank()) {
            return "";
        }
        if (releaseDate.length() >= 4) {
            return releaseDate.substring(0, 4);
        }
        return releaseDate;
    }

    /**
     Δημιουργεί μία “κάρτα ταινίας” (VBox) με poster, τίτλο και βαθμολογία.
     Περιλαμβάνει hover effects και event click για εμφάνιση λεπτομερειών.
     */
    private VBox createSimpleMovieCard(Movie movie) {
        VBox box = new VBox(8);
        box.setPrefWidth(150);
        box.setStyle(
                "-fx-background-color: white; " +
                        "-fx-padding: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);"
        );

        ImageView posterView = new ImageView();
        posterView.setFitWidth(150);
        posterView.setFitHeight(225);
        posterView.setSmooth(true);
        posterView.setPreserveRatio(false);

        if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
            posterView.setImage(new Image(movie.getPosterPath(), true));
        }
