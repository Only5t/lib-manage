package qiwen.com.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import qiwen.com.library.DTO.UmsUserDTO;
import qiwen.com.library.model.UmsUser;

import java.util.List;

@Mapper
public interface UmsUserMapper extends BaseMapper<UmsUser> {

    List<UmsUser> queryUsers(UmsUserDTO userDTO);

    List<UmsUser> selectByPage(UmsUserDTO userDTO);

    int delete(String ids);

    Integer updateStatus(Long id, Integer status);
}
