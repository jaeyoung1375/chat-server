package kr.co.chat.common.file.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class FileResponseDto {

	@Schema(description = "파일아이디")
	private long fileId;

	@Schema(description = "원본파일명")
	private String orgFileNm;

}
