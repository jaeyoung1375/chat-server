package kr.co.chat.message.controller;

import kr.co.chat.message.dto.MessageResponseDto;
import kr.co.chat.message.dto.MessageSendRequestDto;
import kr.co.chat.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageStompController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/rooms/{roomId}/messages")
    public void sendMessage(@DestinationVariable Long roomId, @Payload MessageSendRequestDto request, Principal principal) throws Exception {

        Long userId = Long.parseLong(principal.getName());
        MessageResponseDto saved = messageService.sendMessage(userId, roomId, request);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, saved);


    }
}
