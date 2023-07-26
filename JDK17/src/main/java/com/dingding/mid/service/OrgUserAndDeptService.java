package com.dingding.mid.service;

/**
 * @author : willian fu
 * @version : 1.0
 */
public interface OrgUserAndDeptService {

    /**
     * 查询组织架构树
     * @param deptId 部门id
     * @return 组织架构树数据
     */
    Object getOrgTreeData(Integer deptId, String type);

    /**
     * 模糊搜索用户
     * @param userName 用户名/拼音/首字母
     * @return 匹配到的用户
     */
    Object getOrgTreeUser(String userName);

}
