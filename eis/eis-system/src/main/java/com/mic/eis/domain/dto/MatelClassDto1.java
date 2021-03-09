package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.MatelClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2020-10-26 11:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatelClassDto1 extends MatelClass {

    /**
     * 子种类  试试能不能一直 套下去
     */
    private List<MatelClassDto1> children;

}
