package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    InMemoryFilmStorage inMemoryFilmStorage;
    InMemoryUserStorage inMemoryUserStorage;

    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film addNewFilm(Film film) {
        log.info("Пришел запрос на создание нового фильма с названием - {}", film.getName());
        checkName(film);
        checkDescription(film);
        checkReleaseDate(film);
        checkFilmDuration(film);
        return inMemoryFilmStorage.addNewFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Пришел запрос на изменение информации о фильме с id = {} ", film.getId());
        if (!inMemoryFilmStorage.getFilms().containsKey(film.getId())) {
            throw new NotFoundException("Фильма с таким id не существует");
        }

        Film changeFilm = inMemoryFilmStorage.getFilms().get(film.getId());

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
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Film addLike(long id, long userId) {
        log.info("Пришел запрос на добавление лайка к фильму с id {} от пользователя с userId {}", id, userId);
        checkId(id);
        checkUserId(userId);
        return inMemoryFilmStorage.addLike(id, userId);
    }

    public Film deleteLike(long id, long userId) {
        log.info("Пришел запрос на удаление лайка к фильму с id {} от пользователя с userId {}", id, userId);
        checkId(id);
        checkUserId(userId);
        return inMemoryFilmStorage.deleteLike(id, userId);
    }

    // возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10
    public List<Film> getPopularFilms(long count) {
        log.info("Пришел запрос на получение списка из {} самых популярных фильмов", count);
        Set<Film> filmsSet = inMemoryFilmStorage.getPopularFilms();
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
        if (!inMemoryFilmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм с таким id не существует");
        }
    }

    private void checkUserId(long userId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }
}