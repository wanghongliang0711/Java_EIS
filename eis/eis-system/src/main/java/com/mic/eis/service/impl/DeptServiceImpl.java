package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.dto.DeptDto;
import com.mic.eis.domain.dto.TreeSelectDto;
import com.mic.eis.domain.model.Dept;
import com.mic.eis.domain.vo.DeptQueryVo;
import com.mic.eis.mapper.DeptMapper;
import com.mic.eis.service.DeptService;
import com.mic.eis.util.StringUtils;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务接口实现类
 * @author blake.wang
 * @date 2020-07-07 17:04
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {


    @Resource
    private DeptMapper deptMapper;

    /**
     * 通过id查询部门
     * @param id
     * @return
     */
    @Override
    public Dept selectDeptById(Long id) {
        Dept dept = deptMapper.selectById(id);
        return dept;
    }

    /**
     *通过ParentId查询部门
     * @param id
     * @return
     */
    @Override
    public List<Dept> selectDeptByParentId(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("parent_id", id);
        map.put("is_delete", 0);
        return deptMapper.selectByMap(map);
    }

    @Override
    public List<Dept> selectAllDept(String deptName) {
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.like("name", deptName);
        return deptMapper.selectList(wrapper);
    }

    /**
     * 根据条件分页查询部门
     * @param deptQueryVo
     * @return
     */
    @Override
    public PageInfo<Dept> findDeptListByPage(DeptQueryVo deptQueryVo) {
        PageHelper.startPage(deptQueryVo.getPageNum(), deptQueryVo.getPageSize());
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.like("name", deptQueryVo.getDeptName());
        List<Dept> depts = deptMapper.selectList(wrapper);
        return new PageInfo<>(depts);
    }

    /**
     * 添加部门
     * @param dept
     */
    @Override
    public void addDept(Dept dept) {
        try {
            dept.setId(GeneratorCodeUtil.generateKey());
            deptMapper.insert(dept);
        }catch (Exception e){
            log.error("添加部门异常");
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * 通过id 删除 部门
     * @param id
     */
    @Override
    public void deleteDeptById(Long id) {
        try {
            deptMapper.deleteById(id);
        }catch (Exception e){
            log.error("删除部门异常");
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * 批量删除部门
     * @param deptIds
     */
    @Override
    public void batchDeleteDept(List<Long> deptIds) {
        try {
            deptMapper.deleteBatchIds(deptIds);
        }catch (Exception e){
            log.error("批量删除部门异常");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 编辑 部门
     * @param dept
     * @return
     */
    @Override
    public boolean editDept(Dept dept) {
        try {
            return deptMapper.updateById(dept) == 1;
        }catch (Exception e){
            log.error("编辑部门异常");
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public List<TreeSelectDto> buildDeptTreeSelect(List<DeptDto> depts) {
        List<DeptDto> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelectDto::new).collect(Collectors.toList());
    }

    @Override
    public List<DeptDto> buildDeptTree(List<DeptDto> depts) {
        List<DeptDto> returnList = new ArrayList<DeptDto>();
        List<Long> tempList = new ArrayList<Long>();
        for(DeptDto dept : depts){
            tempList.add(dept.getId());
        }
        for(Iterator<DeptDto> iterator = depts.iterator(); iterator.hasNext();){
            DeptDto dept = (DeptDto)iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if(!tempList.contains(dept.getParentId())){
                recursionFn(depts, dept);
                returnList.add(dept);
            }

        }
        if(returnList.isEmpty()){
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<DeptDto> list, DeptDto t){
        // 得到子节点列表
        List<DeptDto> childList = getChildList(list, t);
        t.setChildren(childList);
        for(DeptDto tChild : childList){
            if (hasChild(list, tChild)){
                // 判断是否有子节点
                Iterator<DeptDto> it = childList.iterator();
                while (it.hasNext()){
                    DeptDto n = (DeptDto) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<DeptDto> getChildList(List<DeptDto> list, DeptDto t){
        List<DeptDto> tlist = new ArrayList<DeptDto>();
        Iterator<DeptDto> it = list.iterator();
        while (it.hasNext()){
            DeptDto n = (DeptDto) it.next();
            if(StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()){
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<DeptDto> list, DeptDto t){
        return getChildList(list, t).size() > 0 ? true : false;
    }

}
