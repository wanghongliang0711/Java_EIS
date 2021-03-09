package com.mic.eis.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.dto.PartTestDto;
import com.mic.eis.domain.model.PartTest;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DataPermissionService;
import com.mic.eis.service.PartTestService;
import com.mic.eis.util.HttpContextUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 测试PartList
 * @author calisto
 * @since 2020-08-07
 */
@RestController
@RequestMapping("/part-test")
public class PartTestController {

    @Resource
    private PartTestService partTestService;

    @Resource
    private DataPermissionService dataPermissionService;

    @ApiOperation("查询所有partlist")
    @PostMapping("/find/{sonProtId}")
    public ResponseEntity<?> findPartList(@PathVariable String sonProtId){
        QueryWrapper<PartTest> wrapper = new QueryWrapper<PartTest>();
        wrapper.lambda()
                .eq(PartTest::getProtId, sonProtId)
                .orderByDesc(PartTest::getLine);
        return ResponseHelper.success(partTestService.list(wrapper));
    }

    @OptionLog("编辑partTest")
    @ApiOperation("编辑partTest")
    @PostMapping("/edit")
    public ResponseEntity<?> editPartRecord(@RequestBody PartTestDto partTestDto){
        Long currUserId = HttpContextUtil.getCurrentUser().getId();
        if (!dataPermissionService.isMember(currUserId, partTestDto.getRootProtId()) &&
                !dataPermissionService.isOwner(currUserId, partTestDto.getRootProtId())) {
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        PartTest partTest = new PartTestDto();
        BeanUtils.copyProperties(partTestDto, partTest);
        return ResponseHelper.success(partTestService.updateById(partTest));
    }


}
