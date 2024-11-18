package __project.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String SECRET = "c38d6b0b229795b333c74c62fca5b1aed48f8760c040bd95f9cc37ab6d8e1d9400774c4457238a964fa727b2a7d2ec566ea7f676d8f357e7ff97b93e955be16ee26361ac7036a0602610287572226e4862ea6fdf365ca2c771cb6fb67a8c1be71e862891897fe5fa572099b268f23e75047a74ccefafb7dad49e72c0bf40eb25ececeecf666fb0e3ce72046ea12b727f80670f6399af55b9d97edbd7bf293c1a9439f6d7ed9c1fe8b7a2c8740809cb7c7bcbdbd8136bebd0fe034d9ff4760c3a5ef3b742796e41e04923835490895bd189f78f5ae0b2c0efcf0067afdbf1e41a1dd8ee74162255deeb971b71fabba8d9821d672b9e33aafca4c680998b9f0837";

    public static String generateJwt(int userId) {
        // Generating a JWT
        String jwt = Jwts.builder()
                .claim("userId", Integer.toString(userId))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        return jwt;
    }

    public static int verifyJwt(String jwt) {
        JwtParser parser = Jwts.parser().setSigningKey(SECRET).build();
        Claims claims = parser.parseClaimsJws(jwt).getBody();
        return Integer.parseInt(claims.get("userId").toString());
    }
}
