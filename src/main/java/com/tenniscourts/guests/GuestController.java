package com.tenniscourts.guests;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tenniscourts.config.BaseRestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/guest")
@AllArgsConstructor
public class GuestController extends BaseRestController {

	private final GuestService guestService;

	@PostMapping
	@ApiOperation(value = "Add a guest user.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User created."),
							@ApiResponse(code = 500, message = "Unexpected error.") })
	public ResponseEntity<Void> add(@RequestBody GuestDTO guestDTO) {
		return ResponseEntity.created(locationByEntity(guestService.addGuest(guestDTO).getId())).build();
	}

	@GetMapping("/{guestId}")
	@ApiOperation(value = "Find a guest by id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Guest user found."),
							@ApiResponse(code = 500, message = "Unexpected error") })
	public ResponseEntity<GuestDTO> findGuestById(@PathVariable Long guestId) {
		return ResponseEntity.ok(guestService.findGuestById(guestId));
	}

	@GetMapping
	@ApiOperation(value = "Get a guest by name")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Guest user found."),
							@ApiResponse(code = 500, message = "Unexpected error") })
	public ResponseEntity<List<GuestDTO>> findByName(@RequestParam(name = "name", required = false) String name) {
		if (name != null)
			return ResponseEntity.ok(guestService.findGuestByName(name));
		else
			return ResponseEntity.ok(guestService.getAll());
	}
	
	@ApiOperation(value = "Update an existend user")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Guest user updated."),
							@ApiResponse(code = 500, message = "Unexpected error") })	
	public ResponseEntity<GuestDTO> Put(@PathVariable long id, @Valid @RequestBody GuestDTO guestDTO) {
		return ResponseEntity.ok(guestService.updateGuest(id, guestDTO));
	}

	@ApiOperation(value = "Delete an existend user")
	@DeleteMapping(value = "/{guestId}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Guest user deleted."),
							@ApiResponse(code = 500, message = "Unexpected error") })		
	public ResponseEntity<Object> Delete(@PathVariable long guestId) {
		return ResponseEntity.ok(guestService.deleteGuest(guestId));
	}
	
}