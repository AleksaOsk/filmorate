package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static ru.yandex.practicum.filmorate.dal.requests.FilmLikeRequests.*;

@Repository
public class FilmLikeRepository extends BaseRepository<Long> {
    public FilmLikeRepository(JdbcTemplate jdbc, RowMapper<Long> mapper) {
        super(jdbc, mapper);
    }

    public void insertLike(long id, long userId) {
        insert(
                INSERT_LIKE_QUERY.query,
                id,
                userId
        );
    }

    public Optional<Long> likeCheck(long id, long userId) {
        return findOne(FIND_ID_QUERY.query,
                id,
                userId);
    }

    public void deleteLike(long id, long userId) {
        update(
                DELETE_LIKE_QUERY.query,
                id,
                userId
        );
    }
}
