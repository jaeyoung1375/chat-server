package kr.co.chat.message.mapper;

import kr.co.chat.message.dto.MessageDto;
import kr.co.chat.message.dto.MessageResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    /**
     * 메시지 등록. insert 후 CHAT_MESSAGE_SEQ로 채번된 값이 dto.messageId에 채워짐
     */
    void insertMessage(MessageDto message);

    /**
     * messageId로 메시지 조회 (SENT_AT 등 DB 기본값을 그대로 돌려받기 위해 insert 직후 재조회할 때 사용)
     */
    MessageDto findById(@Param("messageId") Long messageId);

    /**
     * 방의 메시지를 최신순으로 조회 (beforeMessageId가 있으면 그보다 작은 것만 — 커서 페이지네이션)
     * userId의 JOINED_AT 이후 메시지만 반환 (재입장 전 히스토리는 노출하지 않음)
     */
    List<MessageResponseDto> findMessages(@Param("roomId") Long roomId,
                                           @Param("userId") Long userId,
                                           @Param("beforeMessageId") Long beforeMessageId,
                                           @Param("size") int size);
}
