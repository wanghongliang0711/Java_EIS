package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.MatelClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-19 13:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatelClassDto extends MatelClass {

    /**
     * 子种类
     */
    private List<MatelClass> children;

}
