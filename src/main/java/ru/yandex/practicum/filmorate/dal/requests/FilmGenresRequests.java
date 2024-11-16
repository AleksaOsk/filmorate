package ru.yandex.practicum.filmorate.dal.requests;

public enum FilmGenresRequests {
    DELETE_QUERY("DELETE FROM film_genres WHERE film_id = ?"),
    FIND_FILMS_GENRES_QUERY(
            "SELECT g.id, g.name FROM film_genres f JOIN genres g ON f.genre_id=g.id WHERE f.film_id = ?");

    public final String query;

    FilmGenresRequests(String query) {
        this.query = query;
    }
}
