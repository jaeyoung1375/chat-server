package kr.co.chat.presence.listener;

import kr.co.chat.presence.dto.PresenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class PresenceEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final PresenceSessionRegistry registry;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event){

        // 1. sessionId 조회 (sessionId가 아닌 액세스토큰으로 할 경우 다중 탭에서 하나의 액세스토큰을 사용하기 때문에 하나의 탭에서 종료 시 해당 유저가 삭제되버림)
        String sessionId = SimpMessageHeaderAccessor.getSessionId(event.getMessage().getHeaders());

        // 2. userId 조회
        Long userId = Long.parseLong(event.getUser().getName());

        List<Long> userIds = registry.connect(sessionId, userId);

        // 4. 브로드캐스트
        messagingTemplate.convertAndSend("/topic/presence/count", new PresenceDto(userIds.size(),userIds));

    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event){

        // 1. sessionId 조회 (sessionId가 아닌 액세스토큰으로 할 경우 다중 탭에서 하나의 액세스토큰을 사용하기 때문에 하나의 탭에서 종료 시 해당 유저가 삭제되버림)
        String sessionId = SimpMessageHeaderAccessor.getSessionId(event.getMessage().getHeaders());

        List<Long> userIdList = registry.disconnect(sessionId);

        // 3. 브로드캐스트
        messagingTemplate.convertAndSend("/topic/presence/count", new PresenceDto(userIdList.size(), userIdList));
    }
}
