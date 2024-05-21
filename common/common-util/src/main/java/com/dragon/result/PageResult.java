package com.dragon.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页数据集合
     */
    private List records;
}
