package com.dingding.mid.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
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
public class TemplateGroupVo {

    private Integer id;

    private String name;

    private List<Template> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Template{

        private String formId;

        private Integer tgId;

        private String formName;

        private String icon;

        private Boolean isStop;

        private String remark;
        private JSONObject logo;

        private String background;

        private String updated;
        private String templateId;
    }


}
