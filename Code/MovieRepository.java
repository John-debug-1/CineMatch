package org.MY_APP.Database;

import org.MY_APP.Model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MovieRepository {

    public void save(Movie movie) {
        String sql =
                "MERGE INTO movies (id, title, release_date, overview, poster_path, popularity, vote_average) " +
                        "KEY(id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movie.getId());
            ps.setString(2, movie.getTitle());
            ps.setString(3, movie.getReleaseDate());
            ps.setString(4, movie.getOverview());
            ps.setString(5, movie.getPosterPath());
            ps.setDouble(6, movie.getPopularity());
            ps.setDouble(7, movie.getVoteAverage());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Movie> findById(int id) {
        String sql =
                "SELECT id, title, release_date, overview, poster_path, popularity, vote_average " +
                        "FROM movies WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Movie m = new Movie();
                    m.setId(rs.getInt("id"));
                    m.setTitle(rs.getString("title"));
                    m.setReleaseDate(rs.getString("release_date"));
                    m.setOverview(rs.getString("overview"));
                    m.setPosterPath(rs.getString("poster_path"));
                    m.setPopularity(rs.getDouble("popularity"));
                    m.setVoteAverage(rs.getDouble("vote_average"));
                    return Optional.of(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void saveSearchQuery(String query) {
        String sql = "INSERT INTO search_history (query) VALUES (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, query);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
