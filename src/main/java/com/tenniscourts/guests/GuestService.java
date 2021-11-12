package com.tenniscourts.guests;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tenniscourts.exceptions.EntityNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GuestService {

	private final GuestRepository guestRepository;

	private final GuestMapper guestMapper;

	public GuestDTO addGuest(GuestDTO guestDTO) {
		return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
	}

	public GuestDTO findGuestById(Long id) {
		return guestRepository.findById(id).map(guestMapper::map).<EntityNotFoundException>orElseThrow(() -> {
			throw new EntityNotFoundException("Guest not found.");
		});
	}

	public List<GuestDTO> findGuestByName(String name) {
		List<Guest> guestList = guestRepository.findByName(name); 
		return guestMapper.map(guestList);
	}
	
	public List<GuestDTO> getAll() {
		List<Guest> guestList = guestRepository.findAll(); 
		return guestMapper.map(guestList);
	}

	public GuestDTO updateGuest(Long guestId, GuestDTO guestDTO) {
		Optional<Guest> guestFind = guestRepository.findById(guestId);
		if (guestFind.isPresent()) {
			guestFind.get().setName(guestDTO.getName());
			guestRepository.save(guestFind.get());
		} else {
			throw new EntityNotFoundException("Guest not found.");
		}
		return guestMapper.map(guestFind.get());
	}

	public ResponseEntity<Long> deleteGuest(Long guestId) {
		Optional<Guest> guestFind = guestRepository.findById(guestId);
		if (guestFind.isPresent()) {
			guestRepository.delete(guestFind.get());
		} else {
			throw new EntityNotFoundException("Guest not found.");
		}
		return new ResponseEntity<>(guestId, HttpStatus.OK);
	}

}
