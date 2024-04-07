package com.didan.social.service.impl;

import com.didan.social.dto.ConversationDTO;
import com.didan.social.dto.MessageDTO;
import com.didan.social.entity.*;
import com.didan.social.entity.keys.ParticipantId;
import com.didan.social.payload.request.SendMessageRequest;
import com.didan.social.repository.ConversationRepository;
import com.didan.social.repository.MessageRepository;
import com.didan.social.repository.ParticipantRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.AuthorizePathService;
import com.didan.social.service.ChatService;
import com.didan.social.service.FileUploadsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {
    private final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final FileUploadsService fileUploadsService;
    private final MessageRepository messageRepository;
    private final AuthorizePathService authorizePathService;
    @Autowired
    public ChatServiceImpl(ConversationRepository conversationRepository,
                           UserRepository userRepository,
                           ParticipantRepository participantRepository,
                           FileUploadsService fileUploadsService,
                           MessageRepository messageRepository,
                           AuthorizePathService authorizePathService){
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.fileUploadsService = fileUploadsService;
        this.messageRepository = messageRepository;
        this.authorizePathService = authorizePathService;
    }
    @Override
    public String createConversation(String conversationName) throws Exception {
        String userId = authorizePathService.getUserIdAuthoried();
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        Conversations conversation = new Conversations();
        String conversationId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Date nowSql = Timestamp.valueOf(now);
        conversation.setConversationId(conversationId);
        conversation.setConversationName(conversationName);
        conversation.setCreatedAt(nowSql);
        conversationRepository.save(conversation);
        Participants participant = new Participants();
        participant.setUsers(user);
        participant.setConversations(conversation);
        participant.setParticipantId(new ParticipantId(conversationId, user.getUserId()));
        participantRepository.save(participant);
        return conversationId;
    }

    @Override
    public ConversationDTO joinConversation(String conversationId) throws Exception {
        String userId = authorizePathService.getUserIdAuthoried();
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
        if (findParticipant != null) {
            logger.error("User has already joined conversation");
            throw new Exception("User has already joined conversation");
        }
        Participants participant = new Participants();
        participant.setParticipantId(new ParticipantId(conversationId, user.getUserId()));
        participant.setConversations(conversation);
        participant.setUsers(user);
        participantRepository.save(participant);
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setConversationId(conversationId);
        conversationDTO.setConversationName(conversation.getConversationName());
        conversationDTO.setCreatedAt(conversation.getCreatedAt().toString());
        return conversationDTO;
    }

    @Override
    public boolean leaveConversation(String conversationId) throws Exception {
        String userId = authorizePathService.getUserIdAuthoried();
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        Conversations conversation = conversationRepository.findFirstByConversationId(conversationId);
        if (conversation == null){
            logger.error("There is no conversation");
            throw new Exception("There is no conversation");
        }
        Participants findParticipant = participantRepository.findFirstByConversations_ConversationIdAndUsers_UserId(conversationId, user.getUserId());
        if (findParticipant == null) {
            logger.error("User has not joined conversation yet");
            throw new Exception("User has not joined conversation yet");
        }
        participantRepository.delete(findParticipant);
        return true;
    }

    @Override
    public MessageDTO sendMessage(String conversationId, SendMessageRequest sendMessageRequest) throws Exception {
        String userId = authorizePathService.getUserIdAuthoried();
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
        return new MessageDTO(messageId,
                                sendMessageRequest.getContent(),
                                "message/"+fileName,
                                nowSql.toString(),
                                conversationId,
                                user.getUserId());
    }

    @Override
    public List<ConversationDTO> getAllConversation() throws Exception {
        String userId = authorizePathService.getUserIdAuthoried();
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        List<Participants> participants = participantRepository.findAllByUsers_UserId(user.getUserId());
        if (participants.size() <= 0){
            logger.error("You hasnot joined any conversations yet");
            throw new Exception("You hasnot joined any conversations yet");
        }
        List<ConversationDTO> conversationDTOs = new ArrayList<>();
        for (Participants participant : participants){
            ConversationDTO conversationDTO = new ConversationDTO();
            Conversations conversation = conversationRepository.findFirstByConversationId(participant.getConversations().getConversationId());
            conversationDTO.setConversationId(conversation.getConversationId());
            conversationDTO.setConversationName(conversation.getConversationName());
            conversationDTO.setCreatedAt(conversation.getCreatedAt().toString());
            conversationDTOs.add(conversationDTO);
        }
        Collections.sort(conversationDTOs, Comparator.comparing(ConversationDTO::getCreatedAt).reversed());
        return conversationDTOs;
    }

    @Override
    public List<ConversationDTO> searchConversation(String conversationName) throws Exception {
        List<ConversationDTO> conversationDTOs = new ArrayList<>();
        List<Conversations> conversations = conversationRepository.findAllByConversationNameContainingOrderByCreatedAtDesc(conversationName);
        if (conversations.size() <= 0) {
            logger.error("Not found conversations");
            throw new Exception("Not found conversations");
        }
        for (Conversations conversation : conversations){
            ConversationDTO conversationDTO = new ConversationDTO();
            conversationDTO.setConversationId(conversation.getConversationId());
            conversationDTO.setConversationName(conversation.getConversationName());
            conversationDTO.setCreatedAt(conversation.getCreatedAt().toString());
            conversationDTOs.add(conversationDTO);
        }
        return conversationDTOs;
    }

    @Override
    public List<MessageDTO> getAllMessagesInConversation(String conversationId) throws Exception {
        String userId = authorizePathService.getUserIdAuthoried();
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        Participants participants = participantRepository.findFirstByConversations_ConversationIdAndUsers_UserId(conversationId, user.getUserId());
        if (participants == null){
            logger.error("You hasnot joined any conversations yet");
            throw new Exception("You hasnot joined any conversations yet");
        }
        List<Messages> messages = messageRepository.findAllByConversations_ConversationIdOrderBySentAt(conversationId);
        if (messages.size() <= 0){
            return null;
        }
        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (Messages message : messages){
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageId(message.getMessageId());
            messageDTO.setContent(message.getContent());
            messageDTO.setMessageImg(message.getMessageImg());
            messageDTO.setSentAt(message.getSentAt().toString());
            messageDTO.setSenderId(message.getUsers().getUserId());
            messageDTO.setConversationId(conversationId);
            messageDTOs.add(messageDTO);
        }
        return messageDTOs;
    }
}
