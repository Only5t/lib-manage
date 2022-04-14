package qiwen.com.library.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import qiwen.com.library.common.annotation.JwtIgnore;
import qiwen.com.library.common.model.Audience;
import qiwen.com.library.common.utils.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);
    @Autowired
    private Audience audience;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getServletPath();
        if (url.contains("/actuator/health")) {
            return true;
        }
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        // swagger
        if (url.contains("/swagger-resources")) {
            return true;
        }
        // swagger
        if (url.contains("swagger-ui")) {
            return true;
        }
        // swagger
        if (url.contains("images")) {
            return true;
        }
        if (url.contains("/login/login")) {
            return true;
        }
        if (url.contains("/configuration")) {
            return true;
        }
        // 忽略带JwtIgnore注解的请求, 不做后续token认证校验
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            JwtIgnore jwtIgnore = handlerMethod.getMethodAnnotation(JwtIgnore.class);
            if (jwtIgnore != null){
                return true;
            }
        }

        // 获取请求头信息authorization信息
        final String authHeader = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        log.info("## authHeader= {}", authHeader);
        if (StringUtils.isEmpty(authHeader) || !authHeader.contains(JwtTokenUtil.TOKEN_PREFIX)){
            log.info("### 用户未登录，请先登录 ###" + url);
            throw new Exception("### 用户未登录，请先登录 ###");
        }
        String loginId = authHeader.split(JwtTokenUtil.TOKEN_PREFIX)[0];
        String token = authHeader.split(JwtTokenUtil.TOKEN_PREFIX)[1];
        if (audience == null){
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            audience = (Audience) factory.getBean("audience");

        }
        // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
        Claims claims = JwtTokenUtil.parseJWT(token, audience.getBase64Secret());
        JSONObject jsonObject = JSONObject.parseObject(claims.get("user").toString());
        if (jsonObject.getString("id") != null && loginId != null && loginId.equals(jsonObject.getString("id"))){
            return true;
        }
        return false;
    }
}
