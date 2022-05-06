package com.baojikouyu.teach.service.impl;

import com.baojikouyu.teach.mapper.TemplateMapper;
import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.pojo.Template;
import com.baojikouyu.teach.service.TemplateService;
import com.baojikouyu.teach.util.AsyncTasks;
import com.baojikouyu.teach.util.ShiroUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【template】的数据库操作Service实现
 * @createDate 2022-04-18 05:56:20
 */
@Service
@CacheConfig(cacheNames = "template", keyGenerator = "keyGenerator")
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template>
        implements TemplateService {


    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
//    @Lazy
    private AsyncTasks asyncTasks;

    @Override
    @CacheEvict(value = {"template"}, allEntries = true)
    public boolean save(Template entity) {
        final int insert = templateMapper.insert(entity);
        if (insert > 0) {
            return true;
        } else return false;
    }

    @Override
    @Cacheable
    public Page<Template> getPage(Integer start, Integer size, String name, Integer orderValue, Integer range, Integer folderGrade, Integer parentFolderId) {
        Integer qureyRange = Optional.ofNullable(range).orElse(0);

        LambdaQueryWrapper<Template> lambdaQuery = Wrappers.lambdaQuery(Template.class);
        lambdaQuery.like(!StringUtils.isEmpty(name), Template::getName, name);
        lambdaQuery.eq(folderGrade != null && folderGrade != 0, Template::getFolderGrade, folderGrade);
        lambdaQuery.eq(parentFolderId != null && parentFolderId != 0, Template::getParentFolderId, parentFolderId);
//        lambdaQuery.eq(Template::getParentFolderId, getParentFolder().getId());
        // 如果等于1 说明查自己 0 是查全部默认就是0
        lambdaQuery.eq(qureyRange.equals(1), Template::getCreater, ShiroUtil.getUserName());
//        lambdaQuery.orderByDesc(Template::getIsFolder);
        if (orderValue != null) {
            lambdaQuery.orderByDesc(orderValue.equals(0), Template::getIsFolder, Template::getName);
            lambdaQuery.orderByDesc(orderValue.equals(1), Template::getIsFolder, Template::getCreateTime);
        } else {
            lambdaQuery.orderByDesc(Template::getIsFolder, Template::getCreateTime);
        }

        return templateMapper.selectPage(new Page<>(start, size), lambdaQuery);
    }

    @Override
    @Cacheable
    public Template getParentFolder() {
        return templateMapper.selectOne(Wrappers.lambdaQuery(Template.class).eq(Template::getFolderGrade, 1));
    }


    @Override
    @Cacheable
    public Template getOneByParentFolderId(Integer parentFolderId) {
        return templateMapper.selectOne(Wrappers.lambdaQuery(Template.class).eq(Template::getId, parentFolderId));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"template"}, allEntries = true)
    public Boolean deletByIds(List<Template> templates) {
        for (Template template : templates) {
            final Boolean isSuccess = deletById(template);
            if (!isSuccess) {
                //这里是手动回滚了不需要在抛一个RuntimeException了 , 因为spring管理的事务只能捕获到RuntimeException
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        return true;
    }


    @Override
    @Transactional
    @CacheEvict(value = {"template"}, allEntries = true)
    public Boolean deletById(Template template) {

        final int i = templateMapper.deleteById(template.getId());

        if (template.getIsFolder()) {
            // 递归删除
            List<Template> templates = templateMapper.selectList(Wrappers.lambdaQuery(Template.class).eq(Template::getParentFolderId, template.getId()));
            asyncTasks.deleteTemplate(this, templateMapper, templates);
        }
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"template"}, allEntries = true)
    public Boolean deleteOne(Template template) {
        final int i = templateMapper.deleteById(template.getId());

        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Cacheable
    @Override
    public List<Template> getAllFolder() {

        LambdaQueryWrapper<Template> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Template::getIsFolder, true);
        return getTreeFolder(templateMapper.selectList(queryWrapper));
    }

    @Override
    @Cacheable
    public Template getOneById(Integer to) {
        return templateMapper.selectById(to);
    }

    @Override
    @CacheEvict(cacheNames = "template",
            allEntries = true)
    @Transactional
    public Boolean updateByIds(Integer[] ids, Integer to, Integer grade) {

        LambdaUpdateWrapper<Template> updateWrapper = Wrappers.lambdaUpdate(Template.class);
        updateWrapper.in(ids.length > 0, Template::getId, ids);
        updateWrapper.set(to != 0, Template::getParentFolderId, to);
        updateWrapper.set(grade != null, Template::getFolderGrade, grade);
        int update = templateMapper.update(null, updateWrapper);
        if (update > 0) {
            // 按道理来说应该是放到rabbitMq 中去
            List<Template> all = getBatchById(ids);
            final List<Template> allTemplateFolder = all.stream().filter(f -> f.getIsFolder()).collect(Collectors.toList());
            asyncTasks.updateTemplateFolder(this, allTemplateFolder);
            return true;
        } else return false;
    }

    @Override
    @Cacheable
    public List<Template> getChildrenByFolderParentId(Integer id) {
        return templateMapper.selectList(Wrappers.lambdaQuery(Template.class).eq(id != null, Template::getParentFolderId, id));
    }

    @Override
    @Cacheable
    public String countTemplate(String name, Integer range, Integer parentFolderId, Integer folderGrade) {
        LambdaQueryWrapper<Template> lambdaQuery = Wrappers.lambdaQuery(Template.class);
        lambdaQuery.like(!StringUtils.isEmpty(name), Template::getName, name);
        lambdaQuery.eq(folderGrade != null, Template::getFolderGrade, folderGrade);
        lambdaQuery.eq(range != null && range.equals(1), Template::getCreater, ShiroUtil.getUserName());
        lambdaQuery.eq(parentFolderId != null, Template::getParentFolderId, parentFolderId);
        lambdaQuery.eq(Template::getIsFolder, false);
        return templateMapper.selectCount(lambdaQuery).toString();
    }

    @Override
    @Cacheable
    public String countTemplateFolder(String name, Integer range, Integer parentFolderId, Integer folderGrade) {
        LambdaQueryWrapper<Template> lambdaQuery = Wrappers.lambdaQuery(Template.class);
        lambdaQuery.like(!StringUtils.isEmpty(name), Template::getName, name);
        lambdaQuery.eq(folderGrade != null, Template::getFolderGrade, folderGrade);
        lambdaQuery.eq(range != null && range.equals(1), Template::getCreater, ShiroUtil.getUserName());
        lambdaQuery.eq(parentFolderId != null, Template::getParentFolderId, parentFolderId);
        lambdaQuery.eq(Template::getIsFolder, true);
        return templateMapper.selectCount(lambdaQuery).toString();
    }


    @Cacheable
    public List<Template> getBatchById(Integer[] ids) {
        return templateMapper.selectBatchIds(Arrays.asList(ids));
    }

    public List<Template> getTreeFolder(List<Template> templateList) {
        ArrayList<Template> finalFiles = Lists.newArrayList();
        for (int i = 0; i < templateList.size(); i++) {

            Template template = templateList.get(i);

            for (int j = 0; j < templateList.size(); j++) {
                Template tmp = templateList.get(j);
                if (tmp.getParentFolderId() == template.getId()) {
                    template.getChildrenFolder().add(tmp);
                }
            }
            // 提取出父节点
            if (template.getParentFolderId() == null || template.getParentFolderId() == 0) {
                finalFiles.add(template);
            }
        }
        return finalFiles;
    }


    @Override
    @Transactional
    @CacheEvict(value = {"template"}, allEntries = true)
    public boolean updateById(Template entity) {
        int update = templateMapper.updateById(entity);
        if (update > 0) {
            return true;
        } else return false;
    }
}




