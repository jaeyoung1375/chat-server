package kr.co.chat.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private Long roomId;
    private String roomType;
    private String roomName;
    private String directKey;
    private LocalDateTime lastMessageAt;
    private LocalDateTime regDt;
    private LocalDateTime modDt;
}
