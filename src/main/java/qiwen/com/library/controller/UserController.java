package qiwen.com.library.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qiwen.com.library.DTO.UmsUserDTO;
import qiwen.com.library.DTO.UmsUserLoginDTO;
import qiwen.com.library.common.annotation.JwtIgnore;
import qiwen.com.library.common.common.CommonPage;
import qiwen.com.library.common.common.CommonResult;
import qiwen.com.library.model.UmsUser;
import qiwen.com.library.service.IUmsUserService;

import java.util.Map;

/**
 * 后台用户管理Controller
 * Created by qiwenl on 2018/4/26.
 */
@RestController
@RequestMapping(value = "/user")
@Api(value = "用户管理",tags = "用户管理")
public class UserController {

    @Autowired
    IUmsUserService userService;

    @ApiOperation(value = "用户管理-用户添加")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public int add(@RequestBody UmsUserDTO userDTO){
        return userService.add(userDTO);
    }

    @ApiOperation(value = "用户管理-用户删除")
    @ApiImplicitParam(name = "ids",value = "用户id,可以多个",required = true,dataType = "string",paramType = "query")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.POST)
    public int delete(@PathVariable("ids") String ids){
        return userService.delete(ids);
    }

    @ApiOperation(value = "用户管理-用户查询")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "string",paramType = "query")
    @RequestMapping(value = "/query/{id}",method = RequestMethod.POST)
    public UmsUser query(@PathVariable("id") String id){
        return userService.query(id);
    }

    @ApiOperation(value = "用户管理-分页查询")
    @RequestMapping(value = "/queryUserByPage",method = RequestMethod.POST)
    public CommonPage queryUserByPage(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        UmsUserDTO userDTO = new UmsUserDTO();
        return userService.queryByPage(pageNum,pageSize,userDTO);
    }

    @ApiOperation(value = "用户管理-用户更新")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public int update(@RequestBody UmsUserDTO userDTO){
        return userService.update(userDTO);
    }

    @ApiOperation(value = "用户管理-更改用户状态")
    @RequestMapping(value = "/updateStatus/{id}",method = RequestMethod.POST)
    public Integer updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status ){
        return userService.updateStatus(id,status);
    }

    @ApiOperation(value = "登陆接口")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @JwtIgnore
    private CommonResult<UmsUser> login(@RequestBody UmsUserLoginDTO userLoginDTO){
        return userService.login(userLoginDTO);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "登出接口")
    @JwtIgnore
    public CommonResult logout() {
        return new CommonResult(null);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册接口")
    @JwtIgnore
    public CommonResult<UmsUser> register(@RequestBody UmsUserLoginDTO userLoginDTO) {
        return userService.register(userLoginDTO);
    }

    @GetMapping("/userInfo")
    @ApiOperation(value = "获取用户信息")
    public Map<String, Object> userInfo() {
        return userService.userInfo();
    }
}
