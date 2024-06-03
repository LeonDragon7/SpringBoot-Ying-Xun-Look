package com.dragon.controller;


import com.dragon.result.Result;
import com.dragon.service.FavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 收藏表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Api(tags = "收藏相关接口")
@Slf4j
@RestController
@RequestMapping("/client/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    /**
     * 点击收藏
     * @param id 视频id
     * @return
     */
    @ApiOperation("点击收藏")
    @GetMapping("/collect")
    public Result<Integer> collect(@RequestParam Integer id){
        Integer favorite = favoriteService.collect(id);
        return Result.success(favorite);
    }
}

