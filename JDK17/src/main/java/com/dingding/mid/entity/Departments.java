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
 * (Departments)实体类
 *
 * @author makejava
 * @since 2020-09-17 14:21:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("departments")
public class Departments implements Serializable {
    private static final long serialVersionUID = -53409297247748263L;
    /**
     * 部门id
     */
    @TableId(value = "dept_id",type = IdType.ASSIGN_ID)
    private Long deptId;
    /**
     * 部门名
     */
    private String deptName;
    /**
     * 部门主管
     */
    private String leader;
    /**
     * 父部门id
     */
    private Integer parentId;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;

}
