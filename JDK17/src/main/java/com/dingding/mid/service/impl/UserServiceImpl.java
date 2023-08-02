package com.dingding.mid.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingding.mid.entity.Users;
import com.dingding.mid.mapper.UsersMapper;
import com.dingding.mid.service.SettingService;
import com.dingding.mid.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UsersMapper, Users>  implements UserService{

}
