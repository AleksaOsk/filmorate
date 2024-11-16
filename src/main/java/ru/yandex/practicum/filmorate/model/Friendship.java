package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Friendship {
    private long userId;
    private long friendId;
    private String status;
}
