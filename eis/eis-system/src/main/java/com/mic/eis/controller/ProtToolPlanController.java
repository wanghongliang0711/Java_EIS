package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.dto.ProtToolPlanDto;
import com.mic.eis.domain.model.Prot;
import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.model.ProtToolPlan;
import com.mic.eis.domain.vo.ProtFileVerQueryVo;
import com.mic.eis.domain.vo.ProtMePartListQueryVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.*;
import com.mic.eis.util.ExcelWriteUtils;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.FastDfsUtils;
import com.mic.eis.utils.FileUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-10-27 10:38
 */
@Slf4j
@RestController
@RequestMapping("/protToolPlan")
public class ProtToolPlanController {


    @Resource
    private ProtToolPlanService protToolPlanService;


    @Resource
    private ProtService protService;

    @Resource
    private ProtFileService protFileService;

    @Resource
    private ProtFileVerService protFileVerService;

    @Resource
    private FastDfsUtils fastDfsUtils;

    @Resource
    private DataPermissionService dataPermissionService;

    // 获取所有 VERSION 用于提示 --1
    @OptionLog("获取所有 VERSION 用于提示")
    @ApiOperation("获取所有 VERSION 用于提示")
    @PostMapping("/selectAllVersion")
    public ResponseEntity<?> selectAllVersion(){
        return ResponseHelper.success(protToolPlanService.selectAllVersion());
    }


    // 获取所有 MATERIAL 第三级 用于提示 --2
    @OptionLog("获取所有 MATERIAL 第三级 用于提示")
    @ApiOperation("获取所有 MATERIAL 第三级 用于提示")
    @PostMapping("/selectAllMaterial")
    public ResponseEntity<?> selectAllMaterial(){
        return ResponseHelper.success(protToolPlanService.selectAllMaterial());
    }

    // 获取所有 COLOR No 用于提示 --3
    @OptionLog("获取所有 COLOR No  用于提示")
    @ApiOperation("获取所有 COLOR No 用于提示")
    @PostMapping("/selectAllColorNo")
    public ResponseEntity<?> selectAllColorNo(){
        return ResponseHelper.success(protToolPlanService.selectAllColorNo());
    }

    // 获取所有 PAINTING COLOR No. 用于提示 --4
    @OptionLog("获取所有 PAINTING COLOR No. 用于提示")
    @ApiOperation("获取所有 PAINTING COLOR No. 用于提示")
    @PostMapping("/selectAllPaintingColorNo")
    public ResponseEntity<?> selectAllPaintingColorNo(){
        return ResponseHelper.success(protToolPlanService.selectAllPaintingColorNo());
    }

    // 获取所有 PRINTING COLOR No. 用于提示 --5
    @OptionLog("获取所有 PRINTING COLOR No. 用于提示")
    @ApiOperation("获取所有 PRINTING COLOR No. 用于提示")
    @PostMapping("/selectAllPrintingColorNo")
    public ResponseEntity<?> selectAllPrintingColorNo(){
        return ResponseHelper.success(protToolPlanService.selectAllPrintingColorNo());
    }

    // 获取所有 COATING  CATEGORY 用于提示 --6
    @OptionLog("获取所有 COATING  CATEGORY 用于提示")
    @ApiOperation("获取所有 COATING  CATEGORY 用于提示")
    @PostMapping("/selectAllCoatingCategory")
    public ResponseEntity<?> selectAllCoatingCategory(){
        return ResponseHelper.success(protToolPlanService.selectAllCoatingCategory());
    }


    // 获取所有 TEXTURE  CATEGORY 用于提示 --7
    @OptionLog("获取所有 TEXTURE  CATEGORY 用于提示")
    @ApiOperation("获取所有 TEXTURE  CATEGORY 用于提示")
    @PostMapping("/selectAllTextureCategory")
    public ResponseEntity<?> selectAllTextureCategory(){
        return ResponseHelper.success(protToolPlanService.selectAllTextureCategory());
    }

    // 获取所有 INSERT NUT SPEC. 用于提示 --8
    @OptionLog("获取所有 INSERT NUT SPEC. 用于提示")
    @ApiOperation("获取所有 INSERT NUT SPEC. 用于提示")
    @PostMapping("/selectAllInsertNutSpec")
    public ResponseEntity<?> selectAllInsertNutSpec(){
        return ResponseHelper.success(protToolPlanService.selectAllInsertNutSpec());
    }

    // 获取所有 TOOLING  VENDER 用于提示 --9
    @OptionLog("获取所有 TOOLING  VENDER 用于提示")
    @ApiOperation("获取所有 TOOLING  VENDER 用于提示")
    @PostMapping("/selectAllToolingVender")
    public ResponseEntity<?> selectAllToolingVender(){
        return ResponseHelper.success(protToolPlanService.selectAllToolingVender());
    }


