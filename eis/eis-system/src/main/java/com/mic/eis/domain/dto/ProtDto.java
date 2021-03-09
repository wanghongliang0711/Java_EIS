package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.Prot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-05 10:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtDto extends Prot {

    /**
     * 子项目  --新的需求中，这个字段不再需要
     */
    private List<Prot> children;


    /**
     * 关注状态  0:关注   1:没有关注
     */
    private String follow;


}
