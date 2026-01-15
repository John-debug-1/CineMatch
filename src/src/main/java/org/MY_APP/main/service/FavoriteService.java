package org.MY_APP.main.service;

import org.MY_APP.main.model.*;
import org.MY_APP.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private FavoriteTvRepository favoriteTvRepository;
    @Autowired
    private FavoriteActorRepository favoriteActorRepository;
    @Autowired
    private FavoriteDirectorRepository favoriteDirectorRepository;
    @Autowired
    private UserRepository userRepository;

    public void addFavorite(User user, int movieId, String title, String posterPath) {
        if (favoriteRepository.existsByUserAndMovieId(user, movieId)) return;
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setMovieId(movieId);
        favorite.setTitle(title);
        favorite.setPosterPath(posterPath);
        favoriteRepository.save(favorite);
    }

    public void removeFavorite(User user, int movieId) {
        favoriteRepository.findByUserAndMovieId(user, movieId).ifPresent(favoriteRepository::delete);
    }

    public List<Favorite> getUserFavorites(User user) {
        return favoriteRepository.findByUser(user);
    }

    public void addFavoriteActor(User user, int actorId, String name, String profilePath) {
        if (favoriteActorRepository.existsByUserAndActorId(user, actorId)) return;
        FavoriteActor actor = new FavoriteActor();
        actor.setUser(user);
        actor.setActorId(actorId);
        actor.setName(name);
        actor.setProfilePath(profilePath);
        favoriteActorRepository.save(actor);
    }

    public void removeFavoriteActor(User user, int actorId) {
        favoriteActorRepository.findByUserAndActorId(user, actorId).ifPresent(favoriteActorRepository::delete);
    }


    @Autowired private TmdbService tmdbService;

    // MOVIE
    public boolean isFavoriteMovie(User user, int movieId) {
        return favoriteRepository.existsByUserAndMovieId(user, movieId);
    }

    public void addFavoriteMovieById(User user, int movieId) {
        if (favoriteRepository.existsByUserAndMovieId(user, movieId)) return;

        var m = tmdbService.getMovieDetails(movieId); // φέρε title/poster_path
        Favorite f = new Favorite();
        f.setUser(user);
        f.setMovieId(movieId);
        f.setTitle((String) m.get("title"));
        f.setPosterPath((String) m.get("poster_path"));
        favoriteRepository.save(f);
    }

    // TV
    public boolean isFavoriteTv(User user, int tvId) {
        return favoriteTvRepository.existsByUserAndTvId(user, tvId);
    }

    public void addFavoriteTvById(User user, int tvId) {
        if (favoriteTvRepository.existsByUserAndTvId(user, tvId)) return;

        var tv = tmdbService.getTvDetails(tvId);
        FavoriteTv f = new FavoriteTv();
        f.setUser(user);
        f.setTvId(tvId);
        f.setName((String) tv.get("name"));
        f.setPosterPath((String) tv.get("poster_path"));
        favoriteTvRepository.save(f);
    }

    public void removeFavoriteTv(User user, int tvId) {
        favoriteTvRepository.findByUserAndTvId(user, tvId).ifPresent(favoriteTvRepository::delete);
    }

    // ACTOR
    public boolean isFavoriteActor(User user, int actorId) {
        return favoriteActorRepository.existsByUserAndActorId(user, actorId);
    }

    public void addFavoriteActorById(User user, int actorId) {
        if (favoriteActorRepository.existsByUserAndActorId(user, actorId)) return;

        var p = tmdbService.getPersonDetails(actorId);
        FavoriteActor a = new FavoriteActor();
        a.setUser(user);
        a.setActorId(actorId);
        a.setName((String) p.get("name"));
        a.setProfilePath((String) p.get("profile_path"));
        favoriteActorRepository.save(a);
    }

    // DIRECTOR
    public boolean isFavoriteDirector(User user, int directorId) {
        return favoriteDirectorRepository.existsByUserAndDirectorId(user, directorId);
    }

    public void addFavoriteDirectorById(User user, int directorId) {
        if (favoriteDirectorRepository.existsByUserAndDirectorId(user, directorId)) return;

        var p = tmdbService.getPersonDetails(directorId);
        FavoriteDirector d = new FavoriteDirector();
        d.setUser(user);
        d.setDirectorId(directorId);
        d.setName((String) p.get("name"));
        d.setProfilePath((String) p.get("profile_path"));
        favoriteDirectorRepository.save(d);
    }

    public void removeFavoriteDirector(User user, int directorId) {
        favoriteDirectorRepository.findByUserAndDirectorId(user, directorId).ifPresent(favoriteDirectorRepository::delete);
    }
}