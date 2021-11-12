package com.tenniscourts.schedules;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenniscourts.config.BaseRestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

	@PostMapping
	@ApiOperation(value = "Create a schedulle for a given tennis court.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Schedulle created."),
							@ApiResponse(code = 500, message = "Unexpected error.") })
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO.getTennisCourtId(), createScheduleRequestDTO).getId())).build();
    }

	@GetMapping(path="/{startDate}/{endDate}")
	@ApiOperation(value = "Get the schedule with dates between start date and end date.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "List of schedules generated."),
							@ApiResponse(code = 500, message = "Unexpected error.") })
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(LocalDate startDate,
                                                                  LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

	@GetMapping
	@ApiOperation(value = "Get the schedule with dates between start date and end date.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Schedule found."),
							@ApiResponse(code = 500, message = "Unexpected error.") })
    public ResponseEntity<ScheduleDTO> findByScheduleId(Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
	
}
