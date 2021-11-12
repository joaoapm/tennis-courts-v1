package com.tenniscourts.reservations;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/reservation")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

	@PostMapping
	@ApiOperation(value = "Book a reservation")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Book reservation complete."),
							@ApiResponse(code = 500, message = "Unexpected error.") })
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping("/{reservationId}")
	@ApiOperation(value = "Get a reservation by id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Book reservation complete."),
							@ApiResponse(code = 500, message = "Unexpected error.") })    
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @GetMapping("/")
	@ApiOperation(value = "Returns a list with all reservations.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "List of reservations."),
							@ApiResponse(code = 500, message = "Unexpected error.") })    
    public ResponseEntity<List<ReservationDTO>> getHistoric() {
        return ResponseEntity.ok(reservationService.getAll());
    }
    
    @PutMapping(path="/cancel/{reservationId}")
	@ApiOperation(value = "Cancel one reservation.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Reservation canceled."),
							@ApiResponse(code = 500, message = "Unexpected error.") })    
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping(path = "/reschedule/{reservationId}/{scheduleId}")
	@ApiOperation(value = "Reschedulle one reservation.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Reservation rescheduled."),
							@ApiResponse(code = 500, message = "Unexpected error.") })    
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable Long reservationId,@PathVariable Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
    
}
