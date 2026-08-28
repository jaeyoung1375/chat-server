package kr.co.chat.room.service;

import kr.co.chat.common.code.RoomErrorCode;
import kr.co.chat.common.exception.CustomException;
import kr.co.chat.common.util.DateUtil;
import kr.co.chat.room.dto.GroupRoomCreateRequestDto;
import kr.co.chat.room.dto.RoomDetailDto;
import kr.co.chat.room.dto.RoomDto;
import kr.co.chat.room.dto.RoomListItemDto;
import kr.co.chat.room.dto.RoomMemberDto;
import kr.co.chat.room.dto.RoomResponseDto;
import kr.co.chat.room.enums.MemberActivationResult;
import kr.co.chat.room.enums.RoomType;
import kr.co.chat.room.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomMapper roomMapper;

    @Transactional
    public RoomResponseDto createOrGetDirectRoom(Long userId, Long targetUserId) {

        if (userId.equals(targetUserId)) {
            throw new CustomException(RoomErrorCode.SELF_DIRECT_ROOM);
        }

        String directKey = buildDirectKey(userId, targetUserId);

        RoomDto existing = roomMapper.findByDirectKey(directKey);
        LocalDateTime now = DateUtil.now();
        if (existing != null) {

            List<Long> reactivated = ensureActiveMembers(existing.getRoomId(), userId, targetUserId, now);

            return RoomResponseDto.builder().roomId(existing.getRoomId()).reactivatedUserIds(reactivated).build();
        }

        RoomDto room = RoomDto.builder()
                .roomType(RoomType.DIRECT.name())
                .directKey(directKey)
                .regDt(now)
                .modDt(now)
                .build();

        try {

            roomMapper.insertRoom(room);
        } catch (DuplicateKeyException e) {
            // 동시 요청으로 다른 트랜잭션이 먼저 같은 DIRECT_KEY로 방을 만든 경우 — 그 방을 그대로 반환
            RoomDto raced = roomMapper.findByDirectKey(directKey);
            if (raced == null) throw e;

            List<Long> reactivated = ensureActiveMembers(raced.getRoomId(), userId, targetUserId, now);


            return RoomResponseDto.builder()
                    .roomId(raced.getRoomId())
                    .reactivatedUserIds(reactivated)
                    .build();
        }

        List<Long> reactivated = ensureActiveMembers(room.getRoomId(), userId, targetUserId, now);

        return RoomResponseDto.builder().roomId(room.getRoomId()).reactivatedUserIds(reactivated).build();
    }

    private String buildDirectKey(Long userId, Long targetUserId) {
        long min = Math.min(userId, targetUserId);
        long max = Math.max(userId, targetUserId);
        return min + "_" + max;
    }

    @Transactional
    public RoomResponseDto createGroupRoom(Long userId, GroupRoomCreateRequestDto request) {

        LocalDateTime now = DateUtil.now();

        RoomDto room = RoomDto.builder()
                .roomType(RoomType.GROUP.name())
                .roomName(request.getRoomName())
                .regDt(now)
                .modDt(now)
                .build();

        roomMapper.insertRoom(room);
        roomMapper.insertMember(room.getRoomId(), userId, now);

        for (Long memberUserId : request.getMemberUserIds()) {
            if (!memberUserId.equals(userId)) {
                roomMapper.insertMember(room.getRoomId(), memberUserId, now);
            }
        }

        return RoomResponseDto.builder().roomId(room.getRoomId()).build();
    }

    public List<RoomListItemDto> getMyRooms(Long userId) {
        return roomMapper.findRoomsByUserId(userId);
    }

    public RoomDetailDto getRoomDetail(Long userId, Long roomId) {

        validateMembership(roomId, userId);

        RoomDto room = roomMapper.findById(roomId);
        List<RoomMemberDto> members = roomMapper.findMembers(roomId);

        return RoomDetailDto.builder()
                .roomId(room.getRoomId())
                .roomType(room.getRoomType())
                .roomName(room.getRoomName())
                .members(members)
                .build();
    }

    @Transactional
    public void leaveRoom(Long userId, Long roomId) {
        validateMembership(roomId, userId);
        roomMapper.updateLeftAt(roomId, userId, DateUtil.now());
    }

    @Transactional
    public void markRead(Long userId, Long roomId, Long messageId) {
        validateMembership(roomId, userId);
        roomMapper.updateLastReadMessageId(roomId, userId, messageId);
    }

    // message 도메인 등 다른 도메인에서도 재사용하는 소유권 검증의 단일 출처라 public.
    public void validateMembership(Long roomId, Long userId) {
        if (roomMapper.countActiveMember(roomId, userId) == 0) {
            throw new CustomException(RoomErrorCode.NOT_ROOM_MEMBER);
        }
    }

    @Transactional
    public void touchLastMessageAt(Long roomId) {
        roomMapper.updateLastMessageAt(roomId, DateUtil.now());
    }

    private MemberActivationResult ensureActiveMember(Long roomId, Long userId, LocalDateTime now){

        RoomMemberDto findMember = roomMapper.findMember(roomId, userId);

        if (findMember == null) {
            roomMapper.insertMember(roomId, userId, now);
            return MemberActivationResult.INSERTED;
        } else if (findMember.getLeftAt() != null) {
            roomMapper.reactivateMember(roomId, userId, now);
            return MemberActivationResult.REACTIVATED;
        }

        return MemberActivationResult.ALREADY_ACTIVE;
    }

    private List<Long> ensureActiveMembers(Long roomId, Long userId, Long targetId, LocalDateTime now){
        List<Long> reactivated = new ArrayList<>();

        if (ensureActiveMember(roomId, userId, now) == MemberActivationResult.REACTIVATED) reactivated.add(userId);
        if (ensureActiveMember(roomId, targetId, now) == MemberActivationResult.REACTIVATED) reactivated.add(targetId);

        return reactivated;
    }
}
