package kr.co.chat.room.mapper;

import kr.co.chat.room.dto.RoomDto;
import kr.co.chat.room.dto.RoomListItemDto;
import kr.co.chat.room.dto.RoomMemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RoomMapper {

    /**
     * 채팅방 등록. insert 후 CHAT_ROOM_SEQ로 채번된 값이 dto.roomId에 채워짐
     */
    void insertRoom(RoomDto room);

    /**
     * 채팅방에 참여자 추가
     */
    void insertMember(@Param("roomId") Long roomId, @Param("userId") Long userId, @Param("joinedAt") LocalDateTime joinedAt);

    /**
     * roomId로 채팅방 조회
     */
    RoomDto findById(@Param("roomId") Long roomId);

    /**
     * DIRECT 방 중복 생성 방지용 키로 조회 (없으면 null)
     */
    RoomDto findByDirectKey(@Param("directKey") String directKey);

    /**
     * 내가 참여 중인(LEFT_AT IS NULL) 채팅방 목록 — 마지막 메시지·안읽음 수 포함
     */
    List<RoomListItemDto> findRoomsByUserId(@Param("userId") Long userId);

    /**
     * 채팅방 참여자 목록 (나간 사람 제외)
     */
    List<RoomMemberDto> findMembers(@Param("roomId") Long roomId);

    /**
     * userId가 roomId의 활성 참여자인지 (0/1)
     */
    int countActiveMember(@Param("roomId") Long roomId, @Param("userId") Long userId);

    /**
     * 채팅방 나가기 (LEFT_AT 갱신, 행 삭제하지 않음)
     */
    void updateLeftAt(@Param("roomId") Long roomId, @Param("userId") Long userId, @Param("leftAt") LocalDateTime leftAt);

    /**
     * 읽음 커서 갱신 (기존 값보다 작으면 무시)
     */
    void updateLastReadMessageId(@Param("roomId") Long roomId, @Param("userId") Long userId, @Param("messageId") Long messageId);

    /**
     * 방 목록 정렬용 마지막 메시지 시각 갱신
     */
    void updateLastMessageAt(@Param("roomId") Long roomId, @Param("now") LocalDateTime now);
}
