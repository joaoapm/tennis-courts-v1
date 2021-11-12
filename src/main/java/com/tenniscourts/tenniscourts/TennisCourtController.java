package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/tennis-court")
@AllArgsConstructor
public class TennisCourtController extends BaseRestController {

	private final TennisCourtService tennisCourtService;

	@PostMapping
	@ApiOperation(value = "Add a tennis court")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tennis Court created."),
							@ApiResponse(code = 500, message = "Unexpected error.") })
	public ResponseEntity<Void> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO) {
		return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId()))
				.build();
	}

	@GetMapping("/{tennisCourtId}")
	@ApiOperation(value = "Find tennis court by id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tennis Court found."),
							@ApiResponse(code = 500, message = "Unexpected error.") })
	public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable Long tennisCourtId) {
		return ResponseEntity.ok(tennisCourtService.findTennisCourtById(tennisCourtId));
	}

	@GetMapping("/schedule/{tennisCourtId}")
	@ApiOperation(value = "Find tennis court with schedules by id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tennis Court found."),
							@ApiResponse(code = 500, message = "Unexpected error.") })
	public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(Long tennisCourtId) {
		return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
	}
}