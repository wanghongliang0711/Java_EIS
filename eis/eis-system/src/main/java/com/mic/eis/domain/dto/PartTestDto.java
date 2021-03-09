package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.PartTest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author calisto
 * @date 2020-08-07 1:37 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PartTestDto extends PartTest {

    /**
     * 父
     */
    private Long rootProtId;

}
