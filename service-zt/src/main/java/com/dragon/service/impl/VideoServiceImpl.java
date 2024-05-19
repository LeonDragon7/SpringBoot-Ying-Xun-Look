package com.dragon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragon.config.VideoRecommender;
import com.dragon.constant.RecommendConstant;
import com.dragon.constant.RedisConstant;
import com.dragon.custom.LoginUserInfoHelper;
import com.dragon.entity.*;
import com.dragon.mapper.*;
import com.dragon.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dragon.utils.RecommendUtil;
import com.dragon.vo.VideoDetailVo;
import com.dragon.vo.VideoHotVo;
import com.dragon.vo.VideoReRmVo;
import com.dragon.vo.VideoVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频表 服务实现类
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VideoTypeMapper videoTypeMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private RedisApi redisApi;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VideoRateMapper videoRateMapper;

    @Autowired
    private VideoRecommender videoRecommender;

    /**
     * 最近更新
     * 条件：
     * - 收费情况：1
     * - 状态：1
     * - 是否被删除(逻辑删除)：1
     * - 根据最近年代获取
     * - 获取前五条数据
     * @return
     */
    @Override
    public List<VideoReRmVo> updateByRecent() {
        //1. 封装查询条件
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getPrice,1)
                .eq(Video::getStatus,1)
                .eq(Video::getIsDeleted,1)
                .orderByDesc(Video::getYear)
                .last("limit 5");
        //2. 查询video表数据
        List<Video> list = list(wrapper);
