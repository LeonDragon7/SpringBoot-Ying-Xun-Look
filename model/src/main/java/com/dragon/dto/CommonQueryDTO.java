package com.dragon.dto;

import lombok.Data;

@Data
public class CommonQueryDTO {
    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;
}
