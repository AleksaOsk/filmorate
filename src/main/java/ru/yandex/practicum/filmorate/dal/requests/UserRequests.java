package ru.yandex.practicum.filmorate.dal.requests;

public enum UserRequests {
    FIND_ALL_QUERY("SELECT * FROM users"),
    FIND_BY_ID_QUERY("SELECT * FROM users WHERE id = ?"),
    INSERT_QUERY("INSERT INTO users(name, email, login, birthday) VALUES (?, ?, ?, ?)"),
    UPDATE_QUERY("UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE id = ?"),
    DELETE_BY_ID_QUERY("DELETE FROM users WHERE id = ?");

    public final String query;

    UserRequests(String query) {
        this.query = query;
    }
}
