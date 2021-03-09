package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.model.ProtPilotRunReport;
import com.mic.eis.domain.vo.ProtMePartListQueryVo;
import com.mic.eis.domain.vo.ProtPilotRunReportVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DataPermissionService;
import com.mic.eis.service.ProtFileService;
import com.mic.eis.service.ProtPilotRunReportService;
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
 * @date 2021-01-04 16:59
 */
@Slf4j
@RestController
@RequestMapping("/protPilotRunReport")
public class ProtPilotRunReportController {

    @Resource
    private ProtPilotRunReportService protPilotRunReportService;

    @Resource
    private DataPermissionService dataPermissionService;

    @Resource
    private ProtFileService protFileService;

    @Resource
    private FastDfsUtils fastDfsUtils;


    // 不分页查询 根据子项目id, 文件版本， List<String> severity， List<String> status 查询
    @OptionLog("根据子项目id、文件版本、severity、status 查询数据")
    @ApiOperation("根据子项目id、文件版本、severity、status 查询数据")
    @PostMapping("/find")
    public ResponseEntity<?> findProtPilotRunReport(@Valid @RequestBody ProtPilotRunReportVo protPilotRunReportVo) {
        try {
            return ResponseHelper.success(protPilotRunReportService.findProtPilotRunReport(protPilotRunReportVo));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 通过id删除
    @OptionLog("通过 id 删除 ProtPilotRunReport")
    @ApiOperation("通过 id 删除 ProtPilotRunReport")
    @GetMapping("/deleteById/{id}/{mainProtId}")
    public ResponseEntity<?> deleteProtPilotRunReportById(@PathVariable Long id, @PathVariable Long mainProtId) {
        if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), mainProtId) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), mainProtId)){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            protPilotRunReportService.deleteProtPilotRunReportById(id);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }

    }


    // 添加 ProtPilotRunReport 数据
    @OptionLog("添加 ProtPilotRunReport 数据")
    @ApiOperation("添加 ProtPilotRunReport 数据")
    @PostMapping("/add/{mainProtId}")
    public ResponseEntity<?> addProtPilotRunReport(@PathVariable Long mainProtId, @Valid @RequestBody ProtPilotRunReport protPilotRunReport){
        try {
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), mainProtId) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), mainProtId)){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            return ResponseHelper.success(protPilotRunReportService.addProtPilotRunReport(protPilotRunReport));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 修改 ProtPilotRunReport 数据
    @OptionLog("修改 ProtPilotRunReport 数据")
    @ApiOperation("修改 ProtPilotRunReport 数据")
    @PostMapping("/edit/{mainProtId}")
    public ResponseEntity<?> editProtPilotRunReport(@PathVariable Long mainProtId, @Valid @RequestBody ProtPilotRunReport protPilotRunReport) {
        if (protPilotRunReport.getId() != null) {
            try {
                if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), mainProtId) &&
                        !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), mainProtId)){
                    return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
                }
                return ResponseHelper.success(protPilotRunReportService.editProtPilotRunReport(protPilotRunReport));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        }
        return ResponseHelper.failed();
    }

    // 获取 7 个 提示
    @OptionLog("获取 7 个 提示")
    @ApiOperation("获取 7 个 提示")
    @PostMapping("/selectAllTips")
    public ResponseEntity<?> selectAllTips() {
        return ResponseHelper.success(protPilotRunReportService.selectAllTips());
    }

    // 获取 Severity & Status 提示
    @OptionLog("获取 Severity & Status 提示")
    @ApiOperation("获取 Severity & Status 提示")
    @PostMapping("/selectQueryTips")
    public ResponseEntity<?> selectQueryTips() {
        return ResponseHelper.success(protPilotRunReportService.selectQueryTips());
    }

    // 生成  新  版本
    @OptionLog("生成 PilotRunReport 新版本")
    @ApiOperation("生成 PilotRunReport 新版本")
    @PostMapping("/addVersion/{mainProtId}")
    public ResponseEntity<?> addVersionPilotRunReport(@PathVariable Long mainProtId, @Valid @RequestBody ProtFileVer protFileVer){
        try {
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), mainProtId) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), mainProtId)){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            return ResponseHelper.success(protPilotRunReportService.addNewVersionPilotRunReport(protFileVer));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 恢复为之前的版本
    @OptionLog("根据 项目id 和 文件版本 将 PilotRunReport 恢复为之前的版本")
    @ApiOperation("根据 项目id 和 文件版本 将 PilotRunReport 恢复为之前的版本")
    @PostMapping("/backVersion/{mainProtId}")
    public ResponseEntity<?> backBeforeVersion(@PathVariable Long mainProtId, @Valid @RequestBody ProtMePartListQueryVo protMePartListQueryVo) {
        try {
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), mainProtId) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), mainProtId)){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            protPilotRunReportService.backBeforeVersion(protMePartListQueryVo);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 下载 特定版本的 文件
    @PostMapping("/downloadPilotRunReport")
    public ResponseEntity<?> downloadPilotRunReport(@Valid @RequestBody ProtMePartListQueryVo protMePartListQueryVo){
        try {
            List<ProtFile> protFileList = protFileService.selectByCategory("T4");
            if (protFileList.size() > 0) {
                ProtFile protFile = protFileList.get(0);
                String fileName = protFile.getName();
                byte[] bytes = fastDfsUtils.downloadFile(protFile.getResource());
                InputStream inputStream = new ByteArrayInputStream(bytes);
                List<ProtPilotRunReport> protPilotRunReports = protPilotRunReportService.selectByProtIdAndVersion(protMePartListQueryVo.getProtId(), protMePartListQueryVo.getFileVer());
                Workbook workbook = ExcelWriteUtils.writePilotRunReport(inputStream, fileName, protPilotRunReports);
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


    // 上传 Pilot Run Report Excel
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("protId") Long protId,
                                        @RequestParam("mainProtId") Long mainProtId) {
        try {
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), mainProtId) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), mainProtId)){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            protPilotRunReportService.uploadPilotRunReportFile(file, protId);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

}
