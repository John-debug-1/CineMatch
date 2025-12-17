package org.MY_APP.User_Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.MY_APP.Config.Config;
import org.MY_APP.Model.Director;
import org.MY_APP.TMD_database.TMDdatabase_Client;

import java.util.List;
import java.util.Optional;

public class DirectorSearchController {
 
    // --------- UI Elements συνδεδεμένα με το FXML ---------
    @FXML private TextField searchField;       // Πεδίο για πληκτρολόγηση ονόματος σκηνοθέτη
    @FXML private FlowPane directorFlowPane;   
    @FXML private ScrollPane directorScrollPane; 

    @FXML private Label titleLabel;     
    @FXML private Label yearLabel;      
    @FXML private Label ratingLabel;     
    @FXML private TextArea overviewArea;  

    @FXML private Label statusLabel;   

    private TMDdatabase_Client tmdbClient; // Client για κλήσεις στο TMDB API

    // -------------------------------------------------------------------------
    // initialize() εκτελείται αυτόματα όταν φορτωθεί το FXML
    // -------------------------------------------------------------------------
    @FXML
    public void initialize() {
        String apiKey = Config.getTmdbApiKey();
        tmdbClient = new TMDdatabase_Client(apiKey);
        directorFlowPane.prefWidthProperty().bind(directorScrollPane.widthProperty());
        statusLabel.setText("CineMatch v1.0.0");
    }

    // -------------------------------------------------------------------------
    // onSearch(): Εκτελείται όταν ο χρήστης πατήσει το κουμπί "Search"
    // -------------------------------------------------------------------------
    @FXML
    private void onSearch() {
        // Παίρνουμε το όνομα που έγραψε ο χρήστης
        String query = searchField.getText().trim();
        // Αν δεν έγραψε τίποτα → δείχνουμε μήνυμα και σταματάμε
        if (query.isEmpty()) {
            statusLabel.setText("Please enter a director name.");
            return;
        }

        // Αναζήτηση σκηνοθετών μέσω TMDB API
        List<Director> results = tmdbClient.searchDirectors(query);
        directorFlowPane.getChildren().clear();

        // Αν δεν βρέθηκαν αποτελέσματα
        if (results.isEmpty()) {
            statusLabel.setText("No directors found for: " + query);
            clearDetails();
            return;
        }

        // Βρέθηκαν αποτελέσματα → ενημερώνουμε το status
        statusLabel.setText("Found " + results.size() + " director(s) for: " + query);
        // Για κάθε σκηνοθέτη δημιουργούμε κάρτα
        for (Director d : results) {
            directorFlowPane.getChildren().add(createDirectorCard(d));
        }
    }

    // -------------------------------------------------------------------------
    // createDirectorCard(): Δημιουργεί μια κάρτα VBOX για κάθε σκηνοθέτη
    // -------------------------------------------------------------------------
    private VBox createDirectorCard(Director director) {
        VBox box = new VBox(8); 
        box.setPrefWidth(150); 
        box.setStyle(
                "-fx-background-color: white; " +
                "-fx-padding: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);"
        );
        box.setAlignment(Pos.TOP_CENTER);

        // Εικόνα σκηνοθέτη
        ImageView img = new ImageView();
        img.setFitWidth(150);
        img.setFitHeight(200);
        img.setPreserveRatio(false);

        // Αν υπάρχει profile image → τη φορτώνουμε
        if (director.getProfilePath() != null) {
            img.setImage(new Image(director.getProfilePath(), true));
        }

        // Εμφάνιση ονόματος
        Label name = new Label(director.getName());
        name.setWrapText(true);
        name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");
        Label known = new Label(
                (director.getKnownForTitles() == null || director.getKnownForTitles().isEmpty())
                        ? "Known for: —"
                        : "Known for: " + String.join(", ", director.getKnownForTitles())
        );
        known.setWrapText(true);
        known.setStyle("-fx-text-fill: black;");

        // Προσθήκη στοιχείων στην κάρτα
        box.getChildren().addAll(img, name, known);
        // Όταν ο χρήστης πατήσει πάνω στην κάρτα → δείχνει λεπτομέρειες
        box.setOnMouseClicked(e -> showDirectorDetails(director));

        return box;
    }

    // -------------------------------------------------------------------------
    // showDirectorDetails() — Εμφάνιση αναλυτικών πληροφοριών στο δεξί panel
    // -------------------------------------------------------------------------
    private void showDirectorDetails(Director basicDirector) {
        // Παίρνουμε αναλυτικά δεδομένα από το API
        Optional<Director> detailsOpt = tmdbClient.getDirectorDetails(basicDirector.getId());
        Director director = detailsOpt.orElse(basicDirector);
        
        titleLabel.setText(director.getName());
        String birthday = director.getBirthday();
        yearLabel.setText(birthday != null ? birthday : "Unknown");
        ratingLabel.setText(String.format("%.1f popularity", director.getPopularity()));
        StringBuilder overview = new StringBuilder();

        if (director.getBiography() != null && !director.getBiography().isEmpty()) {
            overview.append(director.getBiography()).append("\n\n");
        }

        if (director.getKnownForTitles() != null && !director.getKnownForTitles().isEmpty()) {
            overview.append("Known for: ")
                    .append(String.join(", ", director.getKnownForTitles()));
        }
        // Βάζουμε το κείμενο στο TextArea
        overviewArea.setText(overview.toString());
    }

    // -------------------------------------------------------------------------
    // clearDetails(): Καθαρίζει το δεξί panel
    // -------------------------------------------------------------------------
    private void clearDetails() {
        titleLabel.setText("");
        yearLabel.setText("");
        ratingLabel.setText("");
        overviewArea.clear();
    }

    // -------------------------------------------------------------------------
    // Επιστροφή σε Movie Search View
    // -------------------------------------------------------------------------
    @FXML
    private void onSearchMovieClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cinematch-view.fxml")
            );

            Parent movieRoot = loader.load()

            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(movieRoot, 1536, 793));
            stage.setTitle("CineMatch – Movie Search");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // Μετάβαση στο Actor Search View
    // -------------------------------------------------------------------------
    @FXML
    private void onSearchActorClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/actor-search-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1536, 793);
            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


















