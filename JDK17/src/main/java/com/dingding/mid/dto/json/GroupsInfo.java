package com.dingding.mid.dto.json;

import java.util.List;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 18:57
 */
@Data
public class GroupsInfo {
    private String groupType;
    private List<ConditionInfo> conditions;
    private List<String> cids;
}
