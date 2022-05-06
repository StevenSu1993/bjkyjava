package com.baojikouyu.teach.service;

import com.baojikouyu.teach.pojo.Template;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【template】的数据库操作Service
 * @createDate 2022-04-18 05:56:20
 */
public interface TemplateService extends IService<Template> {

    Page<Template> getPage(Integer start, Integer size, String name, Integer orderValue, Integer range, Integer folderGrade, Integer parentFolderId);

    Template getParentFolder();

    Template getOneByParentFolderId(Integer parentFolderId);

    Boolean deletById(Template template);

    Boolean deleteOne(Template template);

    List<Template> getAllFolder();

    Template getOneById(Integer to);

    Boolean updateByIds(Integer[] ids, Integer to, Integer garde);

    List<Template> getChildrenByFolderParentId(Integer id);

    String countTemplate(String name, Integer range, Integer parentFolderId, Integer folderGrade);

    String countTemplateFolder(String name, Integer range, Integer parentFolderId, Integer folderGrade);

    Boolean deletByIds(List<Template> templates);
}
