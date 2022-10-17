package com.dingding.mid.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : willian fu
 * @date : 2020/9/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateGroupBo {

    private Integer id;

    private Integer groupId;

    private String groupName;

    private String templateId;

    private String templateName;

    private String icon;

    private Boolean isStop;

    private String remark;

    private Date updated;

    private String background;
}
