package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) {
        return filmService.addNewFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}") // пользователь ставит лайк фильму.
    public Film addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}") // пользователь удаляет лайк.
    public Film deleteLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        return filmService.deleteLike(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular") // возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") long count) {
        return filmService.getPopularFilms(count);
    }
}
