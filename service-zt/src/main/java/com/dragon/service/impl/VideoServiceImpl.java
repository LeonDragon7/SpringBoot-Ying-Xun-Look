package com.dragon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragon.entity.BaseEntity;
import com.dragon.entity.Type;
import com.dragon.entity.Video;
import com.dragon.entity.VideoType;
import com.dragon.mapper.TypeMapper;
import com.dragon.mapper.VideoMapper;
import com.dragon.mapper.VideoTypeMapper;
import com.dragon.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dragon.vo.VideoHotVo;
import com.dragon.vo.VideoRecentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public List<VideoRecentVo> updateByRecent() {
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
        List<VideoRecentVo> videoVoList = list.stream().map(v -> {
            VideoRecentVo videoVo = new VideoRecentVo();
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
    @Override
    public List<VideoHotVo> hotBroadcast() {
        //1. 查询video表关联评分表数据
        List<Video> videoWithRatingList = this.baseMapper.selectVideoWithRating();
        //2. 获取视频id
        List<Integer> videoIdList = videoWithRatingList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        //3. 通过视频id查询关联表对应的类型id
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
        for (Video video : videoWithRatingList) {
            videoHotVo.setId(video.getId());
            videoHotVo.setTitle(video.getTitle());
            videoHotVo.setCoverUrl(video.getCoverUrl());
            videoHotVo.setCategoryName(video.getCategoryName());
            videoHotVo.setVideoCount(1);
            videoHotVo.setRating(video.getRating());
            videoHotVoList.add(videoHotVo);
        }
        return videoHotVoList;
    }
}
