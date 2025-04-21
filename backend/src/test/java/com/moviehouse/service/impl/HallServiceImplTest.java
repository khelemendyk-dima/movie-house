package com.moviehouse.service.impl;

import com.moviehouse.dto.HallDto;
import com.moviehouse.exception.HallNotFoundException;
import com.moviehouse.model.Hall;
import com.moviehouse.repository.HallRepository;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.config.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HallServiceImplTest {

    @Mock
    private HallRepository hallRepository;

    @Mock
    private ConvertorUtil convertor;

    @InjectMocks
    private HallServiceImpl hallService;

    private Hall hall;
    private HallDto hallDto;

    @BeforeEach
    void setup() {
        hall = TestDataFactory.createHall();
        hallDto = TestDataFactory.createHallDto();
    }

    @Test
    void getAllHalls_shouldReturnHallDtos() {
        List<Hall> halls = List.of(hall, hall);
        List<HallDto> hallDtos = List.of(hallDto, hallDto);

        when(hallRepository.findAll()).thenReturn(halls);
        when(convertor.toHallDto(any(Hall.class))).thenReturn(hallDto);

        List<HallDto> result = hallService.getAllHalls();

        assertEquals(hallDtos.size(), result.size());
        assertArrayEquals(hallDtos.toArray(), result.toArray());

        verify(hallRepository).findAll();
        verify(convertor, times(2)).toHallDto(any(Hall.class));
    }

    @Test
    void getAllHalls_shouldReturnHallDtos_whenNoHalls() {
        when(hallRepository.findAll()).thenReturn(Collections.emptyList());

        List<HallDto> result = hallService.getAllHalls();

        assertEquals(0, result.size());

        verify(hallRepository).findAll();
        verify(convertor, never()).toHallDto(any(Hall.class));
    }

    @Test
    void getHallById_shouldReturnHallDto_whenFound() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(convertor.toHallDto(hall)).thenReturn(hallDto);

        HallDto result = hallService.getHallById(1L);

        assertEquals(hallDto, result);

        verify(hallRepository).findById(1L);
        verify(convertor).toHallDto(hall);
    }

    @Test
    void getHallById_shouldThrowException_whenNotFound() {
        when(hallRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(HallNotFoundException.class, () -> hallService.getHallById(404L));
    }

    @Test
    void createHall_shouldSaveAndReturnDto() {
        when(hallRepository.save(any(Hall.class))).thenReturn(hall);
        when(convertor.toHallDto(any(Hall.class))).thenReturn(hallDto);

        HallDto result = hallService.createHall(hallDto);

        verify(hallRepository).save(any(Hall.class));

        assertEquals(hallDto.getName(), result.getName());
        assertEquals(hallDto.getRowCount(), result.getRowCount());
        assertEquals(hallDto.getSeatsPerRow(), result.getSeatsPerRow());
        assertEquals(hallDto, result);
    }

    @Test
    void updateHall_shouldUpdateAndReturnDto_whenFound() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(hallRepository.save(any(Hall.class))).thenReturn(hall);
        when(convertor.toHallDto(hall)).thenReturn(hallDto);

        HallDto result = hallService.updateHall(1L, hallDto);

        assertEquals(hallDto, result);
        verify(hallRepository).save(hall);
    }

    @Test
    void updateHall_shouldThrowException_whenHallNotFound() {
        when(hallRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(HallNotFoundException.class, () -> hallService.updateHall(404L, hallDto));
    }

    @Test
    void deleteHall_shouldDeleteAndReturnDto_whenFound() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(convertor.toHallDto(hall)).thenReturn(hallDto);

        HallDto result = hallService.deleteHall(1L);

        assertEquals(hallDto, result);

        verify(hallRepository).delete(hall);
    }

    @Test
    void deleteHall_shouldThrowException_whenNotFound() {
        when(hallRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(HallNotFoundException.class, () -> hallService.deleteHall(404L));

        verify(hallRepository, never()).delete(any(Hall.class));
    }
}
