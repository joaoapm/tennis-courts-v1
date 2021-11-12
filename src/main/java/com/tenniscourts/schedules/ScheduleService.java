package com.tenniscourts.schedules;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    
    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleMapper scheduleMapper;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
    	
    	Optional<TennisCourt> tenisCourtAlt = tennisCourtRepository.findById(tennisCourtId);
    	
		if (!tenisCourtAlt.isPresent())
    		throw new EntityNotFoundException("Tennis court not found.");
		
		Schedule newSchedule = new Schedule();
		newSchedule.setTennisCourt(tenisCourtAlt.get());
		newSchedule.setStartDateTime(createScheduleRequestDTO.getStartDateTime());
		newSchedule.setEndDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1L));
		
		
       return scheduleMapper.map(scheduleRepository.save(newSchedule));
       
    }
 
	public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
		return scheduleMapper.map(scheduleRepository.findByStartDateTimeAfterAndEndDateTimeBefore(startDate,endDate));
	}

    public ScheduleDTO findSchedule(Long scheduleId) {
    	return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).<EntityNotFoundException>orElseThrow(() -> {
			throw new EntityNotFoundException("Schedule not found.");
		});
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
