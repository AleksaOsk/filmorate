package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.dal.requests.GenreRequests.FIND_ALL_QUERY;
import static ru.yandex.practicum.filmorate.dal.requests.GenreRequests.FIND_BY_ID_QUERY;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getAllCategory() {
        return findMany(FIND_ALL_QUERY.query);
    }

    public Optional<Genre> getCategoryById(long mpaId) {
        return findOne(FIND_BY_ID_QUERY.query, mpaId);
    }

    public List<Genre> getListGenre(List<Genre> list) {
        String placeholders = String.join(",", Collections.nCopies(list.size(), "?"));
        String listGenreQuery = "SELECT id, name FROM genres WHERE id IN (" + placeholders + ")";
        List<Long> listInt = list.stream()
                .map(Genre::getId)
                .toList();
        Object[] params = listInt.toArray(new Object[0]);
        return findMany(listGenreQuery, params);
    }
}
