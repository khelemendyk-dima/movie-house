package com.moviehouse.service;

import com.moviehouse.dto.HallDto;

import java.util.List;

public interface HallService {

    List<HallDto> getAllHalls();

    HallDto getHallById(Long id);

    HallDto createHall(HallDto hallDto);

    HallDto updateHall(Long id, HallDto hallDto);

    HallDto deleteHall(Long id);

}
