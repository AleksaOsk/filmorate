package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Getter
    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    private Long getNextId() {
        return ++id;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(long id) {
        return films.get(id);
    }

    @Override
    public Film addNewFilm(Film film) {
        film.setDuration(film.getDuration());
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film addLike(long id, long userId) {
        Film film = films.get(id);
        film.addLike(userId);
        return film;
    }

    @Override
    public Film deleteLike(long id, long userId) {
        Film film = films.get(id);
        film.deleteLike(userId);
        return film;
    }

    @Override
    public Set<Film> getPopularFilms() {
        Set<Film> film = new TreeSet<>(Comparator.comparing(Film::getCountLikes).reversed());
        film.addAll(films.values());
        return film;
    }
}