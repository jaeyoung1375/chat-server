package kr.co.chat.presence.listener;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PresenceSessionRegistry {

    private final Map<String, Long> sessionUserMap = new ConcurrentHashMap<>();

    public long connect(String sessionId, Long userId){
        sessionUserMap.put(sessionId, userId);

        return count();
    }

    public long disconnect(String sessionId){
        sessionUserMap.remove(sessionId);

        return count();
    }

    public long count(){
        return sessionUserMap.values().stream().distinct().count();
    }
}
