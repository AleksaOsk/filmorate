package ru.yandex.practicum.filmorate.dal.requests;

public enum MpaRequests {
    FIND_ALL_QUERY("SELECT * FROM mpa"),
    FIND_BY_ID_QUERY("SELECT * FROM mpa WHERE id = ?");

    public final String query;

    MpaRequests(String query) {
        this.query = query;
    }
}
