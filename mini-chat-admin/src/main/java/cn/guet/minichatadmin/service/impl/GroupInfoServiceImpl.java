package cn.guet.minichatadmin.service.impl;

import cn.guet.minichatadmin.entity.GroupInfo;
import cn.guet.minichatadmin.mapper.GroupInfoMapper;
import cn.guet.minichatadmin.service.GroupInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 13080
 * @description 针对表【group_info】的数据库操作Service实现
 * @createDate 2023-04-17 23:17:39
 */
@Service
public class GroupInfoServiceImpl extends ServiceImpl<GroupInfoMapper, GroupInfo>
        implements GroupInfoService {

    @Resource
    private GroupInfoMapper groupInfoMapper;

    @Override
    public List<GroupInfo> getGroupListByConsumerId(Integer consumerId) {
        return groupInfoMapper.getGroupListByConsumerId(consumerId);
    }

    @Override
    public List<Integer> getConsumerIds(Integer groupId) {
        return groupInfoMapper.getConsumerIds(groupId);
    }
}




