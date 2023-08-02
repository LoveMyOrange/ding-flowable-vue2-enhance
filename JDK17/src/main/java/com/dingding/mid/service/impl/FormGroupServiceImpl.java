package com.dingding.mid.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingding.mid.entity.FormGroups;
import com.dingding.mid.entity.Users;
import com.dingding.mid.mapper.FormGroupsMapper;
import com.dingding.mid.mapper.UsersMapper;
import com.dingding.mid.service.FormGroupService;
import com.dingding.mid.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Service
public class FormGroupServiceImpl extends ServiceImpl<FormGroupsMapper, FormGroups>  implements
    FormGroupService {


}
