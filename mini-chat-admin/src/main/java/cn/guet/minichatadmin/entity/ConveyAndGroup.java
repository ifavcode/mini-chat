package cn.guet.minichatadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConveyAndGroup {

    private GroupInfo groupInfo;
    private ConveyContainer conveyContainer;
}
