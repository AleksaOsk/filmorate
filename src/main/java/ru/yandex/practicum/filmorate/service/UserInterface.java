package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.user.NewUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UserResponseDto;

import java.util.List;

public interface UserInterface {
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUser(long id);

    UserResponseDto addNewUser(NewUserRequestDto request);

    UserResponseDto updateUser(UpdateUserRequestDto request);

    UserResponseDto deleteUser(long userId);

    UserResponseDto addFriend(long id, long friendId);

    UserResponseDto deleteFriend(long id, long friendId);

    List<UserResponseDto> getAllFriends(long id);

    List<UserResponseDto> getCommonFriends(long id, long otherId);
}
