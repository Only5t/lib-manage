package qiwen.com.library.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qiwen.com.library.DTO.LibBookDTO;
import qiwen.com.library.common.common.CommonPage;
import qiwen.com.library.model.LibBook;
import qiwen.com.library.service.ILibBookService;

/**
 * 书籍管理Controller
 */
@RestController
@RequestMapping(value = "/book")
@Api(value = "书籍管理",tags = "书籍管理")
public class LibBookController {

    @Autowired
    ILibBookService libBookService;

    @ApiOperation(value = "书籍管理-添加")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public int add(@RequestBody LibBookDTO libBookDTO){
        return libBookService.add(libBookDTO);
    }

    @ApiOperation(value = "书籍管理-删除")
    @ApiImplicitParam(name = "id",value = "用户id,可以多个",required = true,dataType = "string",paramType = "path")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public int delete(@PathVariable("id") String id){
        return libBookService.delete(id);
    }

    @ApiOperation(value = "书籍管理-根据id查询")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "string",paramType = "path")
    @RequestMapping(value = "/query/{id}",method = RequestMethod.POST)
    public LibBook query(@PathVariable("id") String id){
        return libBookService.query(id);
    }

    @ApiOperation(value = "书籍管理-分页查询")
    @RequestMapping(value = "/queryByPage",method = RequestMethod.POST)
    public CommonPage queryUserByPage(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                                      @RequestBody LibBookDTO libBookDTO){
        return libBookService.queryByPage(pageNum,pageSize,libBookDTO);
    }

    @ApiOperation(value = "书籍管理-更新")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public int update(@RequestBody LibBookDTO libBookDTO){
        return libBookService.update(libBookDTO);
    }


}
