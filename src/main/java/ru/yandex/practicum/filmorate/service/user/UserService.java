package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAllUsers() { // /users  //для получения списка пользователей.
        log.info("Пришел запрос на получение списка всех пользователей");
        return userStorage.getAllUsers();
    }

    public User getUser(long id) {
        log.info("Пришел запрос на получение пользователя с {}", id);
        return userStorage.getUser(id);
    }

    public User addNewUser(User user) {
        log.info("Пришел запрос на создание нового пользователя с email = {}", user.getEmail());
        checkEmail(user.getEmail());
        checkLogin(user.getLogin());
        checkBirthday(user.getBirthday());
        String newName = checkName(user.getName(), user.getLogin());
        user.setName(newName);
        return userStorage.addNewUser(user);
    }

    public User updateUser(User user) {
        log.info("Пришел запрос на изменение информации о пользователе с id = {} ", user.getId());
        checkId(user.getId());
        User changeUser = userStorage.getUser(user.getId());
        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().equals(changeUser.getEmail())) {
            user.setEmail(changeUser.getEmail());
        } else {
            checkEmail(user.getEmail());
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().equals(changeUser.getLogin())) {
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
        return userStorage.updateUser(user);
    }

    public User addFriend(long id, long friendId) {
        log.info("Пришел запрос от пользователя с id {} на добавление в друзья пользователя с id {}", id, friendId);
        checkId(id);
        checkId(friendId);
        return userStorage.addFriend(id, friendId);
    }

    public User deleteFriend(long id, long friendId) {
        log.info("Пришел запрос от пользователя с id {} на удаление из друзей пользователя с id {}", id, friendId);
        checkId(id);
        checkId(friendId);
        return userStorage.deleteFriend(id, friendId);
    }

    public List<User> getAllFriends(long id) {
        log.info("Пришел запрос на получение списка друзей пользователя с id {}", id);
        checkId(id);
        Set<Long> userId = userStorage.getAllFriends(id);
        List<User> users = new ArrayList<>();
        for (Long user : userId) {
            users.add(getUser(user));
        }
        return users;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        log.info("Пришел запрос от пользователя с id {} на получение списка общих друзей с пользователем с id {}",
                id, otherId);

        checkId(id);
        checkId(otherId);
        Set<Long> userFriends = userStorage.getAllFriends(id);
        Set<Long> otherUserFriends = userStorage.getAllFriends(otherId);

        List<User> commonId = new ArrayList<>();

        for (Long user : userFriends) {
            for (Long otherUser : otherUserFriends) {
                if (user.equals(otherUser)) {
                    commonId.add(getUser(user));
                }
            }
        }
        return commonId;
    }

    private void checkBirthday(LocalDate birthday) {
        if (birthday == null || birthday == LocalDate.now() ||
            birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения");
        }
    }

    private void checkEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Имейл должен быть указан");
        } else if (!email.contains("@")) {
            throw new ValidationException("Имейл должен содержать символ '@'");
        } else {
            for (User user1 : getAllUsers()) {
                if (user1.getEmail().equals(email)) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                }
            }
        }
    }

    private void checkLogin(String login) {
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        } else {
            for (User user1 : userStorage.getAllUsers()) {
                if (user1.getEmail().equals(login)) {
                    throw new DuplicatedDataException("Такой логин уже используется");
                }
            }
        }
    }

    private String checkName(String name, String login) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            return login;
        }
        return name;
    }

    private void checkId(long id) {
        if (userStorage.getUser(id) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }
}
