package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Data
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likes;
    private long countLikes;

    public Film() {
        likes = new TreeSet<>();
    }

    public void addLike(long id) {
        long firstSize = likes.size();
        likes.add(id);
        long secondSize = likes.size();
        if (firstSize != secondSize) {
            countLikes++;
        }
    }

    public void deleteLike(long id) {
        long firstSize = likes.size();
        likes.remove(id);
        long secondSize = likes.size();
        if (firstSize != secondSize) {
            countLikes--;
        }
    }
}
