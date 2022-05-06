package com.baojikouyu.teach.controller;

import cn.hutool.core.date.DateTime;
import com.baojikouyu.teach.annotation.Log;
import com.baojikouyu.teach.constant.rabbitMqConstant;
import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.pojo.Template;
import com.baojikouyu.teach.service.TemplateService;
import com.baojikouyu.teach.util.ShiroUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.jna.platform.win32.ObjBase;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TemplateController {


    @Autowired
    private TemplateService templateService;

    @Log
    @GetMapping("/auth/getAllTemplate")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean getAllTemplate(Integer start, Integer size, String name, Integer orderValue, Integer range, Integer folderGrade, Integer parentFolderId) {
        HashMap<String, Object> map = new HashMap<>();
        Page<Template> page = templateService.getPage(start, size, name, orderValue, range, folderGrade, parentFolderId);
        map.put("page", page);
        // 统计模板总数 和文件夹总数
        if (parentFolderId != null || folderGrade != null) {
            String countTemplate = templateService.countTemplate(name, range, parentFolderId, folderGrade);
            String countTemplateFolder = templateService.countTemplateFolder(name, range, parentFolderId, folderGrade);
            map.put("countTemplate", countTemplate);
            map.put("countTemplateFolder", countTemplateFolder);
        }
        return new ResponseBean(200, "查询模板", map);
    }

    @Log
    @GetMapping("/auth/getAllTemplateFolder")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean getAllTemplateFolder() {

        List<Template> filesList = templateService.getAllFolder();
        return new ResponseBean(200, "成功获取所有模板文件夹", filesList);
    }

    @Log
    @GetMapping("/auth/getTempalteParentFolderId")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean getTempalteParentFolderId(Integer curentFolderId) {

        final Template oneByParentFolderId = templateService.getOneByParentFolderId(curentFolderId);
        return new ResponseBean(200, "成功获取所有模板文件夹", oneByParentFolderId);
    }


    @Log
    @GetMapping("/auth/moveTemplateToFolder")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean moveTemplateToFolder(Integer[] ids, Integer to) {

        if (ids.length < 1 || to == null) {
            return new ResponseBean(400, "请求参数有误，请核对后在提交", "请求参数有误，请核对后在提交");
        }
        Template template = templateService.getOneById(to);
        if (template != null) {
            Boolean isSucess = templateService.updateByIds(ids, to, template.getFolderGrade() + 1);
            if (isSucess) {
                // TODO 后续增加rabbitMq操作
                // 根据ids 查询出所有的文件夹，然后放到rabbitmq 中去更新数据库操作。，为啥放到中间件中呢，
                // 如果文件夹下的文件很多在这里做更新grade 操作那么效率是非常慢的。用户体验相当不好
//                List<Template> all = templateService.getBatchById(ids);
//                final List<Template> allFolder = all.stream().filter(f -> f.getIsFolder()).collect(Collectors.toList());
//                recursion(AllFolder);
//                rabbitTemplate.convertAndSend(rabbitMqConstant.UPDATEGRADEEXCHANGE, rabbitMqConstant.UPDATEGRADEROUTINGKEY, allFolder);
                return new ResponseBean(200, "修改成功", "移动成功");
            } else {
                return new ResponseBean(400, "修改失败", "修改失败");
            }
        } else {
            return new ResponseBean(400, "不好意思您操作的目录已经被别人删除了", "不好意思您操作的目录已经被别人删除了");

        }
    }


    @Log
    @PostMapping("/auth/deleteTemplate")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean deleteTemplate(@RequestBody Template template) {

        Boolean isSuccess = templateService.deletById(template);

        if (isSuccess) {
            return new ResponseBean(200, "删除成功", template.getName());
        } else
            return new ResponseBean(400, "删除失败", template.getName());
    }

    @Log
    @PostMapping("/auth/deleteTemplateByIds")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean deleteTemplateByIds(@RequestBody List<Template> templates) {

        Boolean isSuccess = templateService.deletByIds(templates);

        if (isSuccess) {
            return new ResponseBean(200, "删除成功", isSuccess);
        } else
            return new ResponseBean(400, "删除失败", isSuccess);
    }


    @Log
    @PostMapping("/auth/createTemplate")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean createTemplate(@RequestBody Template template) {

        String userName = ShiroUtil.getUserName();
        Integer userId = ShiroUtil.getUserId();
        template.setCreater(userName);
        template.setCreaterId(userId);
        template.setCreateTime(DateTime.now());

        Integer parentFolderId = template.getParentFolderId();
        if (parentFolderId != null) {
            Template parentTemplate = templateService.getOneByParentFolderId(parentFolderId);
            template.setFolderGrade(parentTemplate.getFolderGrade() + 1);
        }
        templateService.save(template);

        return new ResponseBean(200, "创建成功", template.getName());
    }

    @Log
    @PostMapping("/auth/updateTemplate")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean updateTemplate(@RequestBody Template template) {

        if (template.getCreater() == null) {
            String userName = ShiroUtil.getUserName();
            template.setCreater(userName);
        }
        if (template.getCreaterId() == null) {
            Integer userId = ShiroUtil.getUserId();
            template.setCreaterId(userId);
        }
        if (template.getCreateTime() == null) {
            template.setCreateTime(DateTime.now());
        }

        templateService.updateById(template);
        return new ResponseBean(200, "修改成功", template.getName());
    }


    @Log
    @PostMapping("/auth/createTemplateFolder")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean createTemplateFolder(@RequestBody Template template) {

        String userName = ShiroUtil.getUserName();
        Integer userId = ShiroUtil.getUserId();
        template.setCreater(userName);
        template.setCreaterId(userId);
        template.setCreateTime(DateTime.now());
        template.setIsFolder(true);
        if (template.getParentFolderId() != null) {
            Template parentTemplate = templateService.getOneByParentFolderId(template.getParentFolderId());
            template.setFolderGrade(parentTemplate.getFolderGrade() + 1);
        } else {
            // 设置目录级别
            template.setFolderGrade(2);
            Template parentTemplate = templateService.getParentFolder();
            template.setParentFolderId(parentTemplate.getId());
        }
        templateService.save(template);
        return new ResponseBean(200, "修改成功", template.getName());
    }


}
