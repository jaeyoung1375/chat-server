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
public class RoomMemberDto {

    @Schema(description = "회원아이디")
    private Long userId;

    @Schema(description = "표시 이름 (닉네임 우선, 없으면 이름)")
    private String displayName;

    @Schema(description = "입장일시")
    private LocalDateTime joinedAt;

    private String leftAt;
}
