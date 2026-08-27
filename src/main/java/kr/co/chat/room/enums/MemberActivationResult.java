package kr.co.chat.room.enums;

public enum MemberActivationResult {
    
    INSERTED, // 최초 참여 (신규 방 생성 시 ) - 알림 대상 아님
    REACTIVATED, // 나갔다가 재입장 - 알림 대상
    ALREADY_ACTIVE // 이미 활성 멤버, 변화없음 - 알림 대상 아님
}
