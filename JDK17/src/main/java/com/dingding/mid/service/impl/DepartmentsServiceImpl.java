package com.dingding.mid.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingding.mid.entity.Departments;
import com.dingding.mid.mapper.DepartmentsMapper;
import com.dingding.mid.service.DepartmentsService;
import org.springframework.stereotype.Service;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Service
public class DepartmentsServiceImpl extends ServiceImpl<DepartmentsMapper, Departments>  implements
    DepartmentsService {

}
