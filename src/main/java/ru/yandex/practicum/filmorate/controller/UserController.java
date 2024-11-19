package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UserResponseDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getUsers() { // /users  //для получения списка пользователей.
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable("id") long id) { // /users  //для получения списка пользователей.
        return userService.getUser(id);
    }

    @PostMapping
    public UserResponseDto addNewUser(@RequestBody NewUserRequestDto request) {
        return userService.addNewUser(request);
    }

    @PutMapping
    public UserResponseDto updateUser(@RequestBody UpdateUserRequestDto request) {
        return userService.updateUser(request);
    }

    @DeleteMapping("/{id}")
    public UserResponseDto deleteUser(@PathVariable("id") long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}/friends/{friend_id}") // добавление в друзья.
    public UserResponseDto addFriend(@PathVariable("id") long id, @PathVariable("friend_id") long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friend_id}") // удаление из друзей.
    public UserResponseDto deleteFriend(@PathVariable("id") long id, @PathVariable("friend_id") long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends") // возвращаем список пользователей, являющихся его друзьями.
    public List<UserResponseDto> getAllFriends(@PathVariable("id") long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{friend_id}") // список друзей, общих с другим пользователем.
    public List<UserResponseDto> getCommonFriends(@PathVariable("id") long id, @PathVariable("friend_id") long friendId) {
        return userService.getCommonFriends(id, friendId);
    }
}