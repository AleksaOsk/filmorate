package ru.yandex.practicum.filmorate.dal.requests;

public enum FilmLikeRequests {
    INSERT_LIKE_QUERY("INSERT INTO likes (film_id, user_id) VALUES (?, ?)"),
    FIND_ID_QUERY("SELECT id FROM likes WHERE film_id = ? AND user_id = ?"),
    DELETE_LIKE_QUERY("DELETE FROM likes WHERE film_id = ? AND user_id = ? ");

    public final String query;

    FilmLikeRequests(String query) {
        this.query = query;
    }
}
