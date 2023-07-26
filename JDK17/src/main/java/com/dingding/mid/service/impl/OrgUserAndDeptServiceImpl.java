package com.dingding.mid.service.impl;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dingding.mid.common.R;
import com.dingding.mid.entity.Departments;
import com.dingding.mid.entity.Users;
import com.dingding.mid.service.DepartmentsService;
import com.dingding.mid.service.OrgUserAndDeptService;
import com.dingding.mid.service.UserService;
import com.dingding.mid.vo.OrgTreeVo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Service
public class OrgUserAndDeptServiceImpl implements OrgUserAndDeptService {

    @Resource
    private DepartmentsService departmentsService;

    @Resource
    private UserService userService;

    /**
     * 查询组织架构树
     *
     * @param deptId    部门id
     * @return 组织架构树数据
     */
    @Override
    public Object getOrgTreeData(Integer deptId, String type) {
        LambdaQueryWrapper<Users> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(deptId)) {
            LambdaQueryWrapper<Departments> departmentsLambdaQueryWrapper=new LambdaQueryWrapper<>();
            departmentsLambdaQueryWrapper.eq(Departments::getParentId, deptId);
            lambdaQueryWrapper.like(Users::getDepartmentIds,"%"+deptId+"%")
                .or()
                .like(Users::getDepartmentIds,"%" + deptId + ",%")
                .like(Users::getDepartmentIds,"%," + deptId + "%");


            List<OrgTreeVo> orgTreeVos = new LinkedList<>();

            departmentsService.list(departmentsLambdaQueryWrapper).forEach(dept -> {
                orgTreeVos.add(OrgTreeVo.builder()
                        .id(dept.getDeptId())
                        .name(dept.getDeptName())
                        .selected(false)
                        .type("dept")
                        .build());
            });
            userService.list(lambdaQueryWrapper).forEach(user -> {
                orgTreeVos.add(OrgTreeVo.builder()
                        .id(user.getUserId())
                        .name(user.getUserName())
                        .avatar(user.getAvatar())
                        .sex(user.getSex())
                        .type("user")
                        .selected(false)
                        .build());
            });
            return R.ok(orgTreeVos);
        }

        List<Users> users = userService.list();
        List<Departments> departments = departmentsService.list();
        //将人员按部门归类分组
        Map<Long, List<Users>> deptUsers = new HashMap<>();
        users.forEach(user -> {
            for (String did : user.getDepartmentIds().split(",")) {
                List<Users> usersList = deptUsers.get(Long.parseLong(did));
                if (null == usersList) {
                    List<Users> list = new ArrayList<>();
                    list.add(user);
                } else {
                    usersList.add(user);
                }
            }
        });
        //将部门及员工进行转换嵌套树形结构
        departments.forEach(dept -> {

        });
        return R.ok(Collections.EMPTY_LIST);
    }

    /**
     * 模糊搜索用户
     *
     * @param userName 用户名/拼音/首字母
     * @return 匹配到的用户
     */
    @Override
    public Object getOrgTreeUser(String userName) {
        LambdaQueryWrapper<Users> lambdaQueryWrapper=new LambdaQueryWrapper<>();

        if (Validator.isChinese(userName)) {
            lambdaQueryWrapper.like(Users::getUserName, "%" + userName + "%");
        } else {
            lambdaQueryWrapper.like(Users::getPingyin, "%" + userName + "%");
        }
        List<OrgTreeVo> list = new LinkedList<>();
        userService.list(lambdaQueryWrapper).forEach(user -> {
            list.add(OrgTreeVo.builder().type("user")
                    .sex(user.getSex())
                    .avatar(user.getAvatar())
                    .name(user.getUserName())
                    .id(user.getUserId())
                    .selected(false).build());
        });
        return R.ok(list);
    }
}
