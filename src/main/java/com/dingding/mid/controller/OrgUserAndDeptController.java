package com.dingding.mid.controller;

import com.dingding.mid.service.OrgUserAndDeptService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : willian fu
 * @version : 1.0
 */
@RestController
@RequestMapping("/oa/org")
public class OrgUserAndDeptController {

    @Resource
    private OrgUserAndDeptService orgService;

    /**
     * 查询组织架构树
     * @param deptId 部门id
     * @return 组织架构树数据
     */
    @GetMapping("tree")
    public Object getOrgTreeData(@RequestParam(defaultValue = "0") Integer deptId,
                                 @RequestParam(defaultValue = "user") String type
    ){
        return orgService.getOrgTreeData(deptId, type);
    }

    /**
     * 模糊搜索用户
     * @param userName 用户名/拼音/首字母
     * @return 匹配到的用户
     */
    @GetMapping("tree/user/search")
    public Object getOrgTreeUser(@RequestParam String userName){
        return orgService.getOrgTreeUser(userName.trim());
    }

}
