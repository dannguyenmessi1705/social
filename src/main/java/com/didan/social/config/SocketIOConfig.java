package com.didan.social.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    @Value("${socket-server.port}") // Lấy giá trị từ application.properties
    private Integer port;
    @Value("${socket-server.host}") // Lấy giá trị từ application.properties
    private String host;

    @Bean // Khai báo là một Bean để sử dụng ở những nơi khác
    public SocketIOServer socketIOServer(){ // Khởi tạo một server socket
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration(); // Tạo một cấu hình mới
        config.setHostname(host); // Đặt host name
        config.setPort(port); // Đặt port
//        config.setContext("/socket.io"); // Đặt context để truy cập socket server từ client
        return new SocketIOServer(config); // Trả về một server socket mới
    }
}
