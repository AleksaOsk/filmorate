package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.dal.requests.MpaRequests.FIND_ALL_QUERY;
import static ru.yandex.practicum.filmorate.dal.requests.MpaRequests.FIND_BY_ID_QUERY;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> getAllMpa() {
        return findMany(FIND_ALL_QUERY.query);
    }

    public Optional<Mpa> getMpaById(long mpaId) {
        return findOne(FIND_BY_ID_QUERY.query, mpaId);
    }
}
