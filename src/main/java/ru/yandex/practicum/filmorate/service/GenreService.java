package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.genre.GenreResponseDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("genreService")
@AllArgsConstructor
public class GenreService implements GenreInterface {
    private final GenreRepository genreRepository;

    public List<GenreResponseDto> getGenres() {
        log.info("Пришел запрос на получение списка всех жанров фильмов");
        return genreRepository.getAllCategory()
                .stream()
                .map(GenreMapper::mapToCategoryDto)
                .collect(Collectors.toList());
    }

    public GenreResponseDto getGenreById(long id) {
        log.info("Пришел запрос на получение жанра фильма с {}", id);
        return genreRepository.getCategoryById(id)
                .map(GenreMapper::mapToCategoryDto)
                .orElseThrow(() -> new NotFoundException("Жанр не найден с ID: " + id));
    }
}
