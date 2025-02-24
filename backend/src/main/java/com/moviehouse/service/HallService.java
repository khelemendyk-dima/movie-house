package com.moviehouse.service;

import com.moviehouse.dto.HallDto;

import java.util.List;

public interface HallService {

    List<HallDto> getAll();

    HallDto getById(Long id);

    HallDto create(HallDto hallDto);

    HallDto update(Long id, HallDto hallDto);

    HallDto delete(Long id);

}
