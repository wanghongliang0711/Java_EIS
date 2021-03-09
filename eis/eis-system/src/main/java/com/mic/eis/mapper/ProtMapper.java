package com.mic.eis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.Prot;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 项目 Mapper 接口
 * @author blake.wang
 * @date 2020-07-08
 */
@Mapper
public interface ProtMapper extends BaseMapper<Prot> {

    /**
     * 通过用户id查询负责的项目
     * @author calisto
     * @date 2020-08-07 09:38:55
     * @param
     * @param userId
     **/
    List<Long> selectProtByUserId(@Param("userId") Long userId, @Param("member") Integer member);

}
