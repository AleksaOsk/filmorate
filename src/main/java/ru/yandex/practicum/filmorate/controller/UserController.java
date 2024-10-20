package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    UserService userService;

    @GetMapping
    public List<User> getUsers() { // /users  //для получения списка пользователей.
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") long id) { // /users  //для получения списка пользователей.
        return userService.getUser(id);
    }

    @PostMapping
    public User addNewUser(@RequestBody User user) {
        return userService.addNewUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/friends/{friendId}") // добавление в друзья.
    public User addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) throws Throwable {
        return userService.addFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/friends/{friendId}") // удаление из друзей.
    public User deleteFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends") // возвращаем список пользователей, являющихся его друзьями.
    public List<User> getAllFriends(@PathVariable("id") long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") // список друзей, общих с другим пользователем.
    public List<User> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

}