package com.mic.eis.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.dto.ProtToolPlanDto;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.model.ProtMePartList;
import com.mic.eis.domain.model.ProtToolPlan;
import com.mic.eis.domain.vo.ProtMePartListQueryVo;

import java.util.HashMap;
import java.util.List;

public interface ProtToolPlanService extends IService<ProtToolPlan> {


    /**
     * 通过 子项目id 和 文件版本 查询  Tool Plan
     * @param protMePartListQueryVo  Long protId;  String fileVer;
     * @return  List<ProtToolPlanDto>
     */
    List<ProtToolPlanDto> selectByProtIdAndVersion(ProtMePartListQueryVo protMePartListQueryVo);


    /**
     * 根据 子项目id 、版本信息， 将 tool plan 恢复到之前的版本
     * @param protMePartListQueryVo 实体
     */
    void backBeforeVersion(ProtMePartListQueryVo protMePartListQueryVo);


    /**
     * 根据 子项目id 和 文件版本now 和 PR Number 更新now 版本 的所有的PR Number
     * @param protToolPlan 实体
     */
    void updatePrNum(ProtToolPlan protToolPlan);

    /**
     * 添加新版本 --  Prot Tool Plan 生成新版本数据, ProtFileVer 添加一条数据信息
     * @param protFileVer 实体
     * @return 提示 信息
     */
    HashMap<String, Object> addNewVersion(ProtFileVer protFileVer);


    /**
     * 从Me Part List中找出数据 显示 以供添加
     * @param sonProtId  子项目 id
     * @return 没有在 Tooling plan 中的MePartList 数据
     */
    List<ProtMePartList> selectByMePartList(Long sonProtId);


    /**
     * 添加从Me Part List中找出的数据
     * @param sonProtId 子项目 id
     * @param mePartListPartNums [" "," "....]
     */
    void batchAddByMePartList(Long sonProtId, List<String> mePartListPartNums);


    /**
     * 新增 tool plan 子项
     * @param protToolPlanDto ProtToolPlanDto
     */
    HashMap<String, Object> addToolPlanSon(ProtToolPlanDto protToolPlanDto);

    /**
     * 修改 tool plan
     * @param protToolPlanDto ProtToolPlanDto
     * @return {msg: }
     */
    HashMap<String, Object>  editProtToolPlan(ProtToolPlanDto protToolPlanDto);

    /**
     * 获取所有 VERSION 用于提示
     * @return [{value: VERSION }, ]
     */
    List<HashMap<String, String>> selectAllVersion();


    /**
     * 通过 id 删除 -->  真实删除
     * @param id id  parentId parentId
     */
    void deleteProtToolPlanById(Long id, Long parentId);

    /**
     * 获取 最新的 更新时间  更新人
     * @param sonProtId 子项目id
     * @return {time: 更新时间 ，user: 更新人}
     */
    HashMap<String, String> selectLastTimeUser(Long sonProtId);


    /**
     * 获取某个版本 的 最新 更新时间  更新人
     * @param sonProtId 子项目id
     * @param fileVer 版本
     * @return {time: 更新时间 ，user: 更新人}
     */
    HashMap<String, String> selectLastTimeUserByVer(Long sonProtId, String fileVer);

    /**
     * selectByParentId
     * @param parentId parentId
     * @return List<ProtToolPlan>
     */
    List<ProtToolPlan> selectByParentId(Long parentId);

    /**
     * 获取所有 MATERIAL 第三级 用于提示
     * @return [{value: VERSION }, ]
     */
    List<HashMap<String, String>> selectAllMaterial();


    /**
     * 获取所有 COLOR No 用于提示
     * @return [{value: VERSION }, ]
     */
    List<HashMap<String, String>> selectAllColorNo();


    /**
     * 获取所有 PAINTING COLOR No. 用于提示
     * @return [{value: VERSION }, ]
     */
    List<HashMap<String, String>> selectAllPaintingColorNo();

    /**
     * 获取所有 PRINTING COLOR No. 用于提示
     * @return [{value: VERSION }, ]
     */
    List<HashMap<String, String>> selectAllPrintingColorNo();


    /**
     * 获取所有 COATING  CATEGORY 用于提示
     * @return [{value: VERSION }, ]
     */
    List<HashMap<String, String>> selectAllCoatingCategory();


    /**
     * 获取所有 TEXTURE  CATEGORY 用于提示
     * @return [{value: , label:  }, ]
     */
    List<HashMap<String, String>> selectAllTextureCategory();


    /**
     * 获取所有 INSERT NUT SPEC. 用于提示
     * @return [{value: , label:  }, ]
     */
    List<HashMap<String, String>> selectAllInsertNutSpec();


    /**
     * 获取所有 TOOLING  VENDER 用于提示
     * @return [{value:  }, ]
     */
    List<HashMap<String, String>> selectAllToolingVender();


    /**
     * 获取所有 PR NUMBER. 用于提示
     * @return [{value:  }, ]
     */
    List<HashMap<String, String>> selectAllPrNumber();


    /**
     * 获取所有 SUPPLIED VENDOR 用于提示
     * @return [{value:  }, ]
     */
    List<HashMap<String, String>> selectAllSuppliedVendor();


}
