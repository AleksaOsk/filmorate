package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    GenreService genreService;

    @GetMapping
    public Collection<GenreDto> getGenres() {
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable("id") long id) {
        return genreService.getGenreById(id);
    }

}
