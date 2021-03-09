package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.dto.DeptDto;
import com.mic.eis.domain.dto.TreeSelectDto;
import com.mic.eis.domain.model.Dept;
import com.mic.eis.domain.vo.DeptQueryVo;

import java.util.List;


/**
 * 部门 服务 接口
 * @author blake.wang
 * @date 2020-07-07 15:52
 */
public interface DeptService extends IService<Dept> {

    /**
     * 通过id查询部门
     * @param id
     * @return
     */
    Dept selectDeptById(Long id);

    /**
     *通过ParentId查询部门
     * @param id
     * @return
     */
    List<Dept> selectDeptByParentId(Long id);

    /**
     * 查询所有部门
     * @param deptName
     * @return
     */
    List<Dept> selectAllDept(String deptName);

    /**
     * 根据条件分页查询部门
     * @param deptQueryVo
     * @return
     */
    PageInfo<Dept> findDeptListByPage(DeptQueryVo deptQueryVo);

    /**
     * 添加部门
     * @param dept
     */
    void addDept(Dept dept);

    /**
     * 通过id 删除 部门
     * @param id
     */
    void deleteDeptById(Long id);

    /**
     * 通过id 批量删除部门
     * @param deptIds
     */
    void batchDeleteDept(List<Long> deptIds);

    /**
     * 编辑 部门
     * @param dept
     * @return
     */
    boolean editDept(Dept dept);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    List<TreeSelectDto> buildDeptTreeSelect(List<DeptDto> depts);

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    List<DeptDto> buildDeptTree(List<DeptDto> depts);

}
