package kr.co.chat.common.file.controller;

import java.io.IOException;

import kr.co.chat.common.file.dto.FileResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.co.chat.common.file.service.FileService;
import kr.co.chat.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

	private final FileService fileService;

	@PostMapping("/file/upload")
	public ApiResponse<FileResponseDto> fileUpload(@RequestPart("file") MultipartFile file) throws IOException{

		FileResponseDto response = fileService.upload(file);

		return ApiResponse.ok(response);
	}

}
