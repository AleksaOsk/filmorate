package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.dal.requests.FilmGenresRequests.DELETE_QUERY;
import static ru.yandex.practicum.filmorate.dal.requests.FilmGenresRequests.FIND_FILMS_GENRES_QUERY;

@Repository
public class FilmGenresRepository extends BaseRepository<Genre> {
    public FilmGenresRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getFilmGenres(Long filmId) {
        return findMany(FIND_FILMS_GENRES_QUERY.query, filmId);
    }

    public boolean delete(long filmId) {
        return delete(DELETE_QUERY.query, filmId);
    }

    public void save(Long filmId, List<Genre> uniqueGenres) {
        int size = uniqueGenres.size();
        List<String> queries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Long genreId = uniqueGenres.get(i).getId();
            queries.add("INSERT INTO film_genres (film_id, genre_id) " +
                        "SELECT " + filmId + ", " + genreId + " FROM DUAL WHERE NOT EXISTS (" +
                        "SELECT 1 FROM film_genres WHERE film_id = " + filmId + " AND genre_id = " + genreId + ")");
        }
        String[] arr = queries.toArray(new String[queries.size()]);
        insertBatch(arr);
    }
}
