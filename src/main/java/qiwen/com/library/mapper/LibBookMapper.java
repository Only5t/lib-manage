package qiwen.com.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import qiwen.com.library.DTO.LibBookDTO;
import qiwen.com.library.model.LibBook;

import java.util.List;

@Mapper
public interface LibBookMapper extends BaseMapper<LibBook> {

    List<LibBook> list(LibBookDTO libBookDTO);

}
