package kr.co.chat.configuration;

import kr.co.chat.auth.util.JwtTokenUtil;
import kr.co.chat.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private static final Pattern ROOM_TOPIC_PATTERN = Pattern.compile("^/topic/room/(\\d+)(?:/.*)?$");

    private final JwtTokenUtil jwtTokenUtil;
    private final RoomService roomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authenticate(accessor);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            authorizeRoomSubscription(accessor);
        }

        return message;
    }

    private void authenticate(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")){
            throw new MessagingException("인증 토큰이 없습니다.");
        }

        String token = authHeader.substring(7);
        Long userId = jwtTokenUtil.getUserId(token);

        Principal principal = () -> String.valueOf(userId);
        accessor.setUser(principal);
    }

    private void authorizeRoomSubscription(StompHeaderAccessor accessor){
        Long roomId = extractRoomId(accessor.getDestination());
        if (roomId == null) return;

        Long userId = Long.parseLong(accessor.getUser().getName());
        roomService.validateMembership(roomId, userId);
    }

    private Long extractRoomId(String destination) {
        if (destination == null) return null;
        Matcher matcher = ROOM_TOPIC_PATTERN.matcher(destination);
        if (!matcher.matches()) return null;
        return Long.parseLong(matcher.group(1));

    }

}
