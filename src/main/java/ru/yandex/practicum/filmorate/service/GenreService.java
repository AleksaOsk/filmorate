package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    GenreRepository genreRepository;

    public List<GenreDto> getGenres() {
        log.info("Пришел запрос на получение списка всех жанров фильмов");
        return genreRepository.getAllCategory()
                .stream()
                .map(GenreMapper::mapToCategoryDto)
                .collect(Collectors.toList());
    }

    public GenreDto getGenreById(long id) {
        log.info("Пришел запрос на получение жанра фильма с {}", id);
        return genreRepository.getCategoryById(id)
                .map(GenreMapper::mapToCategoryDto)
                .orElseThrow(() -> new NotFoundException("Жанр не найден с ID: " + id));
    }
}
