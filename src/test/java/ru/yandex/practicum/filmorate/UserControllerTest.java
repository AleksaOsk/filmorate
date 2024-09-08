package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private final UserController userController = new UserController();
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setEmail("email@ysdf.com");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2000, 7, 30));
    }

    public String setting(User user) {
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.addNewUser(user));
        return exception.getMessage();
    }

    @Test
    public void emailValidationUser() {
        user.setEmail("dog");
        assertEquals("Имейл должен содержать символ '@'", setting(user));
    }

    @Test
    public void loginValidationUser() {
        user.setLogin("dog cat");
        assertEquals("Логин не может быть пустым или содержать пробелы", setting(user));
    }

    @Test
    public void nameValidationUser() {
        user.setName("");
        userController.addNewUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void birthdayValidationUser() {
        user.setBirthday(LocalDate.of(2025, 7, 30));
        assertEquals("Некорректная дата рождения", setting(user));
    }
}