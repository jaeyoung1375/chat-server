package kr.co.chat.presence.controller;

import kr.co.chat.presence.dto.PresenceDto;
import kr.co.chat.presence.listener.PresenceSessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PresenceController {

    private final PresenceSessionRegistry registry;

    @SubscribeMapping("/presence/count")
    public PresenceDto getCurrentCount(){

        List<Long> userIds = registry.getUserIdList();

        return new PresenceDto(userIds.size(),userIds);
    }
}
