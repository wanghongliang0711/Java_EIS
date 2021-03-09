package com.mic.eis.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.dto.ProtStatusDto;
import com.mic.eis.domain.dto.ProtStatusForDashboardDto;
import com.mic.eis.domain.dto.ProtStatusInsertDto;
import com.mic.eis.domain.model.ProtStatus;
import com.mic.eis.domain.model.ProtStatusAux;

import java.util.HashMap;
import java.util.List;

public interface ProtStatusService extends IService<ProtStatus> {


    /**
     * 获取所有 protStatus 数据
     * 表头   6 种表头
     * @return HashMap<String, Object>
     */
    HashMap<String, Object> selectAllProtStatus();

    /**
     * 获取关注项目的 protStatus 数据为 Dashboard 显示
     * @return  List<ProtStatusForDashboardDto>
     */
    List<ProtStatusForDashboardDto> selectProtStatusForDashboard();


    /**
     * 根据 mainProtId 获取 protStatus 数据
     * @return ProtStatusDto
     */
    ProtStatusDto selectByMainProtId(Long mainProtId);


    /**
     * selectByParentId
     * @param parentId parentId
     * @return List<ProtStatus>
     */
    List<ProtStatus> selectByParentId(Long parentId);


    /**
     * 根据prot Status 数据id, 获取所有 prot Status Aux表 最新 数据
     * @param protStatusId Long protStatusId
     * @return ProtStatusInsertDto
     */
    ProtStatusInsertDto findByProtStatusId(Long protStatusId);


    /**
     * 根据prot Status 数据id, 获取所有 prot Status Aux表 EVT 类型 options
     * 6 个 类型 的 提示
     * @param protStatusId Long protStatusId
     * @return HashMap<String, Object>
     */
    HashMap<String, Object> getEVTOptionsByStatusId(Long protStatusId);

    /**
     * 根据prot Status 数据id, 获取所有 prot Status Aux表 EVT 类型 options
     * 6 个 类型 的 提示
     * @param protStatusId Long protStatusId
     * @return HashMap<String, Object>
     */
    HashMap<String, Object> getDVTOptionsByStatusId(Long protStatusId);

    /**
     * 根据prot Status 数据id, 获取所有 prot Status Aux表 PVT 类型 options
     * 6 个 类型 的 提示
     * @param protStatusId Long protStatusId
     * @return HashMap<String, Object>
     */
    HashMap<String, Object> getPVTOptionsByStatusId(Long protStatusId);


    /**
     * 添加 ProtStatusAux 数据， 根据 prot Status 数据id，dataType EVT DVT PVT
     * @param protStatusAux ProtStatusAux
     */
    void addProtStatusAux(ProtStatusAux protStatusAux);

    /**
     * 修改 prot Status 和 prot Status Aux
     * @param protStatusInsertDto
     */
    void editProtStatus(ProtStatusInsertDto protStatusInsertDto);

    /**
     * 根据 prot Status Aux id 查询 数据
     * @param id prot Status Aux id
     * @return ProtStatusAux
     */
    ProtStatusAux selectProtStatusAuxById(Long id);

}
