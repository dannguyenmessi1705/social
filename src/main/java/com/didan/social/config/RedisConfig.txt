//package com.didan.social.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//
//@Configuration
//public class RedisConfig {
//    @Bean // Đưa lên Bean để sử dụng ở các class khác
//    public JedisConnectionFactory connectRedis(){ // Kết nối đến Redis
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(); // Cấu hình kết nối
//        config.setDatabase(0); // Chọn database
//        config.setHostName("redis-12204.c302.asia-northeast1-1.gce.cloud.redislabs.com"); // Địa chỉ host
//        config.setUsername("default"); // Tên đăng nhập
//        config.setPassword("LaBDA4NGWeKLKISxlH2IernqYFWfwKTG"); // Mật khẩu
//        config.setPort(12204); // Cổng kết nối
//        return new JedisConnectionFactory(config); // Trả về kết nối
//    }
//
//    @Bean // Đưa lên Bean để sử dụng ở các class khác
//    RedisTemplate<String, Object> redisTemplate(){ // Template để thao tác với Redis (thêm, sửa, xóa, lấy dữ liệu) với key kiểu String và value kiểu Object
//        RedisTemplate<String, Object> template = new RedisTemplate<>(); // Khởi tạo template,
//        template.setConnectionFactory(connectRedis()); // Kết nối đến Redis thông qua connectRedis ở trên
//        return template; // Trả về template để sử dụng
//    }
//}
