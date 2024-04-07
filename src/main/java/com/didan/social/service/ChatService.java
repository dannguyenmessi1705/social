package com.didan.social.service;


import com.didan.social.dto.ConversationDTO;
import com.didan.social.dto.MessageDTO;
import com.didan.social.payload.request.SendMessageRequest;

import java.util.List;

public interface ChatService {
    // Tạo 1 phòng chat
    String createConversation(String conversationName) throws Exception;
    // Tham gia vào phòng chat
    ConversationDTO joinConversation(String conversationId) throws Exception;
    // Rời khỏi phòng chat
    boolean leaveConversation(String conversationId) throws Exception;
    // Gửi tin nhắn trong phòng chat
    MessageDTO sendMessage(String conversationId, SendMessageRequest sendMessageRequest) throws Exception;
    // Lấy ra tất cả nhóm chat đã tham gia
    List<ConversationDTO> getAllConversation() throws Exception;
    // Tìm kiếm nhóm chat để tham gia
    List<ConversationDTO> searchConversation(String conversationName) throws Exception;
    // Lấy ra tất cả đoạn chat trong nhóm chat
    List<MessageDTO> getAllMessagesInConversation(String conversationId) throws Exception;
}
