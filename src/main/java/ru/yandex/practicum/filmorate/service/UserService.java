package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UserResponseDto;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("userService")
@AllArgsConstructor
public class UserService implements UserInterface {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public List<UserResponseDto> getAllUsers() { // /users  //для получения списка пользователей.
        log.info("Пришел запрос на получение списка всех пользователей");
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUser(long id) {
        log.info("Пришел запрос на получение пользователя с {}", id);
        return userRepository.getUserById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
    }

    public UserResponseDto addNewUser(NewUserRequestDto request) {
        log.info("Пришел запрос на создание нового пользователя с email = {}", request.getEmail());
        checkEmail(request.getEmail());
        checkLogin(request.getLogin());
        checkBirthday(request.getBirthday());
        String newName = checkName(request.getName(), request.getLogin());
        request.setName(newName);
        User user = UserMapper.mapToUser(request);

        user = userRepository.addNewUser(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserResponseDto updateUser(UpdateUserRequestDto request) {
        log.info("Пришел запрос на изменение информации о пользователе с id = {} ", request.getId());
        checkId(request.getId());
        Optional<User> userOptional = userRepository.getUserById(request.getId());
        User user = userOptional.get();
        User userReq = UserMapper.updateUserFields(user, request);

        if (!request.getEmail().equals(userReq.getEmail())) {
            checkEmail(request.getEmail());
        }
        if (!request.getLogin().equals(userReq.getLogin())) {
            checkLogin(request.getLogin());
        }
        checkBirthday(request.getBirthday());

        user = userRepository.updateUser(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserResponseDto deleteUser(long userId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден."));
        if (!userRepository.delete(userId)) {
            log.error("Ошибка удаления");
            throw new ValidationException("Ошибка при удалении пользователя с ИД " + userId);
        }
        return UserMapper.mapToUserDto(user);
    }

    public UserResponseDto addFriend(long id, long friendId) {
        log.info("Пришел запрос от пользователя с id {} на добавление в друзья пользователя с id {}", id, friendId);
        if (id == friendId) {
            log.error("id пользователей совпадают");
            throw new ValidationException("Указанные id совпадают");
        }
        checkId(id);
        checkId(friendId);

        if (notFriendsCheck(id, friendId)) {
            friendshipRepository.insertFriend(id, friendId, Status.CONFIRMED.toString());
        } else {
            log.error("ID друга уже есть в БД");
            throw new ValidationException("Пользователь с id " + friendId +
                                          " уже есть в списке друзей id " + id);
        }
        return getUser(id);
    }

    public UserResponseDto deleteFriend(long id, long friendId) {
        log.info("Пришел запрос от пользователя с id {} на удаление из друзей пользователя с id {}", id, friendId);
        checkId(id);
        checkId(friendId);
        if (notFriendsCheck(id, friendId)) {
            log.error("Пользователи не являются друзьями");
            return getUser(id);
        }
        friendshipRepository.deleteFriend(id, friendId);
        return getUser(id);
    }

    public List<UserResponseDto> getAllFriends(long id) {
        log.info("Пришел запрос на получение списка друзей пользователя с id {}", id);
        userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден."));
        return getUsersById(friendshipRepository.getFriendsById(id));
    }

    public List<UserResponseDto> getCommonFriends(long id, long otherId) {
        log.info("Пришел запрос от пользователя с id {} на получение списка общих друзей с пользователем с id {}",
                id, otherId);
        checkId(id);
        checkId(otherId);
        return getUsersById(friendshipRepository.getCommonFriends(id, otherId));
    }

    private List<UserResponseDto> getUsersById(List<Long> idsList) {
        return idsList.stream()
                .map(this::getUser)
                .collect(Collectors.toList());
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
            for (User user1 : userRepository.getAllUsers()) {
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
            for (User user1 : userRepository.getAllUsers()) {
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
        if (userRepository.getUserById(id).isEmpty()) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }

    private boolean notFriendsCheck(long id, long friendId) {
        return friendshipRepository.checkFriendship(id, friendId).isEmpty();
    }
}