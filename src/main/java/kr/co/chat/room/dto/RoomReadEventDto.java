package kr.co.chat.room.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomReadEventDto {
    private Long roomId;
    private Long lastReadMessageId;
}
