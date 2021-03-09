package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.dto.ProtDailyReportQueryRecordDto;
import com.mic.eis.domain.model.Prot;
import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.model.ProtToolTrackList;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DataPermissionService;
import com.mic.eis.service.ProtFileService;
import com.mic.eis.service.ProtService;
import com.mic.eis.service.ProtToolTrackListService;
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
 * @date 2020-10-15 15:07
 */
@Slf4j
@RestController
@RequestMapping("/protToolTrackList")
public class ProtToolTrackListController {

    @Resource
    private ProtToolTrackListService protToolTrackListService;

    @Resource
    private ProtService protService;

    @Resource
    private DataPermissionService dataPermissionService;

    @Resource
    private ProtFileService protFileService;

    @Resource
    private FastDfsUtils fastDfsUtils;

    // 不分页查询
    @OptionLog("通过 子项目id 查询 最新 的数据")
    @ApiOperation("通过 子项目id 查询 最新 的数据")
    @GetMapping("/findByProtId/{sonProtId}")
    public ResponseEntity<?> selectBySonProtId(@PathVariable Long sonProtId) {
        return ResponseHelper.success(protToolTrackListService.selectBySonProtId(sonProtId, ""));
    }

    // 不分页查询, 用于 Daily Report
    @OptionLog("通过 项目id、子项目id、bug等级 查询 最新 的数据")
    @ApiOperation("通过 项目id、子项目id、bug等级 查询 最新 的数据")
    @PostMapping("/findForDailyReport")
    public ResponseEntity<?> findForDailyReport(@Valid @RequestBody ProtDailyReportQueryRecordDto protDailyReportQueryRecordDto){
        try {
            return ResponseHelper.success(protToolTrackListService.findForDailyReport(protDailyReportQueryRecordDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    // 新增 issue
    @OptionLog("添加 protToolTrackList 新 issue")
    @ApiOperation("添加 protToolTrackList 新 issue")
    @PostMapping("/addIssue")
    public ResponseEntity<?> addProtToolTrackListIssue(@Valid @RequestBody ProtToolTrackList protToolTrackList){
        try {
            Prot prot = protService.selectProtByID(protToolTrackList.getProtId());
            if(!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            return ResponseHelper.success(protToolTrackListService.addProtToolTrackListIssue(protToolTrackList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 通过id删除  --ok ??
    @OptionLog("通过 id 删除 protToolTrackList")
    @ApiOperation("通过 id 删除 protToolTrackList")
    @GetMapping("/deleteById/{id}/{parentId}")
    public ResponseEntity<?> deleteProtToolTrackListById(@PathVariable Long id, @PathVariable Long parentId){
        if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), parentId) &&
                !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), parentId)){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            ProtToolTrackList protToolTrackList = protToolTrackListService.selectToolTrackListById(id);
            HashMap<String, Object> map = new HashMap<>();
            if (protToolTrackList.getIsFirst().equals("false")) {
                map.put("msgFail", "非首次加入Tx, 不允许删除！！！");
                return ResponseHelper.success(map);
            }
            protToolTrackListService.deleteProtToolTrackListById(id);
            return ResponseHelper.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 获取 最新的 更新时间  更新人
    @OptionLog("通过 子项目id 查询 最新 的 更新时间和更新人")
    @ApiOperation("通过 子项目id 查询 最新 的更新时间和更新人")
    @GetMapping("/findLastTime/{sonProtId}")
    public ResponseEntity<?> selectLastTime(@PathVariable Long sonProtId){
        return ResponseHelper.success(protToolTrackListService.selectLastTimeUser(sonProtId));
    }

    // 修改 protToolTrackList issue
    @OptionLog("修改 protToolTrackList issue")
    @ApiOperation("修改 protToolTrackList issue")
    @PostMapping("/editIssue")
    public ResponseEntity<?> editProtToolTrackListIssue(@Valid @RequestBody ProtToolTrackList protToolTrackList){
        if(protToolTrackList.getId() != null) {
            try {
                Prot prot = protService.selectProtByID(protToolTrackList.getProtId());
                if(!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                        !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                    return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
                }
                boolean result = protToolTrackListService.editProtToolTrackListIssue(protToolTrackList);
                return result ? ResponseHelper.success() : ResponseHelper.failed();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        }
        return ResponseHelper.failed();
    }

    // 获取所有的 root cause 用于提示
    @OptionLog("获取所有的 root cause 用于提示")
    @ApiOperation("获取所有的 root cause 用于提示")
    @PostMapping("/selectAllRootCause")
    public ResponseEntity<?> selectAllRootCauseTips(){
        return ResponseHelper.success(protToolTrackListService.selectAllRootCauseTips());
    }

    // 获取 最新的 Latest Tx
    @OptionLog("获取 最新的 Latest Tx")
    @ApiOperation("获取 最新的 Latest Tx")
    @GetMapping("/selectLatestTx/{sonProtId}")
    public ResponseEntity<?> selectLatestTxBySonProtId(@PathVariable Long sonProtId) {
        return ResponseHelper.success(protToolTrackListService.selectLatestTxBySonProtId(sonProtId));
    }

    // 获取 Tx History
    @OptionLog("获取 Tx History")
    @ApiOperation("获取 Tx History")
    @GetMapping("/selectTxHistory/{sonProtId}")
    public ResponseEntity<?> selectTxHistoryBySonProtId(@PathVariable Long sonProtId) {
        return ResponseHelper.success(protToolTrackListService.selectTxHistoryBySonProtId(sonProtId));
    }

    // sync tooling plan
    @OptionLog("同步 tooling plan 数据")
    @ApiOperation("同步 tooling plan 数据")
    @GetMapping("/syncToolPlan/{sonProtId}")
    public ResponseEntity<?> syncToolPlanData(@PathVariable Long sonProtId) {
        try {
            Prot prot = protService.selectProtByID(sonProtId);
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            protToolTrackListService.syncToolPlanData(sonProtId);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    // 获取 Issue Report
    @OptionLog("获取 Issue Report")
    @ApiOperation("获取 Issue Report")
    @GetMapping("/selectIssueReport/{sonProtId}")
    public ResponseEntity<?> selectIssueReportBySonProtId(@PathVariable Long sonProtId) {
        return ResponseHelper.success(protToolTrackListService.selectIssueReportBySonProtId(sonProtId));
    }

    // Issue Tracking 这个 issue 的 所有 Tx 数据
    @OptionLog("Issue Tracking 这个 issue 的 所有 Tx 数据")
    @ApiOperation("Issue Tracking 这个 issue 的 所有 Tx 数据")
    @GetMapping("/selectIssueTracking/{sonProtId}/{issueId}")
    public ResponseEntity<?> selectIssueTracking(@PathVariable Long sonProtId, @PathVariable Long issueId) {
        return ResponseHelper.success(protToolTrackListService.selectIssueTracking(sonProtId, issueId));
    }

    // Issue Tracking / ReOpen issue
    @OptionLog("Issue Tracking / ReOpen issue")
    @ApiOperation("Issue Tracking / ReOpen issue")
    @GetMapping("/reOpenIssue/{sonProtId}/{issueId}")
    public ResponseEntity<?> reOpenIssue(@PathVariable Long sonProtId, @PathVariable Long issueId) {
        try {
            Prot prot = protService.selectProtByID(sonProtId);
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            protToolTrackListService.reOpenIssue(issueId);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    // 下载最新 Tx 数据
    @GetMapping("/downloadToolTrackList/{sonProtId}")
    public ResponseEntity<?> downloadToolTrackList(@PathVariable Long sonProtId) {
        try {
            List<ProtFile> protFileList = protFileService.selectByCategory("T2");
            if (protFileList.size() > 0) {
                ProtFile protFile = protFileList.get(0);
                String fileName = protFile.getName();
                byte[] bytes = fastDfsUtils.downloadFile(protFile.getResource());
                // ToolTrackList 模板
                InputStream inputStream = new ByteArrayInputStream(bytes);
                // Excel 数据
                List<ProtToolTrackList> protToolTrackLists = protToolTrackListService.selectExcelData(sonProtId);
                // 项目名称
                Prot sonProt = protService.selectProtByID(sonProtId);
                Prot prot = protService.selectProtByID(sonProt.getParentId());
                String protName = prot.getName() + "/" + sonProt.getName();
                // 获取 更新时间 更新人
                HashMap<String, String> latestTimeUser = protToolTrackListService.selectLastTimeUser(sonProtId);
                Workbook workbook = ExcelWriteUtils.writeToolTrackList(inputStream, fileName, protToolTrackLists, protName, latestTimeUser);
                String filePath = FileUtils.getAbsoluteFile(fileName);
                FileOutputStream fos = new FileOutputStream(filePath);
                if (workbook!= null){ workbook.write(fos); }
                fos.close();
                if (workbook!= null){ workbook.close(); }
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
