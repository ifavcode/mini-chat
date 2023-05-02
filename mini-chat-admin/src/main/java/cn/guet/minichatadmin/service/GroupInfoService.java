package cn.guet.minichatadmin.service;

import cn.guet.minichatadmin.entity.GroupInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 13080
 * @description 针对表【group_info】的数据库操作Service
 * @createDate 2023-04-17 23:17:39
 */
public interface GroupInfoService extends IService<GroupInfo> {


    List<GroupInfo> getGroupListByConsumerId(Integer consumerId);

    List<Integer> getConsumerIds(@Param("groupId") Integer groupId);
}
