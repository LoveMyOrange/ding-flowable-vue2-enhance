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
 * (FormGroups)实体类
 *
 * @author makejava
 * @since 2020-09-21 22:49:33
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "form_groups")
public class FormGroups implements Serializable {
    private static final long serialVersionUID = -50696449296875480L;
    
    @TableId(value = "group_id",type = IdType.AUTO)
    /**
    * id
    */
    private Integer groupId;
    /**
    * 组名
    */
    private String groupName;
    /**
    * 排序号
    */
    private Integer sortNum;
    /**
    * 创建时间
    */
    private Date created;
    /**
    * 更新时间
    */
    private Date updated;

}
