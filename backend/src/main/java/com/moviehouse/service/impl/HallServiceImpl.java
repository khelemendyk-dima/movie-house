package com.moviehouse.service.impl;

import com.moviehouse.dto.HallDto;
import com.moviehouse.exception.HallNotFoundException;
import com.moviehouse.model.Hall;
import com.moviehouse.model.Seat;
import com.moviehouse.repository.HallRepository;
import com.moviehouse.service.HallService;
import com.moviehouse.util.ConvertorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {
    private final HallRepository hallRepository;
    private final ConvertorUtil convertor;

    @Override
    public List<HallDto> getAllHalls() {
        log.info("Fetching all halls");

        return hallRepository.findAll().stream()
                .map(convertor::toHallDto)
                .toList();
    }

    @Override
    public HallDto getHallById(Long id) {
        log.info("Fetching hall with id={}", id);

        return convertor.toHallDto(findHallById(id));
    }

    @Transactional
    @Override
    public HallDto createHall(HallDto hallDto) {
        log.info("Creating new hall: {}", hallDto);

        Hall hall = new Hall();
        hall.setName(hallDto.getName());
        hall.setRowCount(hallDto.getRowCount());
        hall.setSeatsPerRow(hallDto.getSeatsPerRow());

        List<Seat> seats = generateSeats(hallDto.getRowCount(), hallDto.getSeatsPerRow(), hall);

        hall.setSeats(seats);

        hallRepository.save(hall);

        log.info("Created hall with id={}", hall.getId());

        return convertor.toHallDto(hall);
    }

    @Transactional
    @Override
    public HallDto updateHall(Long id, HallDto hallDto) {
        log.info("Updating hall with id={}", id);

        Hall hall = findHallById(id);

        hall.setName(hallDto.getName());
        hall.setRowCount(hallDto.getRowCount());
        hall.setSeatsPerRow(hallDto.getSeatsPerRow());

        log.debug("Clearing previous seats for hall id={}", id);
        hall.getSeats().clear();

        List<Seat> newSeats = generateSeats(hallDto.getRowCount(), hallDto.getSeatsPerRow(), hall);
        hall.getSeats().addAll(newSeats);

        hallRepository.save(hall);

        log.info("Updated hall with id={}", id);

        return convertor.toHallDto(hall);
    }

    @Transactional
    @Override
    public HallDto deleteHall(Long id) {
        log.info("Deleting hall with id={}", id);

        Hall hall = findHallById(id);
        hallRepository.delete(hall);

        log.info("Deleted hall with id={}", id);

        return convertor.toHallDto(hall);
    }

    private Hall findHallById(Long id) {
        log.debug("Searching for hall with id={}", id);

        return hallRepository.findById(id)
                .orElseThrow(() -> new HallNotFoundException(id));
    }

    private List<Seat> generateSeats(int rowCount, int seatsPerRow, Hall hall) {
        log.debug("Generating seats for hall: rowCount={}, seatsPerRow={}", rowCount, seatsPerRow);

        return IntStream.rangeClosed(1, rowCount)
                .boxed()
                .flatMap(row -> IntStream.rangeClosed(1, seatsPerRow)
                        .mapToObj(seatNumber -> {
                            Seat seat = new Seat();
                            seat.setRowNumber(row);
                            seat.setSeatNumber(seatNumber);
                            seat.setHall(hall);

                            return seat;
                        }))
                .toList();
    }
}
