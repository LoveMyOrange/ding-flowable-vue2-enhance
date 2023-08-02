package com.dingding.mid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dingding.mid.entity.TemplateGroup;
import com.dingding.mid.entity.TemplateGroupBo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author : willian fu
 * @date : 2020/9/21
 */
@Mapper
public interface TemplateGroupMapper extends BaseMapper<TemplateGroup> {

    /**
     * 查询所有表单及组
     * @return
     */
    @Select("SELECT fg.group_id, tg.id, fg.group_name, pt.template_id, pt.remark, pt.is_stop, pt.updated, pt.template_name, " +
            "pt.icon, pt.background FROM process_templates pt LEFT JOIN template_group tg ON pt.template_id = tg.template_id\n" +
            "RIGHT JOIN form_groups fg ON tg.group_id = fg.group_id ORDER BY fg.sort_num ASC, tg.sort_num ASC")
    List<TemplateGroupBo> getAllFormAndGroups();
}
