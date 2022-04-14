package qiwen.com.library.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import qiwen.com.library.DTO.UmsUserDTO;
import qiwen.com.library.DTO.UmsUserLoginDTO;
import qiwen.com.library.common.common.CommonPage;
import qiwen.com.library.common.common.CommonResult;
import qiwen.com.library.common.dto.LoginUserDTO;
import qiwen.com.library.common.model.Audience;
import qiwen.com.library.common.utils.JwtTokenUtil;
import qiwen.com.library.mapper.UmsUserMapper;
import qiwen.com.library.model.UmsUser;
import qiwen.com.library.service.IUmsUserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UmsUserServiceImpl implements IUmsUserService {
    @Autowired
    UmsUserMapper userMapper;
    @Autowired
    IUmsUserService umsUserService;
    @Autowired
    Audience audience;

    private static final Logger logger = LoggerFactory.getLogger(UmsUserServiceImpl.class);


    @Override
    public int add(UmsUserDTO userDTO) {
        UmsUser user = new UmsUser();
        BeanUtils.copyProperties(userDTO,user);
        user.setCreateTime(new Date());
        user.setIsDelete(1);
        return userMapper.insert(user);
    }

    @Override
    public int delete(String ids) {
        return userMapper.delete(ids);
    }

    @Override
    public UmsUser query(String id) {
        return queryById(Long.valueOf(id));
    }

    @Override
    public CommonPage queryByPage(Integer pageNum, Integer pageSize, UmsUserDTO userDTO) {
        PageHelper.startPage(pageNum,pageSize);
        List<UmsUser> list = userMapper.selectByPage(userDTO);
        return CommonPage.restPage(list);
    }

    @Override
    public int update(UmsUserDTO userDTO) {
        UmsUser user = queryById(userDTO.getId());
        BeanUtils.copyProperties(userDTO,user);
        return userMapper.updateById(user);
    }

    private UmsUser queryById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public UmsUser queryByUsernameAndPass(UmsUserLoginDTO userLoginDTO) {
        UmsUserDTO userDTO = new UmsUserDTO();
        userDTO.setUsername(userLoginDTO.getUsername());
        userDTO.setPassword(userLoginDTO.getPassword());
        List<UmsUser> users = queryUsers(userDTO);
        if (users != null && users.size() > 0){
            return users.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Integer updateStatus(Long id, Integer status) {
        return userMapper.updateStatus(id,status);
    }

    private List<UmsUser> queryUsers(UmsUserDTO userDTO) {
        return userMapper.queryUsers(userDTO);
    }

    @Override
    public CommonResult<UmsUser> login(UmsUserLoginDTO userLoginDTO) {
        CommonResult commonResult = new CommonResult();
        UmsUser user = queryByUsernameAndPass(userLoginDTO);
        if (user != null){
            LoginUserDTO loginUserDTO = new LoginUserDTO();
            BeanUtils.copyProperties(user,loginUserDTO);
            commonResult.setCode(200);
            commonResult.setMessage("success");
            // 创建token
            String token = JwtTokenUtil.createJWT(loginUserDTO,audience);
            logger.info("### 登录成功, token={} ###", token);
            // 将token放在响应头
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            token = user.getId() + JwtTokenUtil.TOKEN_PREFIX + token;
            response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY,token);
            // 将token响应给客户端
            user.setPassword("***");
            commonResult.setMessage(token);
            commonResult.setData(user);
        }else {
            commonResult.setCode(-1);
            commonResult.setMessage("登陆失败，用户名或密码错误");
        }
        return commonResult;
    }

    @Override
    public CommonResult<UmsUser> register(UmsUserLoginDTO userLoginDTO) {
        CommonResult commonResult = new CommonResult();
        UmsUser user = queryByUsernameAndPass(userLoginDTO);
        if (user == null){
            UmsUserDTO userDTO = new UmsUserDTO();
            userDTO.setUsername(userLoginDTO.getUsername());
            userDTO.setPassword(userLoginDTO.getPassword());
            userDTO.setStatus(1);
            int insert = add(userDTO);
            if (insert != 0){
                commonResult.setCode(200);
                commonResult.setMessage("注册成功");
            }else {
                commonResult.setCode(-1);
                commonResult.setMessage("注册失败");
            }
            return commonResult;
        }else {
            commonResult.setCode(-1);
            commonResult.setMessage("注册失败，用户已存在");
        }
        return commonResult;
    }

    @Override
    public Map<String, Object> userInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        String token = authHeader.split(JwtTokenUtil.TOKEN_PREFIX)[1];
        UmsUser user = null;
        try {
            Claims claims = JwtTokenUtil.parseJWT(token, audience.getBase64Secret());
            user = JSONObject.parseObject(claims.get("user").toString(), UmsUser.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> data  = new HashMap<>();
        if (user != null){
            data.put("username",user.getUsername());
            data.put("icon",user.getIcon());
        }
        return data;
    }
}
