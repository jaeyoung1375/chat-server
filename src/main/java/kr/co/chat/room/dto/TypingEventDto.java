package kr.co.chat.room.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypingEventDto {
    private Long userId;
    private boolean typing;
}
