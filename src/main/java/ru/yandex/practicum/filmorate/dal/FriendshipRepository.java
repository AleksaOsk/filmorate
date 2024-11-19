package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.dal.requests.FriendshipRequests.*;

@Repository
public class FriendshipRepository extends BaseRepository<Long> {

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<Long> mapper) {
        super(jdbc, mapper);
    }

    public void insertFriend(long id, long friendId, String status) {
        insert(
                INSERT_FRIEND_QUERY.query,
                id,
                friendId,
                status
        );
    }

    public Optional<Long> checkFriendship(long id, long friendId) {
        return findOne(FIND_ID_QUERY.query,
                id,
                friendId);
    }

    public List<Long> getFriendsById(long id) {
        return findMany(
                FIND_ALL_FRIENDS_BY_ID_QUERY.query,
                id
        );
    }

    public void deleteFriend(long id, long friendId) {
        update(
                DELETE_FRIEND_QUERY.query,
                id,
                friendId
        );
    }

    public List<Long> getCommonFriends(long id, long friendId) {
        return findMany(
                FIND_MUTUAL_FRIEND_QUERY.query,
                id,
                friendId
        );
    }
}
