package com.dragon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dragon.custom.LoginUserInfoHelper;
import com.dragon.entity.Favorite;
import com.dragon.mapper.FavoriteMapper;
import com.dragon.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    /**
     * 点击收藏
     * @param id 视频id
     * @return
     */
    @Override
    public Integer collect(Integer id) {
        //1. 获取当前用户id
        Integer userId = LoginUserInfoHelper.getUser().getId();
        //2. 使用事务批量操作
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)){
            //3. 根据用户id获取收藏数据
            LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Favorite::getUserId,userId);
            List<Favorite> favoriteList = list(wrapper);

            //4.获取收藏视频Id
            List<Integer> favoriteId = favoriteList.stream().map(f -> f.getId()).collect(Collectors.toList());
            Favorite favorite = null;
            UpdateWrapper<Favorite> updateWrapper = new UpdateWrapper<>();
            //5. 数据不为空
            if(!favoriteList.isEmpty()){
                wrapper.clear();
                wrapper.eq(Favorite::getIsDelete,0)
                        .eq(Favorite::getVideoId,id);
                favorite = getOne(wrapper);
                favorite.setCount(1);//收藏视频数据为空
                //3. 收藏视频不为空
                if(!BeanUtil.isEmpty(favorite)){
                    //更新数据
                    updateWrapper = new UpdateWrapper<>();
                    updateWrapper.in("id",favoriteId).set("visible",0).setSql("count = count - 1").set("is_delete",1);
                    update(updateWrapper);
                    return 0;
                }
            }
            //6. 收藏数据或者收藏视频为空，保存数据
            //获取收藏数据第一个记录 findFirst()
            Favorite favoriteFirst = favoriteList.stream().findFirst().orElse(null);
            //获取收藏数量
            int count = (favoriteFirst != null) ? favoriteFirst.getCount() : 0;
            favorite.setUserId(userId);
            favorite.setVideoId(id);
            favorite.setVisible(1);
            favorite.setCount(count + 1);
            save(favorite);
            //更新收藏数据的收藏数量
            updateWrapper.clear();
            updateWrapper.in("id",favoriteId).setSql("count = count + 1");
            update(updateWrapper);
            sqlSession.commit();
            return 1;
        }
    }
}