    // 获取所有 PR NUMBER. 用于提示 --10
    @OptionLog("获取所有 PR NUMBER. 用于提示")
    @ApiOperation("获取所有 PR NUMBER. 用于提示")
    @PostMapping("/selectAllPrNumber")
    public ResponseEntity<?> selectAllPrNumber(){
        return ResponseHelper.success(protToolPlanService.selectAllPrNumber());
    }


    // 获取所有 SUPPLIED VENDOR 用于提示 --11
    @OptionLog("获取所有 SUPPLIED VENDOR 用于提示")
    @ApiOperation("获取所有 SUPPLIED VENDOR 用于提示")
    @PostMapping("/selectAllSuppliedVendor")
    public ResponseEntity<?> selectAllSuppliedVendor(){
        return ResponseHelper.success(protToolPlanService.selectAllSuppliedVendor());
    }



    // 查询 tool plan 中的数据
    @OptionLog("通过 子项目id 、 版本 查询 子项目 tool plan 文件")
    @ApiOperation("通过 子项目id 、版本 查询 子项目 tool plan  文件")
    @PostMapping("/find")
    public ResponseEntity<?> selectByProtIdAndVersion(@Valid @RequestBody ProtMePartListQueryVo protMePartListQueryVo){
        return ResponseHelper.success(protToolPlanService.selectByProtIdAndVersion(protMePartListQueryVo));
    }


    // 从Me Part List中找出数据添加到 Tooling Plan
    @OptionLog("从Me Part List中找出数据 显示 以供添加")
    @ApiOperation("从Me Part List中找出数据 显示 以供添加 ")
    @PostMapping("/selectByMePartList/{sonProtId}")
    public ResponseEntity<?> selectByMePartList(@PathVariable Long sonProtId){
        return ResponseHelper.success(protToolPlanService.selectByMePartList(sonProtId));
    }

