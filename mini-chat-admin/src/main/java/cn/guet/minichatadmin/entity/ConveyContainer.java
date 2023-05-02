package cn.guet.minichatadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConveyContainer {

    private Integer conveyUserId;
    private String message;
    private Integer userId;
    private Integer conveyGroupId;
}
