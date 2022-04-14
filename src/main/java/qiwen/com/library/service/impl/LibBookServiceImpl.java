package qiwen.com.library.service.impl;

import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwen.com.library.DTO.LibBookDTO;
import qiwen.com.library.common.common.CommonPage;
import qiwen.com.library.common.utils.RedisUtils;
import qiwen.com.library.mapper.LibBookMapper;
import qiwen.com.library.model.LibBook;
import qiwen.com.library.service.ILibBookService;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class LibBookServiceImpl implements ILibBookService {
    @Autowired
    LibBookMapper libBookMapper;
    @Autowired
    RedisUtils redisUtils;
    private static final Logger logger = LoggerFactory.getLogger(LibBookServiceImpl.class);

    @Override
    public int add(LibBookDTO libBookDTO) {
        LibBook libBook = new LibBook();
        BeanUtils.copyProperties(libBookDTO,libBook);
        libBook.setCreateTime(new Date());
        int insert = libBookMapper.insert(libBook);
        if (insert > 0) {
            setValue2Value(libBook);
        }
        return insert;
    }

    @Override
    public int delete(String id) {
        int delete = libBookMapper.deleteById(id);
        if (delete > 0) {
            redisUtils.del("book:" + id);
        }
        return delete;
    }

    @Override
    public LibBook query(String id) {
        LibBook libBook = null;
        try {
            libBook = (LibBook) redisUtils.get("book:" + id);
        }catch (Exception e){
            logger.error("get value from redis failed:",e);
        }
        if (libBook == null){
            libBook = libBookMapper.selectById(id);
            setValue2Value(libBook);
        }
        return libBook;
    }

    private void setValue2Value(LibBook libBook) {
        if (libBook != null){
            redisUtils.setex("book:" + libBook.getId(),libBook, new Random(24*60*60).nextInt(24*60*60*30));
        }
    }

    @Override
    public CommonPage queryByPage(Integer pageNum, Integer pageSize, LibBookDTO libBookDTO) {
        PageHelper.startPage(pageNum,pageSize);
        List<LibBook> list = list(libBookDTO);
        return CommonPage.restPage(list);
    }

    @Override
    public int update(LibBookDTO libBookDTO) {
        LibBook libBook = libBookMapper.selectById(libBookDTO.getId());
        if (libBook != null){
            BeanUtils.copyProperties(libBookDTO,libBook);
        }
        int update = libBookMapper.updateById(libBook);
        if (update > 0) {
            setValue2Value(libBook);
        }
        return update;
    }

    public List<LibBook> list(LibBookDTO libBookDTO) {
        return libBookMapper.list(libBookDTO);
    }
}
