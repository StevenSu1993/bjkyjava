package com.baojikouyu.teach.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baojikouyu.teach.pojo.Directory;
import com.baojikouyu.teach.service.DirectoryService;
import com.baojikouyu.teach.mapper.DirectoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Administrator
 * @description 针对表【directory】的数据库操作Service实现
 * @createDate 2022-03-25 07:14:59
 */
@Service
@CacheConfig(cacheNames = "directory", keyGenerator = "keyGenerator")
public class DirectoryServiceImpl extends ServiceImpl<DirectoryMapper, Directory>
        implements DirectoryService {

    @Autowired
    private DirectoryMapper directoryMapper;

    /*这中清理缓存的方式不是太好，最好是双清。 有可能还会造成数据脏读的。策略就是延时双清*/
    @Override
    @Transactional
    @CacheEvict(value = {"directory"},
            allEntries = true)
    public Boolean saveDirectory(Directory directory) {
        int insert = directoryMapper.insert(directory);
        if (!(insert > 0))
            return false;
        else return true;
    }
}




