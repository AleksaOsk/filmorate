package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id;

    private long getNextId() {
        return ++id;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(long id) {
        return users.get(id);
    }

    @Override
    public User addNewUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User addFriend(long id, long friendId) {
        User user = users.get(id);
        User otherUser = users.get(friendId);
        user.addFriend(otherUser.getId());
        otherUser.addFriend(user.getId());
        return user;
    }

    @Override
    public User deleteFriend(long id, long friendId) {
        User user = users.get(id);
        User otherUser = users.get(friendId);
        user.deleteFriend(otherUser.getId());
        otherUser.deleteFriend(user.getId());
        return user;
    }

    @Override
    public Set<Long> getAllFriends(long id) {
        return users.get(id).getFriends();
    }
}
