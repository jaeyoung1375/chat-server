package kr.co.chat.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.chat.common.response.ApiResponse;
import kr.co.chat.common.util.SecurityUtil;
import kr.co.chat.message.dto.MessageResponseDto;
import kr.co.chat.message.dto.MessageSendRequestDto;
import kr.co.chat.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms/{roomId}/messages")
@Tag(name = "Message", description = "채팅 메시지")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "메시지 목록 조회", description = "beforeMessageId보다 작은 메시지를 최신순으로 size개 조회한다(커서 페이지네이션). beforeMessageId를 생략하면 가장 최근 메시지부터.")
    @GetMapping
    public ApiResponse<List<MessageResponseDto>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long beforeMessageId,
            @RequestParam(defaultValue = "30") int size) {
        Long userId = SecurityUtil.getUserId();
        return ApiResponse.ok(messageService.getMessages(userId, roomId, beforeMessageId, size));
    }

    @Operation(summary = "메시지 전송", description = "WebSocket 실시간 브로드캐스트는 아직 없다 — REST로 저장만 수행한다.")
    @PostMapping
    public ApiResponse<MessageResponseDto> sendMessage(
            @PathVariable Long roomId,
            @Valid @RequestBody MessageSendRequestDto request) {
        Long userId = SecurityUtil.getUserId();
        return ApiResponse.ok(messageService.sendMessage(userId, roomId, request));
    }
}
