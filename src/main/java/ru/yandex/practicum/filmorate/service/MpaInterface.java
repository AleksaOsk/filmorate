package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.mpa.MpaResponseDto;

import java.util.Collection;

public interface MpaInterface {
    Collection<MpaResponseDto> getAllMpa();

    MpaResponseDto getMpaById(long id);
}
