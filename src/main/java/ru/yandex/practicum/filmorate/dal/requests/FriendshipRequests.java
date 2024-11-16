package ru.yandex.practicum.filmorate.dal.requests;

public enum FriendshipRequests {
    INSERT_FRIEND_QUERY("INSERT INTO friendships (user_id, friend_id, status) VALUES (?, ?, ?)"),
    FIND_ID_QUERY("SELECT id FROM friendships WHERE user_id = ? AND friend_id = ?"),
    UPDATE_FRIEND_STATUS_QUERY("UPDATE friendships SET status = ? WHERE user_id = ? AND friend_id = ?"),
    FIND_ALL_FRIENDS_BY_ID_QUERY("""
            SELECT u.id FROM friendships f JOIN users u ON f.friend_id = u.id
             WHERE f.user_id = ?"""),
    DELETE_FRIEND_QUERY("DELETE FROM friendships WHERE user_id = ? AND friend_id = ?"),
    FIND_MUTUAL_FRIEND_QUERY("""
            SELECT u.id FROM friendships f JOIN users u ON f.friend_id = u.id WHERE f.user_id = ?
             INTERSECT SELECT u.id FROM friendships f JOIN users u ON f.friend_id = u.id WHERE f.user_id = ?""");

    public final String query;

    FriendshipRequests(String query) {
        this.query = query;
    }
}
