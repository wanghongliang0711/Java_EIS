package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.Prot;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.vo.ProtFileVerQueryVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DataPermissionService;
import com.mic.eis.service.ProtFileVerService;
import com.mic.eis.service.ProtService;
import com.mic.eis.util.HttpContextUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author blake.wang
 * @date 2020-09-09 16:30
 */
@Slf4j
@RestController
@RequestMapping("/protFileVer")
public class ProtFileVerController {

    @Resource
    private ProtFileVerService protFileVerService;

    @Resource
    private DataPermissionService dataPermissionService;

    @Resource
    private ProtService protService;

    // 不分页查询 --ok
    @OptionLog("通过 子项目id 和 文件类别 和 文件版本 查询 ProtFileVer")
    @ApiOperation("通过 子项目id 和 文件类别 和 文件版本 查询 ProtFileVer")
    @PostMapping("/find")
    public ResponseEntity<?> selectByProtIdAndCategoryAndVer(@Valid @RequestBody ProtFileVerQueryVo protFileVerQueryVo){
        return ResponseHelper.success(protFileVerService.selectByProtIdAndCategoryAndVer(protFileVerQueryVo));
    }

    // 仅仅 now 版本修改 Approval  时 使用--ok
    @OptionLog("添加  ProtFileVer")
    @ApiOperation("添加  ProtFileVer")
    @PostMapping("/add")
    public ResponseEntity<?> addProtFileVer(@Valid @RequestBody ProtFileVer protFileVer){
        Prot prot = protService.selectProtByID(protFileVer.getProtId());
        if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            protFileVerService.addProtFileVer(protFileVer);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

}
