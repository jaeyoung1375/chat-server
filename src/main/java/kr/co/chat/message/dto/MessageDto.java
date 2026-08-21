package kr.co.chat.message.dto;

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
public class MessageDto {

    private Long messageId;
    private Long roomId;
    private Long senderId;
    private String messageType;
    private String content;
    private Long fileId;
    private LocalDateTime sentAt;
    private String deletedYn;
}
