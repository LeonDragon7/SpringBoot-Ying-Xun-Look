package com.dragon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragon.custom.LoginUserInfoHelper;
import com.dragon.entity.Favorite;
import com.dragon.mapper.FavoriteMapper;
import com.dragon.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    /**
     * 点击收藏
     * @param id 视频id
     * @return
     */
    @Override
    public Integer collect(Integer id) {
        //1. 获取当前用户id
        Integer userId = LoginUserInfoHelper.getUser().getId();
        //2. 根据用户id获取收藏数据
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getIsDelete,0)
                .eq(Favorite::getVideoId,id);
        Favorite favorite = getOne(wrapper);
        //3. 收藏视频不为空
        if(!BeanUtil.isEmpty(favorite)){
            favorite.setVisible(0);
            favorite.setCount(favorite.getCount() - 1);
        }else{
            //4. 收藏视频为空，保存数据
            favorite.setUserId(userId);
            favorite.setVideoId(id);
            favorite.setVisible(1);
            favorite.setCount(1);
        }
        save(favorite);
        return  !BeanUtil.isEmpty(favorite) ? 0 : 1;
    }
}
