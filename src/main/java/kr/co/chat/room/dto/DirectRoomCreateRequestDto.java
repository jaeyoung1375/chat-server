package kr.co.chat.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectRoomCreateRequestDto {

    @Schema(description = "상대방 회원아이디")
    @NotNull
    private Long targetUserId;
}
