package com.didan.social.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // Đánh dấu đây là một Bean của Spring
public class ServerCommandLineRunner implements CommandLineRunner { // Chạy khi ứng dụng Spring Boot chạy
    private final SocketIOServer server; // Khai báo một server socket
    @Autowired // Tự động inject một Bean vào đây
    public ServerCommandLineRunner(SocketIOServer server){ // Inject server socket vào đây
        this.server = server;
    }
    @Override // Ghi đè phương thức
    public void run(String... args) throws Exception { // Chạy khi ứng dụng Spring Boot chạy
        server.start(); // Khởi động server socket
    }
}