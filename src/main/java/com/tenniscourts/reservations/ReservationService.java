package com.tenniscourts.reservations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.ScheduleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationMapper reservationMapper;
	private final GuestRepository guestRepository;
	private final ScheduleService scheduleService;

	public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {

		if (guestRepository.findById(createReservationRequestDTO.getGuestId()) == null)
    		throw new EntityNotFoundException("Guest not found.");
    	
    	if(scheduleService.findSchedule(createReservationRequestDTO.getScheduleId())== null)
    		throw new EntityNotFoundException("Schedulle not found.");
    	
    	Reservation res = reservationMapper.map(createReservationRequestDTO);
    	res.setValue(new BigDecimal(10L));
    	res.setRefundValue(new BigDecimal(10L));
    	res = reservationRepository.save(res);
    	
    	
    	return reservationMapper.map(res);
    	
    }

	public ReservationDTO findReservation(Long reservationId) {
		return reservationRepository.findById(reservationId).map(reservationMapper::map)
				.<EntityNotFoundException>orElseThrow(() -> {
					throw new EntityNotFoundException("Reservation not found.");
				});
	}

	public ReservationDTO cancelReservation(Long reservationId) {
		return reservationMapper.map(this.cancel(reservationId));
	}

	public List<ReservationDTO> getAll() {
		List<Reservation> list = reservationRepository.findAll(); 
		return reservationMapper.map(list);
	}
	
	private Reservation cancel(Long reservationId) {
		return reservationRepository.findById(reservationId).map(reservation -> {

			this.validateCancellation(reservation);

			BigDecimal refundValue = getRefundValue(reservation);
			return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

		}).<EntityNotFoundException>orElseThrow(() -> {
			throw new EntityNotFoundException("Reservation not found.");
		});
	}

	private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
		reservation.setReservationStatus(status);
		reservation.setValue(reservation.getValue().subtract(refundValue));
		reservation.setRefundValue(refundValue);

		return reservationRepository.save(reservation);
	}

	private void validateCancellation(Reservation reservation) {
		if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
			throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
		}

		if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
		}
	}

	public BigDecimal getRefundValue(Reservation reservation) {
		long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

		if (hours >= 24)
			return reservation.getValue();
		else if (hours >= 12L)
			return reservation.getValue().multiply(new BigDecimal(0.75F));
		else if (hours >= 2L)
			return reservation.getValue().multiply(new BigDecimal(0.50F));
		else if (hours >= 1L)
			return reservation.getValue().multiply(new BigDecimal(0.25));

		return BigDecimal.ZERO;
	}

 
	public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
		 
		Optional<Reservation> previousReservation = reservationRepository.findById(previousReservationId); 
		
		if (!previousReservation.isPresent())
			throw new IllegalArgumentException("Reservation not found.");
		
		if (scheduleId.equals(previousReservation.get().getSchedule().getId()))  
			throw new IllegalArgumentException("Cannot reschedule to the same slot."); 
		
		
		Reservation reservation = this.cancel(previousReservationId);
		reservation.setReservationStatus(ReservationStatus.RESCHEDULED);
		reservationRepository.save(reservation);

		ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
				.guestId(reservation.getGuest().getId()).scheduleId(scheduleId).build());
		newReservation.setPreviousReservation(reservationMapper.map(previousReservation.get()));
		return newReservation;
	}
}
