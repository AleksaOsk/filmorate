package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addNewFilm(Film film) {
        log.info("Пришел запрос на создание нового фильма с названием - {}", film.getName());
        checkName(film);
        checkDescription(film);
        checkReleaseDate(film);
        checkFilmDuration(film);
        return filmStorage.addNewFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Пришел запрос на изменение информации о фильме с id = {} ", film.getId());
        if (filmStorage.getFilm(film.getId()) == null) {
            throw new NotFoundException("Фильма с таким id не существует");
        }

        Film changeFilm = filmStorage.getFilm(film.getId());

        if (film.getName() == null || film.getName().isBlank()) {
            film.setName(changeFilm.getName());
        } else {
            checkName(film);
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            film.setDescription(changeFilm.getDescription());
        } else {
            checkDescription(film);
        }
        if (film.getReleaseDate() == null) {
            film.setReleaseDate(changeFilm.getReleaseDate());
        } else {
            checkReleaseDate(film);
        }
        if (film.getDuration() == null) {
            film.setDuration(changeFilm.getDuration());
        } else {
            checkFilmDuration(film);
        }
        return filmStorage.updateFilm(film);
    }

    public Film addLike(long id, long userId) {
        log.info("Пришел запрос на добавление лайка к фильму с id {} от пользователя с userId {}", id, userId);
        checkId(id);
        checkUserId(userId);
        return filmStorage.addLike(id, userId);
    }

    public Film deleteLike(long id, long userId) {
        log.info("Пришел запрос на удаление лайка к фильму с id {} от пользователя с userId {}", id, userId);
        checkId(id);
        checkUserId(userId);
        return filmStorage.deleteLike(id, userId);
    }

    // возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10
    public List<Film> getPopularFilms(long count) {
        log.info("Пришел запрос на получение списка из {} самых популярных фильмов", count);
        Set<Film> filmsSet = filmStorage.getPopularFilms();
        List<Film> films = new ArrayList<>(filmsSet);

        List<Film> popularFilm = new ArrayList<>();

        if (films.size() < count) {
            count = films.size();
        }
        if (!films.isEmpty() && count > 0) {
            for (int i = 0; i <= count - 1; i++) {
                popularFilm.add(films.get(i));
            }
        }
        return popularFilm;
    }

    private void checkName(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
    }

    private void checkDescription(Film film) {
        if (film.getDescription() == null || film.getDescription().isBlank() ||
            film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть пустым. Максимальная длина — 200 символов");
        }
    }

    private void checkReleaseDate(Film film) {
        LocalDate firstFilmInWorld = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate() == null || !film.getReleaseDate().isAfter(firstFilmInWorld)) {
            throw new ValidationException("Некорректная дата релиза");
        }
    }

    private void checkFilmDuration(Film film) {
        if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ValidationException("Некорректная продолжительность фильма");
        }
    }

    private void checkId(long id) {
        if (filmStorage.getFilm(id) == null) {
            throw new NotFoundException("Фильм с таким id не существует");
        }
    }

    private void checkUserId(long userId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }
}