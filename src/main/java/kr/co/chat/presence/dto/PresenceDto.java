package kr.co.chat.presence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor

public class PresenceDto {

    private long count;

    private List<Long> userIds;
}
