package com.moviehouse.controller;

import com.moviehouse.dto.HallDto;
import com.moviehouse.service.HallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
public class HallController {
    private final HallService hallService;

    @GetMapping
    public ResponseEntity<List<HallDto>> getAllHalls() {
        return ResponseEntity.ok(hallService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HallDto> getHallById(@PathVariable Long id) {
        return ResponseEntity.ok(hallService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HallDto> createHall(@RequestBody @Valid HallDto dto) {
        return ResponseEntity.ok(hallService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HallDto> updateHall(@PathVariable Long id, @RequestBody @Valid HallDto dto) {
        return ResponseEntity.ok(hallService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HallDto> deleteHall(@PathVariable Long id) {
        return ResponseEntity.ok(hallService.delete(id));
    }
}
