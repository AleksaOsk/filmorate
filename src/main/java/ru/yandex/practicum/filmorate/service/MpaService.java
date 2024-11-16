package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {
    MpaRepository mpaRepository;

    public List<MpaDto> getAllMpa() {
        log.info("Пришел запрос на получение списка возрастных рейтингов");
        return mpaRepository.getAllMpa()
                .stream()
                .map(MpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }

    public MpaDto getMpaById(long id) {
        log.info("Пришел запрос на получение возрастного рейтинга с {}", id);
        if (mpaRepository.getMpaById(id).isEmpty()) {
            throw new NotFoundException("Неверный id mpa");
        }
        return MpaMapper.mapToMpaDto(mpaRepository.getMpaById(id).get());
    }
}
