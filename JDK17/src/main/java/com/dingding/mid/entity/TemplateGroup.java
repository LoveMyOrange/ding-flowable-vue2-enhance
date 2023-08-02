package com.dingding.mid.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (TemplateGroup)实体类
 *
 * @author makejava
 * @since 2020-09-21 22:50:41
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "template_group")
public class TemplateGroup implements Serializable {
    private static final long serialVersionUID = 296474043591142508L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String templateId;

    private Integer groupId;

    private Integer sortNum;

    private Date created;
}
