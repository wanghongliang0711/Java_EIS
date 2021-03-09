package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.model.ProtToolPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2020-10-28 13:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtToolPlanDto extends ProtToolPlan {

    /**
     * 子种类
     */
    private List<ProtToolPlanDto> children;

    /**
     * 图片 列表
     */
    private List<ProtFile> picture;

    /**
     * TEXTURE  CATEGORY  纹理类别
     */
    private List<String> textureCategory;

    /**
     * INSERT NUT SPEC.  插入螺母规格
     */
    private List<String> insertNutSpec;



}
