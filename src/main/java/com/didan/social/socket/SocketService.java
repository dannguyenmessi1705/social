package com.didan.social.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.didan.social.dto.MessageDTO;
import com.didan.social.entity.Conversations;
import com.didan.social.entity.Messages;
import com.didan.social.entity.Participants;
import com.didan.social.entity.Users;
import com.didan.social.payload.request.SendMessageRequest;
import com.didan.social.repository.ConversationRepository;
import com.didan.social.repository.MessageRepository;
import com.didan.social.repository.ParticipantRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.ChatService;
import com.didan.social.service.FileUploadsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service // Đánh dấu đây là một service
public class SocketService { // Khai báo một service để xử lý logic
    private final Logger logger = LoggerFactory.getLogger(SocketService.class);
    private final ChatService chatService; // Khai báo một service để xử lý logic
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;
    private final MessageRepository messageRepository;
    private final FileUploadsService fileUploadsService;
    @Autowired // Đánh dấu đây là một dependency và Spring sẽ tự động inject vào
    public SocketService(ChatService chatService,
                         UserRepository userRepository,
                         ConversationRepository conversationRepository,
                         ParticipantRepository participantRepository,
                         FileUploadsService fileUploadsService,
                         MessageRepository messageRepository){ // Inject service vào
        this.chatService = chatService; // Gán service
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.fileUploadsService = fileUploadsService;
        this.messageRepository = messageRepository;
    }

    public void sendSocketMessage(String conversationId, String eventName, SocketIOClient senderClient, MessageDTO message){ // Hàm gửi tin nhắn qua socket cho tất cả client trong phòng
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(conversationId).getClients() // Lấy ra tất cả client trong phòng
        ) {
            if(!client.getSessionId().toString().equals(senderClient.getSessionId().toString())){ // Nếu client không phải là người gửi
                client.sendEvent(eventName, message); // Gửi tin nhắn qua socket cho client
            }
        }
    }

    public void saveMessage(String userId, String conversationId, String eventName, SocketIOClient senderClient, SendMessageRequest sendMessageRequest) throws Exception{ // Hàm lưu tin nhắn vào database và gửi lại tin nhắn đó cho tất cả client khác trong phòng
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        Conversations conversation = conversationRepository.findFirstByConversationId(conversationId);
        if (conversation == null) {
            logger.error("There is no conversation");
            throw new Exception("There is no conversation");
        }
        Participants findParticipant = participantRepository.findFirstByConversations_ConversationIdAndUsers_UserId(conversationId, user.getUserId());
        if (findParticipant == null) {
            logger.error("User has not joined conversation yet");
            throw new Exception("User has not joined conversation yet");
        }
        Messages message = new Messages();
        String messageId = UUID.randomUUID().toString();
        message.setMessageId(messageId);
        message.setContent(sendMessageRequest.getContent());
        String fileName = null;
        if (sendMessageRequest.getMessageImg() != null && !sendMessageRequest.getMessageImg().isEmpty()){
            fileName = fileUploadsService.storeFile(sendMessageRequest.getMessageImg(), "message", messageId);
            message.setMessageImg("message/"+fileName);
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Date nowSql = Timestamp.valueOf(now);
        message.setSentAt(nowSql);
        message.setUsers(user);
        message.setConversations(conversation);
        messageRepository.save(message);
        MessageDTO messageDTO =
                new MessageDTO(messageId,
                sendMessageRequest.getContent(),
                "message/"+fileName,
                nowSql.toString(),
                conversationId,
                user.getUserId());
        sendSocketMessage(conversationId, eventName, senderClient, messageDTO); // Gửi lại tin nhắn đó cho tất cả client khác trong phòng
    }

    public void saveInfoMessage(String roomId, String eventName, SocketIOClient senderClient, String message){ // Hàm lưu tin nhắn thông báo đã kết nối và gửi đó cho tất cả client khác trong phòng
        sendSocketMessage(roomId, eventName, senderClient, new MessageDTO()); // Gửi tin nhắn qua socket cho tất cả client trong phòng
    }
}
