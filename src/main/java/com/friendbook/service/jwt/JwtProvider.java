package com.friendbook.service.jwt;

import com.friendbook.entity.UserSecurityDetail;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    private final String privateKey = "ducnhung"; // Khóa bí mật dùng để ký token

    // Phương thức tạo token JWT
    public String generateToken(Authentication authentication){
        Map<String, Object> map = new HashMap<>(); // Tạo một map để chứa các thông tin bổ sung cho token
        map.put("roles", authentication.getAuthorities());  // Thêm quyền của người dùng vào map

        // Lấy thông tin chi tiết người dùng từ đối tượng Authentication
        UserSecurityDetail userSecurityDetail = (UserSecurityDetail) authentication.getPrincipal();

        // Tạo token JWT với các thông tin:
        String token = Jwts.builder()
                .setId("ducnhung") // Đặt ID cho token
                .setExpiration(new Date(new Date().getTime() + 3600000))  // Đặt thời gian hết hạn cho token là 1 giờ (3600000 milliseconds)
                .setSubject(userSecurityDetail.getUsername())  // Đặt tên người dùng làm chủ đề của token
                .addClaims(map) // Thêm các claims vào token (ở đây là quyền của người dùng)
                .signWith(SignatureAlgorithm.HS384, privateKey) // Ký token với thuật toán HS384 và khóa bí mật
                .compact();  // Hoàn thành việc tạo token
        return token;
    }

    // Hoàn thành việc tạo token
    public Boolean validateToken(String token){
        try {
            // Cố gắng giải mã và kiểm tra chữ ký của token bằng khóa bí mật
            Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    // Phương thức lấy tên người dùng từ token
    public String getUsernameFormToken(String token){

        // Giải mã token bằng khóa bí mật và lấy chủ đề (tên người dùng) từ phần thân của token
        return Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody().getSubject();
    }
}
