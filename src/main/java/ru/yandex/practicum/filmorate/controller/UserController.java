package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final List<String> emails = new ArrayList<>();
    private final List<String> logins = new ArrayList<>();
    private int id;

    private int getNextId() {
        return ++id;
    }

    @GetMapping
    public List<User> getUsers() { // /users  //для получения списка пользователей.
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addNewUser(@RequestBody User user) {
        log.info("Пришел запрос на создание нового пользователя с email = {}", user.getEmail());
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Имейл должен быть указан");
        } else if (!user.getEmail().contains("@")) {
            throw new ValidationException("Имейл должен содержать символ '@'");
        } else if (emails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        } else if (logins.contains(user.getLogin())) {
            throw new DuplicatedDataException("Такой логин уже используется");
        } else if (user.getBirthday() == null || user.getBirthday() == LocalDate.now() ||
                   user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        logins.add(user.getLogin());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Пришел запрос на изменение информации о пользователе с id = {} ", user.getId());
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с таким id не существует");
        }

        User changeUser = users.get(user.getId());

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(changeUser.getEmail());
        } else {
            if (!user.getEmail().contains("@")) {
                throw new ValidationException("Имейл должен содержать символ '@'");
            } else if (emails.contains(user.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            user.setLogin(changeUser.getLogin());
        } else {
            if (user.getLogin().contains(" ")) {
                throw new ValidationException("Логин не может быть пустым или содержать пробелы");
            }
        }

        if (user.getBirthday() == null) {
            user.setBirthday(changeUser.getBirthday());
        } else {
            if (user.getBirthday() == LocalDate.now() || user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Некорректная дата рождения");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        logins.remove(changeUser.getLogin());
        logins.add(user.getLogin());
        emails.remove(changeUser.getEmail());
        emails.add(user.getEmail());
        users.put(user.getId(), user);
        return user;
    }
}
