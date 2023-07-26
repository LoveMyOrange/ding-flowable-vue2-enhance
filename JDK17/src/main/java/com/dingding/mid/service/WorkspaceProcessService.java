package com.dingding.mid.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author : willian fu
 * @version : 1.0
 */
public interface WorkspaceProcessService  {

    /**
     * 获取用户可见表单
     * @return
     */
    Object getFormGroups(String name);

}
