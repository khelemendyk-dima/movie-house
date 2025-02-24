package com.moviehouse.service.impl;

import com.moviehouse.dto.HallDto;
import com.moviehouse.exceptions.HallNotFoundException;
import com.moviehouse.model.Hall;
import com.moviehouse.model.Seat;
import com.moviehouse.repository.HallRepository;
import com.moviehouse.service.HallService;
import com.moviehouse.util.ConvertorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {
    private final HallRepository hallRepository;
    private final ConvertorUtil convertor;

    @Override
    public List<HallDto> getAll() {
        return hallRepository.findAll().stream()
                .map(convertor::toHallDto)
                .toList();
    }

    @Override
    public HallDto getById(Long id) {
        return convertor.toHallDto(findHallById(id));
    }

    @Transactional
    @Override
    public HallDto create(HallDto hallDto) {
        Hall hall = new Hall();
        hall.setName(hallDto.getName());
        hall.setRowCount(hallDto.getRowCount());
        hall.setSeatsPerRow(hallDto.getSeatsPerRow());

        List<Seat> seats = generateSeats(hallDto.getRowCount(), hallDto.getSeatsPerRow(), hall);

        hall.setSeats(seats);

        hallRepository.save(hall);

        return convertor.toHallDto(hall);
    }

    @Transactional
    @Override
    public HallDto update(Long id, HallDto hallDto) {
        Hall hall = findHallById(id);

        hall.setName(hallDto.getName());
        hall.setRowCount(hallDto.getRowCount());
        hall.setSeatsPerRow(hallDto.getSeatsPerRow());

        // delete previous seats
        hall.getSeats().clear();

        List<Seat> newSeats = generateSeats(hallDto.getRowCount(), hallDto.getSeatsPerRow(), hall);

        hall.getSeats().addAll(newSeats);

        hallRepository.save(hall);

        return convertor.toHallDto(hall);
    }

    @Transactional
    @Override
    public HallDto delete(Long id) {
        Hall hall = findHallById(id);

        hallRepository.delete(hall);

        return convertor.toHallDto(hall);
    }

    private Hall findHallById(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new HallNotFoundException(id));
    }

    private List<Seat> generateSeats(int rowCount, int seatsPerRow, Hall hall) {
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
