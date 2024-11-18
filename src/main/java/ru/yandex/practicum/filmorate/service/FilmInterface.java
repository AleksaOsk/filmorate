package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.film.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequestDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDto;

import java.util.Collection;
import java.util.List;

public interface FilmInterface {
    Collection<FilmResponseDto> getAllFilms();

    FilmResponseDto getFilmById(long id);

    FilmResponseDto addNewFilm(NewFilmRequestDto request);

    FilmResponseDto updateFilm(UpdateFilmRequestDto request);

    FilmResponseDto deleteFilm(long id);

    FilmResponseDto addLike(long id, long userId);

    FilmResponseDto deleteLike(long id, long userId);

    List<FilmResponseDto> getPopularFilms(long count);
}
