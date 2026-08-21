package kr.co.chat.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupRoomCreateRequestDto {

    @Schema(description = "방이름")
    @NotBlank
    private String roomName;

    @Schema(description = "초대할 회원아이디 목록 (요청자 본인은 자동 포함되므로 제외 가능)")
    @NotEmpty
    private List<Long> memberUserIds;
}
