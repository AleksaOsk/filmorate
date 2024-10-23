package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> getAllUsers();

    User getUser(long id);

    User addNewUser(User user);

    User updateUser(User user);

    User addFriend(long id, long friendId);

    User deleteFriend(long id, long friendId);

    Set<Long> getAllFriends(long id);
}
