package ru.yandex.practicum.filmorate.dal.requests;

public enum GenreRequests {
    FIND_ALL_QUERY("SELECT * FROM genres"),
    FIND_BY_ID_QUERY("SELECT * FROM genres WHERE id = ?");

    public final String query;

    GenreRequests(String query) {
        this.query = query;
    }
}
