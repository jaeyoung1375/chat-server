package kr.co.chat.message.service;

import kr.co.chat.common.file.service.FileService;
import kr.co.chat.common.util.DateUtil;
import kr.co.chat.message.dto.MessageDto;
import kr.co.chat.message.dto.MessageResponseDto;
import kr.co.chat.message.dto.MessageSendRequestDto;
import kr.co.chat.message.mapper.MessageMapper;
import kr.co.chat.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageMapper messageMapper;

    private final RoomService roomService;

    private final FileService fileService;

    public List<MessageResponseDto> getMessages(Long userId, Long roomId, Long beforeMessageId, int size) {
        roomService.validateMembership(roomId, userId);

        List<MessageResponseDto> response = messageMapper.findMessages(roomId, beforeMessageId, size);

        response.forEach(message -> {
            if(message.getFileId() != null){
                message.setRealFilePath(fileService.getUploadPath(message.getFileId()));
            }
        });
        return response;
    }

    @Transactional
    public MessageResponseDto sendMessage(Long userId, Long roomId,  MessageSendRequestDto request) throws IOException {

        roomService.validateMembership(roomId, userId);

        MessageDto message = MessageDto.builder()
                .roomId(roomId)
                .senderId(userId)
                .messageType(request.getMessageType())
                .content(request.getContent())
                .fileId(request.getFileId())
                .sentAt(DateUtil.now())
                .build();

        messageMapper.insertMessage(message);
        roomService.touchLastMessageAt(roomId);

        MessageDto saved = messageMapper.findById(message.getMessageId());

        return MessageResponseDto.builder()
                .messageId(saved.getMessageId())
                .roomId(saved.getRoomId())
                .senderId(saved.getSenderId())
                .messageType(saved.getMessageType())
                .content(saved.getContent())
                .fileId(saved.getFileId())
                .sentAt(saved.getSentAt())
                .build();
    }
}
