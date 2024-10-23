package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film getFilm(long id);

    Film addNewFilm(Film film);

    Film updateFilm(Film film);

    Film addLike(long id, long friendId);

    Film deleteLike(long id, long friendId);

    Set<Film> getPopularFilms();
}
