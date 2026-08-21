package kr.co.chat.common.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum RoomErrorCode implements ResponseCode {

	/** 채팅방 참여자가 아닙니다. */
	NOT_ROOM_MEMBER("CR0001", HttpStatus.FORBIDDEN, "채팅방 참여자가 아닙니다."),
	/** 자기 자신과는 1:1 채팅방을 만들 수 없습니다. */
	SELF_DIRECT_ROOM("CR0002", HttpStatus.BAD_REQUEST, "자기 자신과는 1:1 채팅방을 만들 수 없습니다.");

	/** 코드 */
    private final String code;

    /** HttpStatus */
    private final HttpStatus httpStatus;

    /** 메시지 키 */
    private final String message;

}
