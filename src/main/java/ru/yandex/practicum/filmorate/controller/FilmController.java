package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequestDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmResponseDto> getFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public FilmResponseDto getFilmById(@PathVariable("id") long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public FilmResponseDto addNewFilm(@RequestBody NewFilmRequestDto request) {
        return filmService.addNewFilm(request);
    }

    @PutMapping
    public FilmResponseDto updateFilm(@RequestBody UpdateFilmRequestDto request) {
        return filmService.updateFilm(request);
    }

    @DeleteMapping("/{id}") // пользователь удаляет лайк.
    public FilmResponseDto deleteFilm(@PathVariable("id") long id) {
        return filmService.deleteFilm(id);
    }

    @PutMapping("/{id}/like/{userId}") // пользователь ставит лайк фильму.
    public FilmResponseDto addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}") // пользователь удаляет лайк.
    public FilmResponseDto deleteLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular") // возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10
    public List<FilmResponseDto> getPopularFilms(@RequestParam(defaultValue = "10") long count) {
        return filmService.getPopularFilms(count);
    }
}