package qiwen.com.library.common.utils;


import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import qiwen.com.library.common.common.ResultCode;
import qiwen.com.library.common.dto.LoginUserDTO;
import qiwen.com.library.common.exception.ApiException;
import qiwen.com.library.common.model.Audience;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtil {
    private static Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String TOKEN_PREFIX = "#QIWENL#";
    /**
     * 解析jwt
     * @param jsonWebToken
     * @param base64Security
     * @return
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security) throws Exception {
        Claims claims = null;
        try {
            // 使用HS256加密算法
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            //生成签名密钥
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes,signatureAlgorithm.getJcaName());
            claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(jsonWebToken).getBody();

        }catch (ExpiredJwtException eje){
            log.error("===== Token过期 =====", eje);
            throw new ApiException(ResultCode.UNAUTHORIZED);
        }catch (Exception e){
            log.error("===== token解析异常 =====", e);
            throw new ApiException(ResultCode.AUTHORIZED_FAILED);
        }
        return claims;
    }
    /**
     * 构建jwt
     */
    public static String createJWT(LoginUserDTO user, Audience audience){
        String token = "";
        try {
            // 使用HS256加密算法
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            long nowMillis = System.currentTimeMillis();
            Date now  = new Date(nowMillis);
            //生成签名密钥
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(audience.getBase64Secret());
            Key signingKey = new SecretKeySpec(apiKeySecretBytes,signatureAlgorithm.getJcaName());
            //userId是重要信息，进行加密下
            String userJson = JSONObject.toJSONString(user);
            JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("typ","JWT")
                    //.claim("role",user.getRole())
                    .claim("user",userJson)   // 可以将基本不重要的对象信息放到claims
                    .setSubject(user.getUsername())     // 代表这个JWT的主体，即它的所有人
                    .setIssuer(audience.getName())      // 代表这个JWT的签发主体；
                    .setIssuedAt(new Date())       // 是一个时间戳，代表这个JWT的签发时间；
                    .setAudience(audience.getClientId())        // 代表这个JWT的接收对象；
                    .signWith(signatureAlgorithm,signingKey);
            int TTLMillis = audience.getExpiredSecond();    //添加Token过期时间
            if (TTLMillis >= 0){
                long expMillis = nowMillis + TTLMillis;
                Date exp = new Date(expMillis);
                jwtBuilder.setExpiration(exp)   // 是一个时间戳，代表这个JWT的过期时间；
                        .setNotBefore(now);     // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败
            }
            token = jwtBuilder.compact();
        }catch (Exception e){
            log.error("签名失败", e);
        }
        return token;
    }

    /**
     * 从token中获取用户名
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUsername(String token, String base64Security) throws Exception {
        return parseJWT(token,base64Security).getSubject();
    }

    /**
     * 从token中获取用户ID
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUserId(String token, String base64Security) throws Exception {
        String userId = parseJWT(token,base64Security).get("userId", String.class);
        try {
            return new String(Base64Utils.decode(userId.getBytes()),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Exception("===== userId解析异常 =====");
        }
    }

    /**
     * 是否已过期
     * @param token
     * @param base64Security
     * @return
     */
    public static boolean isExpiration(String token, String base64Security) throws Exception {
        return parseJWT(token,base64Security).getExpiration().before(new Date());
    }
}
