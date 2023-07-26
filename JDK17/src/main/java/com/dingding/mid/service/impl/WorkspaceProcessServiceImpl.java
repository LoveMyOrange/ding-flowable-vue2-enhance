package com.dingding.mid.service.impl;

import com.dingding.mid.service.SettingService;
import com.dingding.mid.service.WorkspaceProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Service
public class WorkspaceProcessServiceImpl implements WorkspaceProcessService {

    @Autowired
    private SettingService settingService;

    /**
     * 获取用户可见表单
     *
     * @return
     */
    @Override
    public Object getFormGroups(String name) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
//        String token = CookieUtil.getCookieValue(servletRequestAttributes.getRequest(), "token");

        return null;
    }
}
