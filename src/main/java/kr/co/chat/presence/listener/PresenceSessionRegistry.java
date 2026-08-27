package kr.co.chat.presence.listener;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PresenceSessionRegistry {

    private final Map<String, Long> sessionUserMap = new ConcurrentHashMap<>();

    public List<Long> connect(String sessionId, Long userId){
        sessionUserMap.put(sessionId, userId);

        return getUserIdList();
    }

    public List<Long> disconnect(String sessionId){
        sessionUserMap.remove(sessionId);

        return getUserIdList();
    }

    public List<Long> getUserIdList(){

        return sessionUserMap.values().stream().distinct().toList();
    }
}
