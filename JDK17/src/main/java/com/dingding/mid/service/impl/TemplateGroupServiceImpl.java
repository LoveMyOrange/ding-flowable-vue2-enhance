package com.dingding.mid.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingding.mid.entity.TemplateGroup;
import com.dingding.mid.entity.Users;
import com.dingding.mid.mapper.TemplateGroupMapper;
import com.dingding.mid.mapper.UsersMapper;
import com.dingding.mid.service.TemplateGroupService;
import com.dingding.mid.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Service
public class TemplateGroupServiceImpl extends ServiceImpl<TemplateGroupMapper, TemplateGroup>  implements
    TemplateGroupService {

}
