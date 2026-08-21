package kr.co.chat.room.dto;

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
public class RoomListItemDto {

    @Schema(description = "채팅방아이디")
    private Long roomId;

    @Schema(description = "채팅방유형 (DIRECT, GROUP)")
    private String roomType;

    @Schema(description = "표시 이름 (GROUP은 방이름, DIRECT는 상대방 닉네임/이름)")
    private String displayName;

    @Schema(description = "마지막 메시지 발송시각")
    private LocalDateTime lastMessageAt;

    @Schema(description = "마지막 메시지 내용 미리보기")
    private String lastMessageContent;

    @Schema(description = "안읽은 메시지 수")
    private int unreadCount;
}
