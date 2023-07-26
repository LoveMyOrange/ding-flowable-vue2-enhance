package com.dingding.mid.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author LoveMyOrange
 * @create 2022-10-16 9:42
 */
@Schema(description = "评论的VO")
@Data
public class CommentVO {
    private String comments;
    private String userId;
    private String userName;
    private Date createTime;
}
