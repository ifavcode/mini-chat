package cn.guet.minichatadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class GroupInfo {

    @TableId(type = IdType.AUTO)
    private Integer groupId;

    private String groupName;
    private String groupDesc;
    private String pic;
    private String groupKey;
}
