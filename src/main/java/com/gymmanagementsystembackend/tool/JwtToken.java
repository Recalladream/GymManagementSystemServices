package com.gymmanagementsystembackend.tool;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gymmanagementsystembackend.exception.BusinessException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtToken {
    /*
     * 公用密钥-保存在服务端，客户端是不知道密钥的，以防被攻击
     * */
    public static String SECRET = "kkkoke2021";

    // String username, String password
    public static String creatToken(String account, long id) throws Exception {
        // 签发时间
        Date iatDate = new Date();

        // 过期时间
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DAY_OF_WEEK, 1);
        Date expiresDate = nowTime.getTime();

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HMAC256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map) // header
                .withClaim("username", account) // payload
                .withClaim("identityId", id)
                .withExpiresAt(expiresDate) // 设置过期时间。过期时间要大于签发时间
                .withIssuedAt(iatDate) // 设置签发时间
                .sign(Algorithm.HMAC256(SECRET)); // 加密

        return token;
    }

    /*
     * 解密Token
     * */
    public static Map<String, Claim> verifyToken(String token) throws Exception {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw new BusinessException(Code.login_outtime,"登录凭证已过去，请重新登录");
        }

        return jwt.getClaims();
    }
}
