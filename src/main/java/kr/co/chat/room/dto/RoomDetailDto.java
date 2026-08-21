package kr.co.chat.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RoomDetailDto {

    @Schema(description = "채팅방아이디")
    private Long roomId;

    @Schema(description = "채팅방유형 (DIRECT, GROUP)")
    private String roomType;

    @Schema(description = "방이름 (GROUP만 존재, DIRECT는 null)")
    private String roomName;

    @Schema(description = "참여자 목록 (나간 사람 제외)")
    private List<RoomMemberDto> members;
}
