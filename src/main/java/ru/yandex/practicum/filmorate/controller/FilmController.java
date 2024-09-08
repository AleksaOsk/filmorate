package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    private int getNextId() {
        id++;
        return id;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) {
        log.info("Пришел запрос на создание нового фильма с названием - {}", film.getName());
        LocalDate firstFilmInWorld = LocalDate.of(1895, 12, 28);
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription() == null || film.getDescription().isBlank() ||
                   film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть пустым. Максимальная длина — 200 символов");
        } else if (film.getReleaseDate() == null || !film.getReleaseDate().isAfter(firstFilmInWorld)) {
            throw new ValidationException("Некорректная дата релиза");
        } else if (film.getDuration() == null || film.getDuration() < 0) {
            throw new ValidationException("Некорректная продолжительность фильма");
        }

        film.setDuration(film.getDuration());
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Пришел запрос на изменение информации о фильме с id = {} ", film.getId());
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с таким id не существует");
        }
        Film changeFilm = films.get(film.getId());
        LocalDate firstFilmInWorld = LocalDate.of(1895, 12, 28);

        if (film.getName() == null || film.getName().isBlank()) {
            film.setName(changeFilm.getName());
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            film.setName(changeFilm.getName());
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть пустым. Максимальная длина — 200 символов");
        }
        if (film.getReleaseDate() == null) {
            film.setName(changeFilm.getName());
        } else if (!film.getReleaseDate().isAfter(firstFilmInWorld)) {
            throw new ValidationException("Некорректная дата релиза");
        }
        if (film.getDuration() == null) {
            film.setName(changeFilm.getName());
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Некорректная продолжительность фильма");
        }

        films.put(film.getId(), film);
        return film;
    }
}
