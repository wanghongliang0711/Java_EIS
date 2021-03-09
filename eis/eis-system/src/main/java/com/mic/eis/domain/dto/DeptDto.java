package com.mic.eis.domain.dto;

import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-07-20 13:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeptDto  extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *部门名
     */
    @NotNull(message = "部门名不能为空")
    private String name;

    /**
     *父部门id
     */
    private Long parentId;

    /** 子部门 */
    private List<DeptDto> children = new ArrayList<DeptDto>();

    public List<DeptDto> getChildren() {
        return children;
    }

    public void setChildren(List<DeptDto> children) {
        this.children = children;
    }

}

