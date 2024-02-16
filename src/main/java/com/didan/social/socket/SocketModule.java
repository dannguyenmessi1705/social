package com.didan.social.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.didan.social.entity.Messages;
import com.didan.social.payload.request.SendMessageRequest;
import com.didan.social.repository.UserRepository;
import com.didan.social.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SocketModule {
    private Logger logger = LoggerFactory.getLogger(SocketModule.class);
    private final SocketIOServer server; // Khai báo một server socket
    private final SocketService socketService; // Khai báo một service để xử lý logic
    private final JwtUtils jwtUtils;
    @Autowired
    public SocketModule(SocketIOServer server, SocketService socketService, JwtUtils jwtUtils){ // Inject server socket và service vào
        this.jwtUtils = jwtUtils;
        this.server = server; // Gán server socket
        this.socketService = socketService; // Gán service
        server.addConnectListener(onConnected()); // Thêm listener khi có client kết nối
        server.addDisconnectListener(onDisconnected()); // Thêm listener khi có client ngắt kết nối
        server.addEventListener("send_message", SendMessageRequest.class, onChatReceived()); // Thêm listener khi có client gửi tin nhắn, với tên sự kiện là send_message và kiểu dữ liệu là Chat
    }

    private DataListener<SendMessageRequest> onChatReceived(){ // Hàm xử lý khi có tin nhắn được gửi đến
        return (senderClient, data, ackServer) -> { // Trả về một listener xử lý khi có tin nhắn được gửi đến
            String accessToken = senderClient.getHandshakeData().getSingleUrlParam("token");
            try {
                jwtUtils.validateAccessToken(accessToken);
                String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
                String conversationId = senderClient.getHandshakeData().getSingleUrlParam("conversationID"); // Lấy ra các tham số và giá trị của URL mà client gửi lên
                logger.info(String.format("%s[%s] -> %s",email, senderClient.getSessionId().toString(), data.toString())); // In ra màn hình console thông tin của session và tin nhắn được gửi đến
                socketService.saveMessage(email, conversationId, "get_message", senderClient, data); // Lưu tin nhắn vào database và gửi lại tin nhắn đó cho tất cả client khác trong phòng qua hàm saveMessage của service
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        };
    }

    private ConnectListener onConnected(){ // Hàm xử lý khi có client kết nối
        return (client) -> { // Trả về một listener xử lý khi có client kết nối
            String accessToken = client.getHandshakeData().getSingleUrlParam("token");
            try {
                jwtUtils.validateAccessToken(accessToken);
                String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
                String conversationId = client.getHandshakeData().getSingleUrlParam("conversationID"); // Lấy ra các tham số và giá trị của URL mà client gửi lên
                client.joinRoom(conversationId); // Thêm client vào phòng chat với id là roomId
                socketService.saveInfoMessage(conversationId, "get_message", client, String.format("%s joined to chat", email)); // Lưu tin nhắn thông báo đã kết nối và gửi đó cho tất cả client khác trong phòng qua hàm saveInfoMessage của service
                logger.info(String.format("Socket ID[%s] - conversation ID[%s] - email[%s]  Connected to chat module through", client.getSessionId().toString(), conversationId, email)); // In ra màn hình console thông tin của session, phòng và tên của client vừa kết nối
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        };
    }

    private DisconnectListener onDisconnected(){ // Hàm xử lý khi có client ngắt kết nối
        return (client) -> { // Trả về một listener xử lý khi có client kết nối
            String accessToken = client.getHandshakeData().getSingleUrlParam("token");
            try {
                jwtUtils.validateAccessToken(accessToken);
                String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
                String conversationId = client.getHandshakeData().getSingleUrlParam("conversationID"); // Lấy ra các tham số và giá trị của URL mà client gửi lên
                client.leaveRoom(conversationId);
                socketService.saveInfoMessage(conversationId, "get_message", client, String.format("%s disconnected", email)); // Lưu tin nhắn thông báo đã kết nối và gửi đó cho tất cả client khác trong phòng qua hàm saveInfoMessage của service
                logger.info(String.format("Socket ID[%s] - conversation ID[%s] - email[%s]  Disconnected to chat module through", client.getSessionId().toString(), conversationId, email)); // In ra màn hình console thông tin của session, phòng và tên của client vừa kết nối
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        };
    }
}