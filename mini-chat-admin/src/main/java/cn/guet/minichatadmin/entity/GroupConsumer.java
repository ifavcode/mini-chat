package cn.guet.minichatadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class GroupConsumer {

    private Integer groupId;
    private Integer consumerId;
}
