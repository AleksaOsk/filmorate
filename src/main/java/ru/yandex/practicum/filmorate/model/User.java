package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Data
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