//        ArrayList<VideoVo> videoVoList = new ArrayList<>();
//        VideoVo videoVo = new VideoVo();
//        for (Video video : list) {
//            BeanUtil.copyProperties(video,videoVo);
//            videoVoList.add(videoVo);
//        }
//        return videoVoList;
        //3. 将数据拷贝到videoVo对象
        List<VideoReRmVo> videoVoList = list.stream().map(v -> {
            VideoReRmVo videoVo = new VideoReRmVo();
            BeanUtil.copyProperties(v, videoVo);
            return videoVo;
        }).collect(Collectors.toList());
        //返回数据
        return videoVoList;
    }

    /**
     * 正在热播
     * @return
     */
    @Transactional
    @Override
    public List<VideoHotVo> hotBroadcast() {
        //1. 查询video表关联评分表数据
        List<VideoVo> videoWithRatingList = this.baseMapper.selectVideoWithRating();
        //2. 获取视频id
        List<Integer> videoIdList = videoWithRatingList.stream().map(VideoVo::getId).collect(Collectors.toList());
        //3. 通过视频id集合查询关联表对应的类型id
        List<List<Integer>> allVideoTypeIds = new ArrayList<>();

        for (Integer videoId : videoIdList) {
            LambdaQueryWrapper<VideoType> videoTypeWrapper = new LambdaQueryWrapper<>();
            videoTypeWrapper.eq(VideoType::getVideoId,videoId);

            List<Integer> videoTypeIds = videoTypeMapper.selectList(videoTypeWrapper).stream()
                    .map(VideoType::getTypeId)
                    .collect(Collectors.toList());

            allVideoTypeIds.add(videoTypeIds);
        }

        //4. 根据对应类型id集合查询类型名称
        LambdaQueryWrapper<Type> typeWrapper = new LambdaQueryWrapper<>();
        typeWrapper.eq(Type::getStatus,1) // 类型状态为1
                .select(Type::getTypeName); // 指定要查询的字段

        for (List<Integer> videoTypeId : allVideoTypeIds) {
            typeWrapper.in(Type::getId,videoTypeId); //构建in子句
        }
        List<String> typeNameList = typeMapper.selectList(typeWrapper)
                .stream()
                .map(Type::getTypeName)
                .collect(Collectors.toList());

        //5. 封装vo对象条件
        List<VideoHotVo> videoHotVoList = new ArrayList<>();
        VideoHotVo videoHotVo = new VideoHotVo();
        for (VideoVo videoVo : videoWithRatingList) {
            videoHotVo.setId(videoVo.getId());
            videoHotVo.setTitle(videoVo.getTitle());
            videoHotVo.setCoverUrl(videoVo.getCoverUrl());
            videoHotVo.setCategoryName(videoVo.getCategoryName());
            videoHotVo.setVideoCount(1);
            videoHotVo.setRating(videoVo.getRating());
            videoHotVo.setTypeList(typeNameList);
            videoHotVoList.add(videoHotVo);
        }
        return videoHotVoList;
    }

    /**
     * 电影推荐
     * @return
     */
    @Override
    public List<VideoReRmVo> recommendMovie() {
        return this.recommend(RecommendConstant.RECOMMEND_MOVIE);
    }

    /**
     * 动漫推荐
     * @return
     */
    @Override
    public List<VideoReRmVo> recommendAnime() {
      return this.recommend(RecommendConstant.RECOMMEND_ANIME);
    }

    /**
     * 推荐功能
     * @return
     */
    private List<VideoReRmVo> recommend(String categoryName) {
        List<Video> recommendVideoList = new ArrayList<>();
        int userId = LoginUserInfoHelper.getUserId().intValue();
        String recommend = "";
        //1.用户已登录
        User user = userMapper.selectById(userId);
        if(user != null){
            //1.1 load缓存数据
            recommend = redisApi.getString(RecommendUtil.getKey(RedisConstant.RECOMMEND,user.getSoleTag()));
            //1.2 缓存数据是空
            if(StrUtil.isBlank(recommend)){
                //用户打过分且分类名称是电影或者动漫
                List<VideoRate> videoRateList = videoRateMapper.findAllByUserId(userId, categoryName);
                if(!videoRateList.isEmpty()){
                    //基于用户推荐
                    try {
                        List<Integer> animeIdList = videoRecommender.itemBasedRecommender(userId, RecommendConstant.RECOMMEND_SIZE);
                        recommendVideoList.addAll(this.listByIds(animeIdList));
                    } catch (TasteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else {
                // 从缓存中直接加载
                recommendVideoList.addAll(JSONObject.parseArray(recommend,Video.class));
            }
        }else{
            //2.随机返回数据
            recommendVideoList.addAll(this.baseMapper.findAllByCountLimit(RecommendConstant.RECOMMEND_SIZE));
        }
        //3.上述异常，直接通过用户标签查询数据库并推荐
        if(recommendVideoList.isEmpty() && user != null){
            Map<String, Object> map = new HashMap<>();
            map.put("soleTag",user.getSoleTag());
            map.put("size",RecommendConstant.RECOMMEND_SIZE);
            map.put("categoryName",categoryName);
            recommendVideoList.addAll(this.baseMapper.getVideoByUserTag(map));
        }
        if(StringUtils.isEmpty(recommend)){
            assert user != null;
            redisApi.setValue(RecommendUtil.getKey(RedisConstant.RECOMMEND,user.getSoleTag()),JSONObject.toJSONString(recommendVideoList),1, TimeUnit.DAYS);
        }

        //4.返回数据
        List<VideoReRmVo> videoReRmVoList = recommendVideoList.stream().map(video -> {
            VideoReRmVo videoReRmVo = new VideoReRmVo();
            BeanUtil.copyProperties(video, videoReRmVo);
            return videoReRmVo;
        }).collect(Collectors.toList());
        return videoReRmVoList;
    }

    /**
     * 获取视频详细信息
     * @param id
     * @return
     */
    @Override
    public VideoDetailVo getVideoDetail(Integer id) {
        LambdaQueryWrapper<VideoType> wrapper = new LambdaQueryWrapper<>();

        //1. 根据id查询video表关联评分表详细信息
        List<VideoVo> videoWithRatingList  = this.baseMapper.getVideoDetail(id);

        //2. 根据视频id获取视频类型id
        wrapper.eq(VideoType::getVideoId,id);
        List<VideoType> videoTypeList = videoTypeMapper.selectList(wrapper);
        List<Integer> typeIdList = videoTypeList.stream().map(type -> type.getTypeId()).collect(Collectors.toList());

        //3. 根据视频类型id获取类型信息
        LambdaQueryWrapper<Type> typeWrapper = new LambdaQueryWrapper<>();
        typeWrapper.eq(Type::getStatus,1) // 类型状态为1
                .select(Type::getTypeName); // 指定要查询的字段

        typeWrapper.in(Type::getId,typeIdList); //构建in子句

        //4. 获取类型名称
        List<String> typeNameList = typeMapper.selectList(typeWrapper)
                .stream()
                .map(Type::getTypeName)
                .collect(Collectors.toList());

        //5. 封装视频详情和类型名称
        VideoDetailVo videoDetailVo = new VideoDetailVo();
        videoDetailVo.setVideoWithRating(videoWithRatingList);
        videoDetailVo.setVideoTypeList(typeNameList);
        return videoDetailVo;
    }
}
