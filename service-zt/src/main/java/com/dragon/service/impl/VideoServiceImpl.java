package com.dragon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragon.config.VideoRecommender;
import com.dragon.constant.MessageConstant;
import com.dragon.constant.RecommendConstant;
import com.dragon.constant.RedisConstant;
import com.dragon.custom.LoginUserInfoHelper;
import com.dragon.dto.CommonQueryDTO;
import com.dragon.dto.VideoPageQueryDTO;
import com.dragon.entity.*;
import com.dragon.exception.RuntimeExceptionCustom;
import com.dragon.mapper.*;
import com.dragon.result.PageResult;
import com.dragon.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dragon.utils.RecommendUtil;
import com.dragon.vo.VideoDetailVo;
import com.dragon.vo.VideoHotVo;
import com.dragon.vo.VideoReRmVo;
import com.dragon.vo.VideoVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
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
@Slf4j
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

    @Autowired
    private UserVideoMapper userVideoMapper;

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
        List<VideoHotVo> videoHotVoList = new ArrayList<>();//存储热播数据
        //1. 查询video表关联评分表数据，根据评分降序
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
        for (int i = 0; i < videoWithRatingList.size(); i++) {
            VideoVo videoVo = videoWithRatingList.get(i);//获取视频返回对象
            List<Integer> videoTypeIds = allVideoTypeIds.get(i);//获取视频对于的类型

            LambdaQueryWrapper<Type> typeWrapper = new LambdaQueryWrapper<>();
            typeWrapper.eq(Type::getStatus, 1) // 类型状态为1
                    .in(Type::getId, videoTypeIds) //构建in子句
                    .select(Type::getTypeName); // 指定要查询的字段

            List<String> typeNameList = typeMapper.selectList(typeWrapper)
                    .stream()
                    .map(Type::getTypeName)
                    .collect(Collectors.toList());

            //5. 封装vo对象条件
            VideoHotVo videoHotVo = new VideoHotVo();
            videoHotVo.setId(videoVo.getId());
            videoHotVo.setTitle(videoVo.getTitle());
            videoHotVo.setCoverUrl(videoVo.getCoverUrl());
            videoHotVo.setCategoryName(videoVo.getCategoryName());
            videoHotVo.setPlayCount(9999);
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
    public List<VideoReRmVo> recommendMovie(Integer userId) {
        //用户未登录
        if(userId == null){
            // 返回电影数据 评分从高到低
            return this.baseMapper.getHotRatingByCategoryName(RecommendConstant.RECOMMEND_MOVIE);
        }
        return this.recommend(RecommendConstant.RECOMMEND_MOVIE,userId);
    }

    /**
     * 动漫推荐
     * @return
     */
    @Override
    public List<VideoReRmVo> recommendAnime(Integer userId) {
        //用户未登录
        if(userId == null){
            // 返回动漫数据 评分从高到低
            return this.baseMapper.getHotRatingByCategoryName(RecommendConstant.RECOMMEND_ANIME);
        }
      return this.recommend(RecommendConstant.RECOMMEND_ANIME,userId);
    }

    /**
     * 推荐功能
     * @return
     */
    private List<VideoReRmVo> recommend(String categoryName,Integer userId) {
        List<Video> recommendVideoList = new ArrayList<>();
        String recommend = "";
        //1.用户已登录
        User user = userMapper.selectById(userId);
        if(user != null){
            //1.1 load缓存数据
            recommend = redisApi.getString(RecommendUtil.getKey(RedisConstant.RECOMMEND,user.getSoleTag()));
            //1.2 缓存数据是空
            if(StrUtil.isBlank(recommend)){
                //用户打过分且分类名称是电影或者动漫
                List<VideoRate> videoRateList = videoRateMapper.findAllByUserId(user.getId(), categoryName);
                if(!videoRateList.isEmpty()){
                    //基于用户推荐
                    try {
                        List<Integer> animeIdList = videoRecommender.userBasedRecommender(user.getId(), RecommendConstant.RECOMMEND_SIZE);
                        if(animeIdList.isEmpty()) recommendVideoList.addAll(new ArrayList<>());
                        else recommendVideoList.addAll(this.listByIds(animeIdList));
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

    /**
     * 高分影视
     * @return
     */
    @Override
    public Map<String,Object> getHotRating() {
        //1. 根据key获取高分数据
        String hotRatingValue = redisApi.getString(RedisConstant.HOT_RATING);
        //2. 判断是否存在高分数据
        Map<String, Object> map = new HashMap<>(2, 1);
        if(StrUtil.isBlank(hotRatingValue)){
            //3. 不存在
            //3.1 获取高分影视
            List<VideoReRmVo> hotRatingList = this.baseMapper.getHotRating();
            //3.2 保存到redis
            redisApi.setValue(RedisConstant.HOT_RATING,JSONObject.toJSONString(hotRatingList),RedisConstant.HOT_RATING_TIME,TimeUnit.DAYS);
            map.put("hotRatingList",hotRatingList);
        }else{
            //4. 存在
            //4.1 将数据类型转换成实体类
            List<VideoReRmVo> videoReRmVoValue = JSONObject.parseArray(hotRatingValue, VideoReRmVo.class);
            map.put("hotRatingList",videoReRmVoValue);
        }
        return map;
    }

    /**
     * 猜你喜欢
     * @return
     */
    @Override
    public List<VideoReRmVo> getLike(Integer userId) {
        //1. 用户未登录
        if(userId == null) return this.baseMapper.getHotRating();
        //2. 根据当前用户id获取用户视频关联信息
        LambdaQueryWrapper<UserVideo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserVideo::getCollect,1)// 收藏 1
                .eq(UserVideo::getUserId,userId) // 当前用户ID
                .orderByDesc(UserVideo::getPlayCount)// 播放次数倒序
                .orderByDesc(UserVideo::getPlayTime)// 播放最近时间倒序
                .last("limit 5");
        List<UserVideo> userVideoList = userVideoMapper.selectList(wrapper);

        //3. 根据用户视频关联表获取视频id集合
        List<Integer> videoIds = userVideoList.stream().map(UserVideo::getVideoId).collect(Collectors.toList());

        //4. 根据视频id集合获取视频数据
        LambdaQueryWrapper<Video> videoWrapper = new LambdaQueryWrapper<>();
        videoWrapper.in(BaseEntity::getId,videoIds);
        List<Video> videoList = this.list(videoWrapper);
        //5. 判断是否存在五条数据
        int size = videoList.size();
        if(size < 5 && !videoList.isEmpty()){
            //随机返回视频
            List<Video> allByCountLimit = this.baseMapper.findAllByCountLimit(5 - size);
            videoList.addAll(allByCountLimit);
        }else if(videoList.isEmpty()){
            //返回高分视频
            return this.baseMapper.getHotRating();
        }
        //6. 封装数据
        List<VideoReRmVo> videoReRmVoList = videoList.stream().map(v -> {
            VideoReRmVo videoReRmVo = new VideoReRmVo();
            BeanUtil.copyProperties(v, videoReRmVo);
            return videoReRmVo;
        }).collect(Collectors.toList());
        //7. 返回数据
        return videoReRmVoList;
    }

    /**
     * 视频条件分页查询
     * @param videoPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageList(VideoPageQueryDTO videoPageQueryDTO) {
        Map<String, Object> map = new HashMap<>();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        if(videoPageQueryDTO.getPageTag() == 1) {
            //1. 本周热播
            //1.1 计算本周的星期一的零点
            LocalDateTime beginTime = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .with(LocalTime.MIDNIGHT);
            //1.2 计算当前时间本周的星期天的23:59:59
            LocalDateTime endTime = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                    .with(LocalTime.MAX)
                    .minusNanos(1); // 减去一个纳秒，将时间调整为23:59:59
            //1.3 封装数据
            map.put("begin", beginTime);
            map.put("end", endTime);
        }else{
            //2. 历史热播
            //2.1 获取1949新中国成立时间
            LocalDateTime chineseStart = LocalDate.of(1949, 1, 1).atStartOfDay();
            //2.2 封装数据
            map.put("begin", chineseStart);
            map.put("end", now);
        }
        map.put("vp", videoPageQueryDTO);
        //3. 返回数据
        return page(map);
    }

    /**
     * 分页查询
     * @param map
     * @return
     */
    private PageResult page(Map<String,Object> map){
        //select * from video join ... limit 0,pageSize;
        //分页查询
        VideoPageQueryDTO vp = (VideoPageQueryDTO) map.get("vp");
        Integer pageTag = vp.getPageTag();
        PageHelper.startPage(vp.getPage(), vp.getPageSize());
        Page<VideoReRmVo> pageResult = null;
        if(pageTag == 1 || pageTag == 2){
            //本周热播/历史热播
            pageResult = this.baseMapper.pageWeekList(map);
        }else if(pageTag == 3){
            //最新上线
            pageResult = this.baseMapper.pageNewList(map);
        }else if(pageTag == 4){
            //最受欢迎
            pageResult = this.baseMapper.pageRatingList(map);
        }
        return new PageResult(pageResult.getTotal(),pageResult.getResult());
    }

    /**
     * 历史观看
     * @param commonQueryDTO
     * @return
     */
    @Override
    public PageResult historyView(CommonQueryDTO commonQueryDTO) {
        //1. 获取当前用户id
        Integer userId = LoginUserInfoHelper.getUser().getId();

        //2. 分页查询
        PageHelper.startPage(commonQueryDTO.getPage(), commonQueryDTO.getPageSize());

        //3. 封装信息
        Map<String, Object> map = new HashMap<>();
        map.put("id",userId);
        map.put("hp", commonQueryDTO);
        Page<VideoReRmVo> pageResult = this.baseMapper.historyView(map);

        //4.返回数据
        return new PageResult(pageResult.getTotal(),pageResult.getResult());
    }

    /**
     * 会员专区
     * @param commonQueryDTO
     * @return
     */
    @Override
    public PageResult prefecture(CommonQueryDTO commonQueryDTO) {
        //1. 获取当前用户id
        Integer userId = LoginUserInfoHelper.getUser().getId();
        //2. 根据当前用户id查询是否为会员用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getId,userId)
                .eq(User::getStatus,0);
        User user = userMapper.selectOne(wrapper);
        //3. 非会员用户
        if(user.getVip() == 0) throw new RuntimeExceptionCustom(MessageConstant.NO_VIP_USER);
        //4. 分页查询视频数据
        PageHelper.startPage(commonQueryDTO.getPage(),commonQueryDTO.getPageSize());
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getStatus,1)
                .eq(Video::getPrice,2) // 会员视频
                .eq(Video::getIsDeleted,1);
        List<Video> videos = this.baseMapper.selectList(queryWrapper);
        //5. 类似转换
        List<VideoReRmVo> videoReRmVoList = this.baseMapper.selectList(queryWrapper).stream().map(v -> {
            VideoReRmVo videoReRmVo = new VideoReRmVo();
            BeanUtil.copyProperties(v, videoReRmVo);
            return videoReRmVo;
        }).collect(Collectors.toList());
        //6. 获取分页信息
        PageInfo<VideoReRmVo> pageResult = new PageInfo<>(videoReRmVoList);
        //7.返回数据
        return new PageResult(pageResult.getTotal(),videoReRmVoList);
    }
}