    // 从Me Part List中找出数据添加到 Tooling Plan
    @OptionLog("添加从Me Part List中找出的数据")
    @ApiOperation("添加从Me Part List中找出的数据 ")
    @PostMapping("/addByMePartList/{sonProtId}/{parentId}")
    public ResponseEntity<?> batchAddByMePartList(@PathVariable Long sonProtId,
                                                  @PathVariable Long parentId,
                                                  @RequestBody List<String> mePartListPartNums){
        try {
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), parentId) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), parentId)){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            protToolPlanService.batchAddByMePartList(sonProtId, mePartListPartNums);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 新增
    @OptionLog("新增 tool plan 子项")
    @ApiOperation("新增 tool plan 子项")
    @PostMapping("/add")
    public ResponseEntity<?> addToolPlanSon(@RequestBody ProtToolPlanDto protToolPlanDto){
        try {
            Prot prot = protService.selectProtByID(protToolPlanDto.getProtId());
            if(!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            return ResponseHelper.success(protToolPlanService.addToolPlanSon(protToolPlanDto));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 修改
    @OptionLog("修改 tool plan ")
    @ApiOperation("修改 tool plan ")
    @PostMapping("/edit")
    public ResponseEntity<?> editProtToolPlan(@RequestBody ProtToolPlanDto protToolPlanDto){
        try {
            Prot prot = protService.selectProtByID(protToolPlanDto.getProtId());
            if(!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            return ResponseHelper.success(protToolPlanService.editProtToolPlan(protToolPlanDto));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 删除
    @OptionLog("删除 tool plan ")
    @ApiOperation("删除 tool plan ")
    @PostMapping("/deleteById/{id}/{protParentId}/{parentId}")
    public ResponseEntity<?> deleteProtToolPlanById(@PathVariable Long id, @PathVariable Long protParentId, @PathVariable Long parentId){
        try {
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), protParentId) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), protParentId)){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            HashMap<String, Object> map = new HashMap<>();
            if (parentId == 0) {
                List<ProtToolPlan> protToolPlans = protToolPlanService.selectByParentId(id);
                if (protToolPlans.size() > 0){
                    map.put("msgFail", "请删除所有子项后再删除该数据！！！");
                    return ResponseHelper.success(map);
                }
            }
            protToolPlanService.deleteProtToolPlanById(id, parentId);
            return ResponseHelper.success(map);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 获取 最新的 更新时间  更新人
    @OptionLog("通过 子项目id 查询 最新 的 更新时间和更新人")
    @ApiOperation("通过 子项目id 查询 最新 的更新时间和更新人")
    @GetMapping("/findLastTime/{sonProtId}")
    public ResponseEntity<?> selectLastTimeUser(@PathVariable Long sonProtId){
        return ResponseHelper.success(protToolPlanService.selectLastTimeUser(sonProtId));
    }

    // 生成  新  版本  --ok
    @OptionLog("生成 Tool plan 新版本")
    @ApiOperation("生成 Tool plan 新版本")
    @PostMapping("/addVersion")
    public ResponseEntity<?> addVersionProtToolPlan(@Valid @RequestBody ProtFileVer protFileVer){
        try {
            Prot prot = protService.selectProtByID(protFileVer.getProtId());
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            return ResponseHelper.success(protToolPlanService.addNewVersion(protFileVer));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 恢复为之前的版本
    @OptionLog("根据 子项目id 和 文件版本 将 tool plan 恢复为之前的版本")
    @ApiOperation("根据 子项目id 和 文件版本 将 tool plan 恢复为之前的版本")
    @PostMapping("/backVersion")
    public ResponseEntity<?> backBeforeVersion(@Valid @RequestBody ProtMePartListQueryVo protMePartListQueryVo){
        try {
            Prot prot = protService.selectProtByID(protMePartListQueryVo.getProtId());
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            protToolPlanService.backBeforeVersion(protMePartListQueryVo);
            return ResponseHelper.success();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }

    }


    // 修改 PR Number
    @OptionLog("根据 子项目id 和 文件版本now 和 PR Number 更新now 版本 的所有的PR Number ")
    @ApiOperation("根据 子项目id 和 文件版本now 和 PR Number 更新now 版本 的所有的PR Number ")
    @PostMapping("/updatePrNum")
    public ResponseEntity<?> updatePrNum(@RequestBody ProtToolPlan protToolPlan){
        try {
            if (protToolPlan.getProtId() != null && protToolPlan.getPrNumber() != null){
                Prot prot = protService.selectProtByID(protToolPlan.getProtId());
                if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                        !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                    return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
                }
                protToolPlanService.updatePrNum(protToolPlan);
                return ResponseHelper.success();
            } else {
                return ResponseHelper.failed();
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 下载 特定版本的 文件
    @PostMapping("/downloadToolPlan")
    public ResponseEntity<?> downloadToolPlan(@Valid @RequestBody ProtMePartListQueryVo protMePartListQueryVo){
        try {
            List<ProtFile> protFileList = protFileService.selectByCategory("T3");
            if (protFileList.size() > 0) {
                ProtFile protFile = protFileList.get(0);
                String fileName = protFile.getName();
                byte[] bytes = fastDfsUtils.downloadFile(protFile.getResource());
                // tool plan 模板
                InputStream inputStream = new ByteArrayInputStream(bytes);
                // 特定版本 的 tool plan 数据
                List<ProtToolPlanDto> protToolPlanDtos = protToolPlanService.selectByProtIdAndVersion(protMePartListQueryVo);
                // 项目名
                Prot sonProt = protService.selectProtByID(protMePartListQueryVo.getProtId());
                Prot prot = protService.selectProtByID(sonProt.getParentId());
                String protName = prot.getName() + "/" + sonProt.getName();
                // approval
                ProtFileVerQueryVo protFileVerQueryVo = new ProtFileVerQueryVo();
                protFileVerQueryVo.setProtId(protMePartListQueryVo.getProtId());
                protFileVerQueryVo.setCategory("B");
                protFileVerQueryVo.setFileVer(protMePartListQueryVo.getFileVer());
                List<ProtFileVer> protFileVers = protFileVerService.selectByProtIdAndCategoryAndVer(protFileVerQueryVo);
                String approval = "";
                if (protFileVers.size() > 0){ approval = protFileVers.get(0).getApproval(); }
                // 这个版本的更新时间 和 更新人
                HashMap<String, String> lastTimeUserByVer = protToolPlanService.selectLastTimeUserByVer(protMePartListQueryVo.getProtId(), protMePartListQueryVo.getFileVer());
                // REV:
                String fileVer = protMePartListQueryVo.getFileVer();
                Workbook workbook = ExcelWriteUtils.writeToolPlan(inputStream, fileName, protToolPlanDtos, protName, approval, lastTimeUserByVer, fileVer);
                String filePath = FileUtils.getAbsoluteFile(fileName);
                FileOutputStream fos = new FileOutputStream(filePath);
                if (workbook!= null){
                    workbook.write(fos);
                }
                fos.close();
                if (workbook!= null){
                    workbook.close();
                }
                inputStream.close();
                return ResponseHelper.success(fileName);
            } else {
                return ResponseHelper.failed();
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

}
