package qiwen.com.library.service;

import qiwen.com.library.DTO.LibBookDTO;
import qiwen.com.library.DTO.UmsRoleDTO;
import qiwen.com.library.common.common.CommonPage;
import qiwen.com.library.model.LibBook;
import qiwen.com.library.model.UmsMenu;
import qiwen.com.library.model.UmsRole;

import java.util.List;

public interface ILibBookService {

    int add(LibBookDTO libBookDTO);

    int delete(String id);

    LibBook query(String id);

    CommonPage queryByPage(Integer pageNum, Integer pageSize, LibBookDTO libBookDTO);

    int update(LibBookDTO libBookDTO);

}
