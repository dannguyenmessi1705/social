package com.didan.social.security;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Đánh dấu đây là class cấu hình
@EnableWebSecurity // Bật tính năng bảo mật trên Web, khi truy cập vào các route sẽ phải đi vào bộ
// lọc ở đây
public class CustomFilterSecurity {
    //    // Đới với User query từ Database
//    private final CustomUserDetailsService customUserDetailsService; // Khai báo biến customUserDetailsService để tiêm
//    // vào đây
//    @Autowired // Tiêm CustomUserDetailsService vào đây (tự động tìm kiếm và tiêm)
//    public CustomFilterSecurity(CustomUserDetailsService customUserDetailsService) { // Khởi tạo đối tượng
//        // CustomFilterSecurity
//        this.customUserDetailsService = customUserDetailsService;
//    }
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Khai báo biến customUserDetailsService để tiêm
    // vào đây
    @Autowired // Tiêm JwtAuthenticationFilter vào đây (tự động tìm kiếm và tiêm)
    public CustomFilterSecurity(JwtAuthenticationFilter jwtAuthenticationFilter) { // Khởi tạo đối tượng
        // CustomFilterSecurity
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    // Quy định các rules
    @Bean // Đánh dấu đây là Bean, ghi đè lên Bean mặc định của Spring Security
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Cấu hình bộ lọc
        http.cors(cors -> cors.disable()); // Tắt cors
        http.csrf(csrf -> csrf.disable()); // Tắt csrf
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Tắt Session
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/**", "/images/**")
                .permitAll()
                .anyRequest()
                .authenticated()
        ); // Cấu hình các route nào được bảo mật, không bảo mật
        http.exceptionHandling(except -> except
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    String jsonMessage = "{\"statusCode\": 401\n\t\"success\": \"false\"\n\t\"description\": \"Unauthorized or invalid access token\"}";
                    response.getWriter().write(jsonMessage);
                })
        ); // Bắt lỗi nếu không authorized được thì trả về message
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build(); // Trả về bộ lọc
    }

    // Định nghĩa lại Bean để tạo BcryptPasswordEncoder (Mã hóa mật khẩu)
    @Bean
    public PasswordEncoder passwordEncoder() { // Đánh dấu đây là Bean, ghi đè lên Bean mặc định của Spring Security
        return new BCryptPasswordEncoder(); // Trả về đối tượng BCryptPasswordEncoder
    }

//    // Tạo các User mặc định đưa lên Memory
//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager() { // Đánh dấu đây là Bean, ghi đè lên Bean mặc định của
//                                                             // Spring Security
//        UserDetails user1 = User.withUsername("USER1") // Tạo đối tượng UserDetails
//                .password(passwordEncoder().encode("123")) // Mã hóa mật khẩu
//                .roles("USER") // Phân quyền
//                .build(); // Tạo đối tượng
//        UserDetails user2 = User.withUsername("USER2") // Tạo đối tượng UserDetails
//                .password(passwordEncoder().encode("456")) // Mã hóa mật khẩu
//                .roles("USER") // Phân quyền
//                .build(); // Tạo đối tượng
//        UserDetails admin = User.withUsername("ADMIN") // Tạo đối tượng UserDetails
//                .password(passwordEncoder().encode("789")) // Mã hóa mật khẩu
//                .roles("ADMIN") // Phân quyền
//                .build(); // Tạo đối tượng
//        return new InMemoryUserDetailsManager(user1, user2, admin); // Trả về đối tượng InMemoryUserDetailsManager
//    }

//    // Đới với User query từ Database
//    private final CustomUserDetailsService customUserDetailsService; // Khai báo biến customUserDetailsService để tiêm
//    // vào đây
//    @Autowired // Tiêm CustomUserDetailsService vào đây (tự động tìm kiếm và tiêm)
//    public CustomFilterSecurity(CustomUserDetailsService customUserDetailsService) { // Khởi tạo đối tượng
//        // CustomFilterSecurity
//        this.customUserDetailsService = customUserDetailsService;
//    }
//    @Bean // Đánh dấu đây là Bean, ghi đè lên Bean mặc định của Spring Security
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception { // Định nghĩa lại Bean
//        // AuthenticationManager để Custom các User từ Database
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class); // Lấy ra đối tượng AuthenticationManagerBuilder từ HttpSecurity để tiêm vào đây
//        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder()); // Tiêm CustomUserDetailsService và PasswordEncoder vào đây để xác thực User, với PasswordEncoder là để mã hóa mật khẩu trước khi xác thực
//        // passwordEncoder(passwordEncoder()) dùng 2 lần vì có 2 đối tượng cần mã hóa mật khẩu là UserDetailsService và AuthenticationManagerBuilder trươc khi xác thực
//        return authenticationManagerBuilder.build(); // Trả về đối tượng AuthenticationManager
//
//    }

}
