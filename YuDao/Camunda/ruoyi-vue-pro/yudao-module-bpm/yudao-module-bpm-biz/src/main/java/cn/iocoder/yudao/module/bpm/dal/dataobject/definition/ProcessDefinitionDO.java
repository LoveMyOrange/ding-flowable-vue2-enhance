package cn.iocoder.yudao.module.bpm.dal.dataobject.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.bpm.enums.definition.BpmModelFormTypeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.List;

/**
 *
 * @author 芋道源码
 */
@TableName(value = "act_re_procdef", autoResultMap = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDefinitionDO  {

    @TableId(type = IdType.NONE,value = "ID_")
    private String id;
    @TableField(value = "VERSION_")
    private  int version;
    @TableField(value = "SUSPENSION_STATE_")
    private int suspensionState;
    @TableField(value = "DEPLOYMENT_ID_")
    private String deploymentId;
}
