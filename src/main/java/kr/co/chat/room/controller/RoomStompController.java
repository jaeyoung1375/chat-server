package kr.co.chat.room.controller;

import kr.co.chat.room.dto.MarkReadRequestDto;
import kr.co.chat.room.dto.RoomReadEventDto;
import kr.co.chat.room.dto.TypingEventDto;
import kr.co.chat.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class RoomStompController {

    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/rooms/{roomId}/read")
    public void markRead(@DestinationVariable Long roomId, @Payload MarkReadRequestDto request, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        roomService.markRead(userId, roomId, request.getMessageId());

        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/read", new RoomReadEventDto(userId, request.getMessageId()));

    }

    @MessageMapping("/rooms/{roomId}/typing")
    public void typing(@DestinationVariable Long roomId, @Payload TypingEventDto event, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        event.setUserId(userId);

        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/typing", event);
    }
}
