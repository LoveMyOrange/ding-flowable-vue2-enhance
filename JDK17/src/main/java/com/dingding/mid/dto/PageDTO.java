package com.dingding.mid.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 10:33
 */
@Data
@Schema(description = "分页DTO")
public class PageDTO {
    @Schema(name = "第几页",required = true)
    private Integer pageNo;
    @Schema(name  = "多少条",required = true)
    private Integer pageSize;
}
