package cn.guet.minichatadmin.mapper;

import cn.guet.minichatadmin.entity.GroupInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 13080
 * @description 针对表【group_info】的数据库操作Mapper
 * @createDate 2023-04-17 23:17:39
 * @Entity cn.guet.minichatadmin.entity.GroupInfo
 */
public interface GroupInfoMapper extends BaseMapper<GroupInfo> {


    @Select("SELECT gi.* FROM group_info gi left join group_consumer gc on gi.group_id = gc.group_id  where gc.consumer_id = #{consumerId}")
    List<GroupInfo> getGroupListByConsumerId(@Param("consumerId") Integer consumerId);

    @Select("SELECT gc.consumer_id FROM group_info gi left join group_consumer gc on gi.group_id = gc.group_id  where gc.group_id = #{groupId}")
    List<Integer> getConsumerIds(@Param("groupId") Integer groupId);
}




