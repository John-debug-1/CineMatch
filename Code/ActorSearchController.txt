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
import org.MY_APP.Model.Actor;
import org.MY_APP.TMD_database.TMDdatabase_Client;

import java.util.List;
import java.util.Optional;

public class ActorSearchController {

    // Πάνω μέρος – πεδίο αναζήτησης
    @FXML private TextField searchField;

    // Κεντρική περιοχή – εμφάνιση των καρτών ηθοποιών
    @FXML private FlowPane actorFlowPane;
    @FXML private ScrollPane actorScrollPane;

    // Δεξιό panel – λεπτομέρειες ηθοποιού
    @FXML private Label titleLabel;     // όνομα ηθοποιού
    @FXML private Label yearLabel;      // ημερομηνία γέννησης
    @FXML private Label ratingLabel;    // δημοτικότητα (popularity)
    @FXML private TextArea overviewArea; // βιογραφία + γνωστά έργα

    // Κάτω status bar
    @FXML private Label statusLabel;

    private TMDdatabase_Client tmdbClient;

    // Εκτελείται αυτόματα όταν φορτωθεί το FXML
    @FXML
    public void initialize() {
        // Φόρτωση API key και δημιουργία TMDb client
        String apiKey = Config.getTmdbApiKey();
        tmdbClient = new TMDdatabase_Client(apiKey);

        statusLabel.setText("CineMatch v1.0.0");
    }

    // Εκτελείται όταν ο χρήστης πατήσει το κουμπί "Search"
    @FXML
    private void onSearch() {
        String query = searchField.getText().trim(); // Παίρνουμε το κείμενο του χρήστη

        if (query.isEmpty()) { // Έλεγχος για άδειο input
            statusLabel.setText("Enter actor name");
            return;
        }

        // Αναζήτηση ηθοποιών μέσω TMDb API
        List<Actor> actors = tmdbClient.searchActors(query);

        actorFlowPane.getChildren().clear(); // Καθαρισμός προηγούμενων αποτελεσμάτων

        if (actors.isEmpty()) { // Αν δεν βρέθηκαν αποτελέσματα
            statusLabel.setText("No actors found");
            clearDetails();
            return;
        }

        statusLabel.setText("Found " + actors.size() + " actors");

        // Δημιουργία καρτών για κάθε ηθοποιό
        for (Actor a : actors) {
            actorFlowPane.getChildren().add(createActorCard(a));
        }
    }

    // Δημιουργεί την κάρτα ενός ηθοποιού (εικόνα + όνομα + known for)
    private VBox createActorCard(Actor actor) {
        VBox box = new VBox(8); // Κάθετη διάταξη με απόσταση
        box.setPrefWidth(150);
        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-padding: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);"
        );
        box.setAlignment(Pos.TOP_CENTER);

        // --- Εικόνα ηθοποιού ---
        ImageView img = new ImageView();
        img.setFitWidth(150);
        img.setFitHeight(200);
        img.setPreserveRatio(false);

        if (actor.getProfilePath() != null) { // Αν υπάρχει URL εικόνας
            img.setImage(new Image(actor.getProfilePath(), true));
        }

        // --- Όνομα ηθοποιού ---
        Label name = new Label(actor.getName());
        name.setWrapText(true);
        name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // --- Γνωστός/ή για ---
        Label known = new Label(
                actor.getKnownForTitles() == null || actor.getKnownForTitles().isEmpty()
                ? "Known for: —"
                : "Known for: " + String.join(", ", actor.getKnownForTitles())
        );
        known.setWrapText(true);

        // Προσθήκη στοιχείων στην κάρτα
        box.getChildren().addAll(img, name, known);

        // Όταν ο χρήστης κάνει click → εμφάνιση λεπτομερειών
        box.setOnMouseClicked(e -> showActorDetails(actor));

        return box;
    }

    // Εμφανίζει στο δεξί panel τα πλήρη στοιχεία του ηθοποιού
    private void showActorDetails(Actor basicActor) {
        // Φέρνουμε πιο αναλυτικά στοιχεία από TMDb
        Optional<Actor> detailsOpt = tmdbClient.getActorDetails(basicActor.getId());
        Actor actor = detailsOpt.orElse(basicActor); // fallback αν αποτύχει

        titleLabel.setText(actor.getName());

        String birthday = actor.getBirthday();
        yearLabel.setText(birthday != null ? birthday : "Unknown");

        ratingLabel.setText(String.format("%.1f popularity", actor.getPopularity()));

        // Δημιουργία κειμένου βιογραφίας
        StringBuilder overview = new StringBuilder();

        if (actor.getBiography() != null && !actor.getBiography().isEmpty()) {
            overview.append(actor.getBiography()).append("\n\n");
        }

        if (actor.getKnownForTitles() != null && !actor.getKnownForTitles().isEmpty()) {
            overview.append("Known for: ")
                    .append(String.join(", ", actor.getKnownForTitles()));
        }

        overviewArea.setText(overview.toString());
    }

    // Καθαρίζει το δεξί panel με τις λεπτομέρειες
    private void clearDetails() {
        titleLabel.setText("");
        yearLabel.setText("");
        ratingLabel.setText("");
        overviewArea.clear();
    }

    // Μετάβαση στη σελίδα αναζήτησης ταινιών
    @FXML
    private void onSearchMovieClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cinematch-view.fxml")
            );

            Parent movieRoot = loader.load();

            // Αλλαγή ολόκληρης της σκηνής
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(movieRoot, 1536, 793));
            stage.setTitle("CineMatch – Movie Search");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Μετάβαση στη σελίδα αναζήτησης σκηνοθετών
    @FXML
    private void onSearchDirectorClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/director-search-view.fxml"));
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

