package com.moviehouse.controller;

import com.moviehouse.dto.HallDto;
import com.moviehouse.service.HallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
@Tag(name = "Hall", description = "Endpoints for managing cinema halls")
public class HallController {
    private final HallService hallService;

    @Operation(summary = "Get all halls")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of halls.")
    })
    @GetMapping
    public ResponseEntity<List<HallDto>> getAllHalls() {
        log.info("Received request to get all halls");

        return ResponseEntity.ok(hallService.getAllHalls());
    }

    @Operation(summary = "Get hall details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved hall details."),
            @ApiResponse(responseCode = "404", description = "Hall not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HallDto> getHallById(@PathVariable Long id) {
        log.info("Received request to get hall by id={}", id);

        return ResponseEntity.ok(hallService.getHallById(id));
    }

    @Operation(summary = "Create a new hall")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the hall."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data.")
    })
    @PostMapping
    public ResponseEntity<HallDto> createHall(@RequestBody @Valid HallDto dto) {
        log.info("Received request to create a new hall with name='{}'", dto.getName());

        return ResponseEntity.ok(hallService.createHall(dto));
    }

    @Operation(summary = "Update hall details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the hall."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data."),
            @ApiResponse(responseCode = "404", description = "Hall not found.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<HallDto> updateHall(@PathVariable Long id, @RequestBody @Valid HallDto dto) {
        log.info("Received request to update hall by id={}", id);

        return ResponseEntity.ok(hallService.updateHall(id, dto));
    }

    @Operation(summary = "Delete a hall")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the hall."),
            @ApiResponse(responseCode = "404", description = "Hall not found.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HallDto> deleteHall(@PathVariable Long id) {
        log.info("Received request to delete hall by id={}", id);

        return ResponseEntity.ok(hallService.deleteHall(id));
    }
}
