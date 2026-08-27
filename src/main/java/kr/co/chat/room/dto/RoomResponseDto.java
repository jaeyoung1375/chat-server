package kr.co.chat.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RoomResponseDto {

    @Schema(description = "채팅방아이디")
    private Long roomId;

    private List<Long> reactivatedUserIds;
}
