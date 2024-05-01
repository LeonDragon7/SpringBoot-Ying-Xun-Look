package com.dragon.service.impl;

import com.dragon.entity.UserVideo;
import com.dragon.mapper.UserVideoMapper;
import com.dragon.service.UserVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户视频关联表 服务实现类
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Service
public class UserVideoServiceImpl extends ServiceImpl<UserVideoMapper, UserVideo> implements UserVideoService {

}
