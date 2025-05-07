package com.scaffold.template.services.meeting;

import com.scaffold.template.dtos.meeting.CreateMeetingRequestDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public interface MeetingService {

    CreateMeetingRequestDTO createMeeting(Long studentId, Long mentorId, Long matchId, LocalDate date, LocalTime time, int duration, String reason);
}
