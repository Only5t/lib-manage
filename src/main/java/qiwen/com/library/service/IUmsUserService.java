package qiwen.com.library.service;
import qiwen.com.library.DTO.UmsUserDTO;
import qiwen.com.library.DTO.UmsUserLoginDTO;
import qiwen.com.library.common.common.CommonPage;
import qiwen.com.library.common.common.CommonResult;
import qiwen.com.library.model.UmsUser;

import java.util.Map;

public interface IUmsUserService {

    int add(UmsUserDTO userDTO);

    int delete(String ids);

    UmsUser query(String id);

    CommonPage queryByPage(Integer pageNum, Integer pageSize, UmsUserDTO userDTO);

    int update(UmsUserDTO userDTO);

    UmsUser queryByUsernameAndPass(UmsUserLoginDTO userLoginDTO);

    Integer updateStatus(Long id, Integer status);

    CommonResult<UmsUser> login(UmsUserLoginDTO userLoginDTO);

    CommonResult<UmsUser> register(UmsUserLoginDTO userLoginDTO);

    Map<String, Object> userInfo();
}
