package kr.co.chat.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class MessageResponseDto {

    @Schema(description = "메시지아이디")
    private Long messageId;

    @Schema(description = "채팅방아이디")
    private Long roomId;

    @Schema(description = "보낸사람 회원아이디")
    private Long senderId;

    @Schema(description = "보낸사람 표시이름 (목록 조회 시에만 채워짐)")
    private String senderName;

    @Schema(description = "메시지유형 (TEXT, IMAGE, SYSTEM)")
    private String messageType;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "첨부파일아이디 (IMAGE 메시지, CMM_FILE.FILE_ID)")
    private Long fileId;

    @Schema(description = "발송일시")
    private LocalDateTime sentAt;
}
