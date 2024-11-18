package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dto.film.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequestDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("filmService")
@AllArgsConstructor
public class FilmService implements FilmInterface {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final FilmLikeRepository filmLikeRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final FilmGenresRepository filmGenresRepository;

    public List<FilmResponseDto> getAllFilms() {
        log.info("Пришел запрос на получение списка всех фильмов");
        return filmRepository.getAllFilms()
                .stream()
                .map(this::setRating)
                .map(this::findGenres)
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmResponseDto getFilmById(long id) {
        log.info("Пришел запрос на получение фильма с {}", id);
        Film film = filmRepository.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с идентификатором " + id + " не найден."));
        setRating(film);
        findGenres(film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmResponseDto addNewFilm(NewFilmRequestDto request) {
        log.info("Пришел запрос на создание нового фильма с названием - {}", request.getName());
        checkName(request.getName());
        checkDescription(request.getDescription());
        checkReleaseDate(request.getReleaseDate());
        checkFilmDuration(request.getDuration());

        Film film = FilmMapper.mapToFilm(request);
        setRating(film);
        setGenres(film);
        film = filmRepository.addNewFilm(film);
        if (film.getGenres() != null) {
            filmGenresRepository.save(film.getId(), checkGenreListDuplicates(film.getGenres()));
        }
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmResponseDto updateFilm(UpdateFilmRequestDto request) {
        log.info("Пришел запрос на изменение информации о фильме с id = {} ", request.getId());
        checkId(request.getId());
        checkName(request.getName());
        checkDescription(request.getDescription());
        checkReleaseDate(request.getReleaseDate());
        checkFilmDuration(request.getDuration());

        Optional<Film> filmOptional = filmRepository.getFilmById(request.getId());
        Film film = filmOptional.get();
        Film filmReq = FilmMapper.updateFilmFields(film, request);

        FilmResponseDto serviceOldFilm = getFilmById(filmReq.getId());

        if (serviceOldFilm.getGenres() != null && !serviceOldFilm.getGenres().isEmpty()) {
            filmGenresRepository.delete(filmReq.getId());
        }
        if (filmReq.getGenres() != null && !filmReq.getGenres().isEmpty()) {
            filmGenresRepository.save(filmReq.getId(), filmReq.getGenres());
        }
        setRating(filmReq);
        setGenres(filmReq);

        filmRepository.updateFilm(filmReq);
        log.info("Фильм {} был успешно обновлен", filmReq);
        return FilmMapper.mapToFilmDto(filmReq);
    }

    public FilmResponseDto deleteFilm(long filmId) {
        log.error("Пришел запрос на изменение информации о фильме с id = {} ", filmId);
        Film film = filmRepository.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с идентификатором " + filmId + " не найден."));

        if (!filmRepository.delete(filmId)) {
            throw new ValidationException("Ошибка при удалении фильма с id " + filmId);
        }
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmResponseDto addLike(long id, long userId) {
        log.info("Пришел запрос на добавление лайка к фильму с id {} от пользователя с userId {}", id, userId);
        checkId(id);
        checkUserId(userId);
        if (notLikesCheck(id, userId)) {
            filmLikeRepository.insertLike(id, userId);
        } else {
            throw new ValidationException("Эту фильму ранее был поставлен лайк");
        }
        return getFilmById(id);
    }

    public FilmResponseDto deleteLike(long id, long userId) {
        log.info("Пришел запрос на удаление лайка к фильму с id {} от пользователя с userId {}", id, userId);
        checkId(id);
        checkUserId(userId);
        if (notLikesCheck(id, userId)) {
            throw new ValidationException("Этому фильму не был поставлен ранее лайк");
        }
        filmLikeRepository.deleteLike(id, userId);
        return getFilmById(id);

    }

    public List<FilmResponseDto> getPopularFilms(long count) {
        log.info("Пришел запрос на получение списка из {} самых популярных фильмов", count);
        return filmRepository.getPopularFilms(count)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public Film setGenres(Film film) {
        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres();
            genres = genreRepository.getListGenre(genres);
            if (genres.isEmpty()) {
                log.error("Введен несуществующий жанр");
                throw new ValidationException("Жанр с таким id не существует");
            }
            film.setGenres(genres);
            return film;
        }
        return film;
    }

    public Film setRating(Film film) {
        if (film.getMpa() != null) {
            long mpaId = film.getMpa().getId();
            Optional<Mpa> oMpa = mpaRepository.getMpaById(mpaId);
            if (oMpa.isPresent()) {
                film.setMpa(oMpa.get());
            } else {
                throw new ValidationException("Возрастной рейтинг с идентификатором " + mpaId + " не найден.");
            }
        }
        return film;
    }

    public Film findGenres(Film film) {
        List<Genre> genres = filmGenresRepository.getFilmGenres(film.getId());
        if (genres != null && !genres.isEmpty()) {
            film.setGenres(genres);
        }
        return film;
    }

    private void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
    }

    private void checkDescription(String description) {
        if (description == null || description.isBlank() ||
            description.length() > 200) {
            throw new ValidationException("Описание не может быть пустым. Максимальная длина — 200 символов");
        }
    }

    private void checkReleaseDate(LocalDate localDate) {
        LocalDate firstFilmInWorld = LocalDate.of(1895, 12, 28);
        if (localDate == null || !localDate.isAfter(firstFilmInWorld)) {
            throw new ValidationException("Некорректная дата релиза");
        }
    }

    private void checkFilmDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new ValidationException("Некорректная продолжительность фильма");
        }
    }

    private void checkId(long id) {
        if (filmRepository.getFilmById(id).isEmpty()) {
            throw new NotFoundException("Фильм с таким id не существует");
        }
    }

    private void checkUserId(long userId) {
        if (userRepository.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }

    private boolean notLikesCheck(long filmId, long userId) {
        return filmLikeRepository.likeCheck(filmId, userId).isEmpty();
    }

    public List<Genre> checkGenreListDuplicates(List<Genre> genresList) {
        Map<Long, Genre> uniqueGenresMap = genresList.stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre, (existing, replacement) -> existing));
        return uniqueGenresMap.values().stream().collect(Collectors.toList());
    }

}