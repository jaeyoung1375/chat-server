package kr.co.chat.presence.controller;

import kr.co.chat.presence.dto.PresenceCountDto;
import kr.co.chat.presence.listener.PresenceSessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PresenceController {

    private final PresenceSessionRegistry registry;

    @SubscribeMapping("/presence/count")
    public PresenceCountDto getCurrentCount(){
        return new PresenceCountDto(registry.count());
    }
}
