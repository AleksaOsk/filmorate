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
        checkEmail(user.getEmail());
        checkLogin(user.getLogin());
        checkBirthday(user.getBirthday());
        String newName = checkName(user.getName(), user.getLogin());
        user.setName(newName);
        user.setId(getNextId());
        users.put(user.getId(), user);
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
            checkEmail(user.getEmail());
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            user.setLogin(changeUser.getLogin());
        } else {
            checkLogin(user.getLogin());
        }
        if (user.getBirthday() == null) {
            user.setBirthday(changeUser.getBirthday());
        } else {
            checkBirthday(user.getBirthday());
        }
        String newName = checkName(user.getName(), user.getLogin());
        user.setName(newName);
        users.put(user.getId(), user);
        return user;
    }

    void checkBirthday(LocalDate birthday) {
        if (birthday == null || birthday == LocalDate.now() ||
            birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения");
        }
    }

    void checkEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Имейл должен быть указан");
        } else if (!email.contains("@")) {
            throw new ValidationException("Имейл должен содержать символ '@'");
        } else {
            for (User user1 : users.values()) {
                if (user1.getEmail().equals(email)) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                }
            }
        }
    }

    void checkLogin(String login) {
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        } else {
            for (User user1 : users.values()) {
                if (user1.getEmail().equals(login)) {
                    throw new DuplicatedDataException("Такой логин уже используется");
                }
            }
        }
    }

    String checkName(String name, String login) {
        if (name == null || name.isBlank()) {
            return login;
        }
        return name;
    }
}