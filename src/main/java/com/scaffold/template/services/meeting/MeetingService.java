package com.scaffold.template.services.meeting;

import com.scaffold.template.dtos.meeting.CreateMeetingRequestDTO;
import com.scaffold.template.dtos.meeting.MeetingHistoryResponseDTO;
import com.scaffold.template.entities.MeetingEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public interface MeetingService {

    CreateMeetingRequestDTO createMeeting(Long studentId, Long mentorId, Long matchId, LocalDate date, LocalTime time, int duration, String reason);
    List<MeetingHistoryResponseDTO> getMeetingHistoryResponseDTO(Long userId);

    void respondToMeeting(Long meetingId, boolean status);

    MeetingEntity getMeetingById(Long meetingId);
}
