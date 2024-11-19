package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.genre.GenreResponseDto;

import java.util.Collection;

public interface GenreInterface {
    Collection<GenreResponseDto> getGenres();

    GenreResponseDto getGenreById(long id);
}
