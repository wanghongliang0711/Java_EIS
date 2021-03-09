package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.Prot;
import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.model.ProtMePartList;
import com.mic.eis.domain.vo.ProtMePartListQueryVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DataPermissionService;
import com.mic.eis.service.ProtFileService;
import com.mic.eis.service.ProtMePartListService;
import com.mic.eis.service.ProtService;
import com.mic.eis.util.ExcelWriteUtils;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.FastDfsUtils;
import com.mic.eis.utils.FileUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-09-08 9:31
 */
@Slf4j
@RestController
@RequestMapping("/protMePartList")
public class ProtMePartListController {

    @Resource
    private ProtMePartListService protMePartListService;

    @Resource
    private DataPermissionService dataPermissionService;

    @Resource
    private ProtService protService;

    @Resource
    private ProtFileService protFileService;

    @Resource
    private FastDfsUtils fastDfsUtils;

    // 不分页查询   --ok
    @OptionLog("通过 子项目id 、 版本 查询 子项目 MePartList 文件")
    @ApiOperation("通过 子项目id 、版本 查询 子项目 MePartList 文件")
    @PostMapping("/find")
    public ResponseEntity<?> selectByProtIdAndVersion(@Valid @RequestBody ProtMePartListQueryVo protMePartListQueryVo){
        return ResponseHelper.success(protMePartListService.selectByProtIdAndVersion(protMePartListQueryVo));
    }

    // 添加  -- 前台添加  --ok ??
    @OptionLog("添加Me Part List")
    @ApiOperation("添加Me Part List")
    @PostMapping("/add")
    public ResponseEntity<?> addMePartList(@Valid @RequestBody ProtMePartList protMePartList) {
        Prot prot = protService.selectProtByID(protMePartList.getProtId());
        if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            return ResponseHelper.success(protMePartListService.addProtMePartList(protMePartList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    // 通过id删除  --ok ??
    @OptionLog("通过 id 删除 MePartList")
    @ApiOperation("通过 id 删除 MePartList")
    @GetMapping("/deleteById/{id}/{parentId}")
    public ResponseEntity<?> deleteProtMePartListById(@PathVariable Long id, @PathVariable Long parentId){
        if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), parentId) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), parentId)){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            protMePartListService.deleteProtMePartListById(id);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 修改       --ok ??
    @OptionLog("修改 Me Part List")
    @ApiOperation("修改 Me Part List")
    @PostMapping("/edit")
    public ResponseEntity<?> editProtMePartList(@Valid @RequestBody ProtMePartList protMePartList) {
        Prot prot = protService.selectProtByID(protMePartList.getProtId());
        if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            return ResponseHelper.success(protMePartListService.editProtMePartList(protMePartList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }

    }


    // 生成  新  版本  --ok
    @OptionLog("生成 Me Part List 新版本")
    @ApiOperation("生成 Me Part List 新版本")
    @PostMapping("/addVersion")
    public ResponseEntity<?> addVersionProtMePartList(@Valid @RequestBody ProtFileVer protFileVer){
        Prot prot = protService.selectProtByID(protFileVer.getProtId());
        if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            return ResponseHelper.success(protMePartListService.addNewVersion(protFileVer));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 匹配Part Number. --ok
    @OptionLog("根据Part description 匹配 Part Number")
    @ApiOperation("根据Part description 匹配 Part Number")
    @GetMapping("/matchPartNum/{sonProtId}/{parentId}")
    public ResponseEntity<?> matchPartNum(@PathVariable Long sonProtId, @PathVariable Long parentId){
        if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), parentId) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), parentId)){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            return ResponseHelper.success(protMePartListService.matchPartNum(sonProtId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    // 恢复为之前的版本  --ok
    @OptionLog("根据 子项目id 和 文件版本 将 me part list 恢复为之前的版本")
    @ApiOperation("根据 子项目id 和 文件版本 将 me part list 恢复为之前的版本")
    @PostMapping("/backVersion")
    public ResponseEntity<?> backBeforeVersion(@Valid @RequestBody ProtMePartListQueryVo protMePartListQueryVo){
        Prot prot = protService.selectProtByID(protMePartListQueryVo.getProtId());
        if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            protMePartListService.backBeforeVersion(protMePartListQueryVo);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }

    }


    // 上传 me part list Excel --ok
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("sonProtId") Long sonProtId){
        try {
            Prot prot = protService.selectProtByID(sonProtId);
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            return ResponseHelper.success(protMePartListService.uploadMePartListFile(file, sonProtId));
        } catch (Exception e ) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 按预设项 覆盖description --ok
//    @OptionLog("根据 按预设项 覆盖description")
//    @ApiOperation("根据 按预设项 覆盖description")
    @PostMapping("/overDes")
    public ResponseEntity<?> overDes(@RequestBody List<ProtMePartList> protMePartLists){
        try {
            protMePartListService.overDes(protMePartLists);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 下载 特定版本的 文件
    @PostMapping("/downloadMePartList")
    public ResponseEntity<?> downloadMePartList(@Valid @RequestBody ProtMePartListQueryVo protMePartListQueryVo){
        try {
            List<ProtFile> protFileList = protFileService.selectByCategory("T1");
            if (protFileList.size() > 0) {
                ProtFile protFile = protFileList.get(0);
                String fileName = protFile.getName();
                byte[] bytes = fastDfsUtils.downloadFile(protFile.getResource());
                // me part list 模板
                InputStream inputStream = new ByteArrayInputStream(bytes);
                // 特定版本 的 me part list 数据
                List<ProtMePartList> protMePartLists = protMePartListService.selectByProtIdAndVersion(protMePartListQueryVo);
                // 文件名
                Prot sonProt = protService.selectProtByID(protMePartListQueryVo.getProtId());
                Prot prot = protService.selectProtByID(sonProt.getParentId());
                String protName = prot.getName() + "/" + sonProt.getName();
                Workbook workbook = ExcelWriteUtils.writeMePartList(inputStream, fileName, protMePartLists, protName);
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
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }




}
