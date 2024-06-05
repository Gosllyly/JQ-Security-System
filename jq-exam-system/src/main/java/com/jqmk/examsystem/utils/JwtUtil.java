package com.jqmk.examsystem.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

/**
 * @ClassName JwtUtil
 * @Author tian
 * @Date 2024/6/5 13:59
 * @Description token的相关设置
 */
public class JwtUtil {

    // 设置过期时间为24小时
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    // 设置jwt密钥
    private static final String SECRET = "secretOfJQMK";


    // 生成token
    public static String generateToken(String userName) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withAudience(userName) // 将用户名保存进token中
                .withExpiresAt(date) // 设置过期时间
                .sign(algorithm); // 加密方式
    }

    /**
     * 根据token解析用户名
     *
     * @param token
     * @return
     */
    public static String parseToken(String token) {
        try {
            String userName = JWT.decode(token).getAudience().get(0);
            return userName;
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 对 token 进行校验
     *
     * @param token
     * @return
     */
    public static boolean checkToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 判断是否过期
     *
     * @param token
     * @return
     */
    public static boolean isExpiration(String token) {
        try {
            return JWT.decode(token).getExpiresAt().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
