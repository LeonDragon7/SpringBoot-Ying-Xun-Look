package com.dragon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dragon.custom.LoginUserInfoHelper;
import com.dragon.entity.BaseEntity;
import com.dragon.entity.Favorite;
import com.dragon.mapper.FavoriteMapper;
import com.dragon.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            List<Favorite> favoriteList = list(new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId, userId));
            //4.获取收藏数据第一个记录 findFirst()
            Favorite favoriteFirst = favoriteList.stream().findFirst().orElse(null);
            //5.获取收藏数量
            int count = (favoriteFirst != null) ? favoriteFirst.getCount() : 0;
            //6. 数据不为空
            if(!favoriteList.isEmpty()){
                //7.获取收藏视频Id
                List<Integer> favoriteId = favoriteList.stream().map(BaseEntity::getId).collect(Collectors.toList());
                //8.是否存在当前收藏视频数据
                boolean hasFavorite = favoriteList.stream().anyMatch(favorite -> favorite.getVideoId().equals(id));
                //收藏视频存在
                if(hasFavorite){
                    if(favoriteList.size() != 1){
                        //更新当前的其他用户收藏记录
                        update(new UpdateWrapper<Favorite>()
                                .set("update_time", LocalDateTime.now())
                                .in("id", favoriteId)
                                .set("count", count - 1));
                    }
                    //删除当前记录信息
                    remove(new LambdaQueryWrapper<Favorite>().eq(Favorite::getVideoId, id));
                    return 0;
                }else{
                    //保存收藏数据
                    Favorite favorite = this.createFavorite(userId, id, count);
                    save(favorite);

                    //更新其他的视频数据
                    update(new UpdateWrapper<Favorite>()
                            .set("update_time", LocalDateTime.now())
                            .in("id", favoriteId)
                            .set("count", count + 1));
                    return 1;
                }
            }
            //9. 收藏数据，保存数据
            Favorite favorite = this.createFavorite(userId, id, count);
            save(favorite);
            sqlSession.commit();
            return 1;
        }
    }

    /**
     * 保存收藏数据
     * @param userId
     * @param videoId
     * @param count
     * @return
     */
    private Favorite createFavorite(Integer userId, Integer videoId, int count) {
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setVideoId(videoId);
        favorite.setVisible(1);
        favorite.setCount(count + 1);
        LocalDateTime now = LocalDateTime.now();
        favorite.setCreateTime(now);
        favorite.setUpdateTime(now);
        favorite.setCreateUser(userId);
        favorite.setUpdateUser(userId);
        return favorite;
    }
}
