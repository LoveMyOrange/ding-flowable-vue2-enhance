package com.dingding.mid.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author LoveMyOrange
 * @create 2022-10-16 9:42
 */
@ApiModel("评论的VO")
@Data
public class CommentVO {
    private String comments;
    private String userId;
    private String userName;
    private Date createTime;
}
