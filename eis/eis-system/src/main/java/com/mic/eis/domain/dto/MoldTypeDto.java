package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.MoldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-13 16:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoldTypeDto extends MoldType {

    /**
     * 子种类
     */
    private List<MoldType> children;

}
