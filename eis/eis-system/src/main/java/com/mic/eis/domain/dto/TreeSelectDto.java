package com.mic.eis.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mic.eis.domain.model.Dept;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 * @author blake.wang
 * @date 2020-07-20 12:37
 */
public class TreeSelectDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectDto> children;

    public TreeSelectDto(){

    }

    public TreeSelectDto(DeptDto deptDto){
        this.id = deptDto.getId();
        this.label = deptDto.getName();
        this.children = deptDto.getChildren().stream().map(TreeSelectDto::new).collect(Collectors.toList());
    }

    public TreeSelectDto(Dept dept) {
    }


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<TreeSelectDto> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeSelectDto> children)
    {
        this.children = children;
    }

}
