package cn.iocoder.yudao.module.bpm.dal.dataobject.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 工作流的模型plus表.
 * 中肯的
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_form", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmModelPlusDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 模型名
     */
    private String name;
    /**
     * 模型key
     */
    private String key;
    /**
     * 模型分类
     */
    private String category;
    private Integer version;
    private String bpmnXml;
    private String formInfo;
    private String deploymentId;
    private String description;
    private String tenantId;



}
