package kr.co.chat.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkReadRequestDto {

    @Schema(description = "마지막으로 읽은 메시지아이디까지 읽음 커서를 이동")
    @NotNull
    private Long messageId;
}
