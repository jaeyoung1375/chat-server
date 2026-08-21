package kr.co.chat.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSendRequestDto {

    @Schema(description = "메시지유형 (TEXT, IMAGE) — SYSTEM은 서버 전용이라 클라이언트가 보낼 수 없음")
    @Pattern(regexp = "TEXT|IMAGE", message = "messageType은 TEXT 또는 IMAGE만 가능합니다.")
    private String messageType;

    @Schema(description = "내용 (TEXT는 본문, IMAGE는 캡션으로 선택 사용)")
    @Size(max = 4000)
    private String content;

    @Schema(description = "첨부파일아이디 (IMAGE 메시지, CMM_FILE.FILE_ID — 업로드는 기존 /file/editor-image 재사용)")
    private Long fileId;
}
