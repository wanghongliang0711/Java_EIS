package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.dto.ProtDto;
import com.mic.eis.domain.model.Prot;
import com.mic.eis.domain.vo.ProtQueryVo;

import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-07-09 9:34
 */
public interface ProtService extends IService<Prot> {

    /**
     * 通过 id 查询项目
     * @param id
     * @return
     */
    Prot selectProtByID(Long id);

    /**
     * 过ParentId查询项目
     * @param id
     * @return
     */
    List<Prot> selectProtByParentId(Long id);

    /**
     * 通过Name查询项目
     * @param name
     * @return
     */
    List<Prot> selectProtByName(String name);

    /**
     * 通过Name查询项目
     * @param code
     * @return
     */
    List<Prot> selectProtByCode(String code);

    /**
     * 通过 项目名 和 status 分页查询 项目
     * @param protQueryVo
     * @return
     */
    PageInfo<ProtDto> findMainProtByPage(ProtQueryVo protQueryVo);

    /**
     * 查询所有 项目 用于 提示
     * @return [{value: 项目id，  label：项目名 }, ]
     */
    List<HashMap<String, Object>> selectAllProt();

    /**
     * 查询这个项目 对应的 所有子项目 用于 提示
     * @param protId  项目 id
     * @return [{value: 子项目id，  label：子项目名 }, ]
     */
    List<HashMap<String, Object>> selectAllSonProt(Long protId);

    /**
     * 通过 项目名 和 status 不分页查询 项目
     * @param protQueryVo
     * @return
     */
    List<ProtDto> queryByNameAndStatus(ProtQueryVo protQueryVo);

    /**
     * 添加项目
     * @param prot
     */
    void addProt(Prot prot);

    /**
     * 通过 id 删除项目
     * @param id 项目id
     */
    void deleteProtById(Long id);

    /**
     * 通过子项目 id 删除， 因为子项目 与 userProt userFollowProt 无关，所以单独写
     * @param id  项目id
     */
    void deleteSubProtById(Long id);

    /**
     *  批量删除项目
     * @param deptIds
     */
    void batchDeleteProt(List<Long> deptIds);

    /**
     * 编辑 项目
     * @param prot
     */
    boolean editProt(Prot prot);

    /**
     * 用于 编辑 项目 时 主项目 判断是否重复使用
     * 排除 当前的  name  =name  != id
     */
    List<Prot> selectByNameExcludeProt(Prot prot);

    /**
     * 用于 编辑 项目 时 主项目 判断是否重复使用
     * 排除 当前的  code  =code  != id
     */
    List<Prot> selectByCodeExcludeProt(Prot prot);


    /**
     * 通过 userId 查询 用户关注的 项目 的详细信息
     * 返回项目 信息 list
     */
    List<ProtDto> selectByUserIdReturnProt();
}
