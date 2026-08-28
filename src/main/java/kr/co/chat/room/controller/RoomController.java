package kr.co.chat.room.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.chat.auth.dto.User;
import kr.co.chat.auth.service.AuthService;
import kr.co.chat.common.response.ApiResponse;
import kr.co.chat.common.util.SecurityUtil;
import kr.co.chat.message.dto.MessageResponseDto;
import kr.co.chat.message.service.MessageService;
import kr.co.chat.room.dto.DirectRoomCreateRequestDto;
import kr.co.chat.room.dto.GroupRoomCreateRequestDto;
import kr.co.chat.room.dto.MarkReadRequestDto;
import kr.co.chat.room.dto.RoomDetailDto;
import kr.co.chat.room.dto.RoomListItemDto;
import kr.co.chat.room.dto.RoomResponseDto;
import kr.co.chat.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@Tag(name = "Room", description = "채팅방")
public class RoomController {

    private final RoomService roomService;
    private final MessageService messageService;
    private final AuthService authService;
    private final SimpMessagingTemplate messagingTemplate;

    @Operation(summary = "1:1 채팅방 생성/조회", description = "상대방과의 DIRECT 방이 이미 있으면 기존 방을, 없으면 새로 만든 방을 반환한다.")
    @PostMapping("/direct")
    public ApiResponse<RoomResponseDto> createDirectRoom(@Valid @RequestBody DirectRoomCreateRequestDto request) {
        Long userId = SecurityUtil.getUserId();

        RoomResponseDto roomResponseDto = roomService.createOrGetDirectRoom(userId, request.getTargetUserId());

        for (Long reactivateUserId : roomResponseDto.getReactivatedUserIds()){
            User user = authService.findUser(reactivateUserId);
            String nickname = user.getNickname() != null ? user.getNickname() : user.getName();
            MessageResponseDto systemMessage = messageService.sendSystemMessage(roomResponseDto.getRoomId(), nickname + "님이 입장했습니다.");
            messagingTemplate.convertAndSend("/topic/room/" + roomResponseDto.getRoomId(), systemMessage);
        }


        return ApiResponse.ok(roomResponseDto);
    }

    @Operation(summary = "그룹 채팅방 생성")
    @PostMapping("/group")
    public ApiResponse<RoomResponseDto> createGroupRoom(@Valid @RequestBody GroupRoomCreateRequestDto request) {
        Long userId = SecurityUtil.getUserId();
        return ApiResponse.ok(roomService.createGroupRoom(userId, request));
    }

    @Operation(summary = "내 채팅방 목록", description = "참여 중인 방을 마지막 메시지 시각 최신순으로 반환한다.")
    @GetMapping
    public ApiResponse<List<RoomListItemDto>> getMyRooms() {
        Long userId = SecurityUtil.getUserId();
        return ApiResponse.ok(roomService.getMyRooms(userId));
    }

    @Operation(summary = "채팅방 상세", description = "참여자 목록을 포함한다.")
    @GetMapping("/{roomId}")
    public ApiResponse<RoomDetailDto> getRoomDetail(@PathVariable Long roomId) {
        Long userId = SecurityUtil.getUserId();
        return ApiResponse.ok(roomService.getRoomDetail(userId, roomId));
    }

    @Operation(summary = "채팅방 나가기")
    @PostMapping("/{roomId}/leave")
    public ApiResponse<Void> leaveRoom(@PathVariable Long roomId) {
        Long userId = SecurityUtil.getUserId();
        roomService.leaveRoom(userId, roomId);

        User user = authService.findUser(userId);
        String nickname = user.getNickname() != null ? user.getNickname() : user.getName();
        MessageResponseDto systemMessage = messageService.sendSystemMessage(roomId, nickname + "님이 퇴장했습니다.");
        messagingTemplate.convertAndSend("/topic/room/" + roomId, systemMessage);

        return ApiResponse.ok();
    }

    @Operation(summary = "읽음 처리", description = "요청한 메시지아이디까지 읽음 커서를 이동한다.")
    @PostMapping("/{roomId}/read")
    public ApiResponse<Void> markRead(@PathVariable Long roomId, @Valid @RequestBody MarkReadRequestDto request) {
        Long userId = SecurityUtil.getUserId();
        roomService.markRead(userId, roomId, request.getMessageId());
        return ApiResponse.ok();
    }
}
