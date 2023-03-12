package cn.iocoder.yudao.module.bpm.dal.dataobject.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 工作流的模型plus表.
 * 中肯的
 *
 * @author 芋道源码
 */
@TableName(value = "act_re_model_plus", autoResultMap = true)
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmModelPlusDO /*extends BaseDO*/ {

    /**
     * 编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 模型名
     */
    @TableField(value = "name_")
    private String name;
    /**
     * 模型key
     */
    @TableField(value = "key_")
    private String key;
    /**
     * 模型分类
     */
    private String category;
    @TableField(value = "version_")
    private Integer version;
    private String bpmnXml;
    private String formInfo;
    private String deploymentId;
    private String description;
    private String tenantId;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String creator;
    /**
     * 更新者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updater;
}
