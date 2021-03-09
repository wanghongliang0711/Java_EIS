package com.mic.eis.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.Prot;
import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.vo.ProtFileUploadVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DataPermissionService;
import com.mic.eis.service.ProtFileService;
import com.mic.eis.service.ProtService;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.FastDfsUtils;
import com.mic.eis.utils.FileUtils;
import com.mic.eis.utils.ToolUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author blake.wang
 * @date 2020-08-10 11:03
 */
@Slf4j
@RestController
@RequestMapping("/protFile")
public class ProtFileController {

    @Resource
    private ProtFileService protFileService;

    @Resource
    private FastDfsUtils fastDfsUtils;

    @Resource
    private DataPermissionService dataPermissionService;

    @Resource
    private ProtService protService;

    // 以前写的，似乎弃用了
    @OptionLog("通过 子项目id、文件类别 查询 项目文件")
    @ApiOperation("通过 子项目id、文件类别 查询 项目文件")
    @PostMapping("/find")
    public ResponseEntity<?> selectByProtIdAndCategory(@RequestBody ProtFile protFile){
        if (protFile.getCategory() == null) {
            protFile.setCategory("");
        }
        return ResponseHelper.success(protFileService.selectByProtIdAndCategory(protFile));
    }

    // 通过 子项目id、文件类别 查询 项目文件 2020/12/24
    @OptionLog("通过 子项目id、文件类别 查询 项目文件")
    @ApiOperation("通过 子项目id、文件类别 查询 项目文件")
    @PostMapping("/select")
    public ResponseEntity<?> selectByCategoryAndProtId(@RequestBody ProtFile protFile){
        if (protFile.getCategory() == null) {
            protFile.setCategory("subProtFile");
        }
        return ResponseHelper.success(protFileService.selectByCategoryAndProtId(protFile));
    }

    // 通过 子项目id、查询最新的 数据类别
    @OptionLog("通过 子项目id 查询 最新的 项目文件")
    @ApiOperation("通过 子项目id 查询 最新的 项目文件")
    @PostMapping("/selectBySonProtId/{sonProtId}")
    public ResponseEntity<?> selectNewestFile(@PathVariable Long sonProtId) {
        return ResponseHelper.success(protFileService.selectNewestFile(sonProtId));
    }


//    @OptionLog("通过 子项目id、文件类别 上传 项目文件")
//    @ApiOperation("通过 子项目id、文件类别 上传 项目文件")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@Valid ProtFileUploadVo protFileUploadVo) {
        try {
            protFileService.uploadFile(protFileUploadVo);
            return ResponseHelper.success();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }

    }

    @PostMapping("/uploadForDocument")
    public ResponseEntity<?> uploadFileForDocument(@Valid ProtFileUploadVo protFileUploadVo) {
        try {
            Prot prot = protService.selectProtByID(protFileUploadVo.getProtId());
            if(!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), prot.getParentId()) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            protFileService.uploadFile(protFileUploadVo);
            return ResponseHelper.success();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }

    }

    // 上传 Template 文件，所有的Template文件都可以使用这个接口
    @PostMapping("/uploadTemplate")
    public ResponseEntity<?> uploadTemplate(@Valid ProtFileUploadVo protFileUploadVo) throws IOException {
        protFileService.uploadTemplateFile(protFileUploadVo);
        return ResponseHelper.success();
    }

    @GetMapping("/download/{protFileId}")
    public ResponseEntity<?> downloadFileById(@PathVariable Long protFileId) throws IOException {
        ProtFile protFile = protFileService.downloadFileById(protFileId);
//        String fileName = GeneratorCodeUtil.generateKey().toString() + ".xlsx";
//        String fileName = new String(protFile.getName().getBytes("gb2312"), "ISO8859-1");
        String fileName = protFile.getName();
        byte[] bytes = fastDfsUtils.downloadFile(protFile.getResource());
        String filePath = FileUtils.getAbsoluteFile(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseHelper.success(fileName);
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String filePath = ToolUtil.getDownloadPath() + fileName;
            response.setCharacterEncoding("utf-8");
            // 下载使用"application/octet-stream"更标准
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + FileUtils.setFileDownloadHeader(request, fileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 可以参考：从fastDfs获取 模板，修改后，输出到 浏览器下载
     *D:\Study\vue\vueblog-springboot\src\main\
     * java\com\studyVue\controller\FastdfsController.java
     */
//    @GetMapping("/download/{protFileId}")
//    public ResponseEntity<?> downloadFileData(@PathVariable Long protFileId) throws IOException {
//        ProtFile protFile = protFileService.downloadFileById(protFileId);
////        String fileName = GeneratorCodeUtil.generateKey().toString() + ".xlsx";
////        String fileName = new String(protFile.getName().getBytes("gb2312"), "ISO8859-1");
//        String fileName = protFile.getName();
//        byte[] bytes = fastDfsUtils.downloadFile(protFile.getResource());
//        InputStream inputStream = new ByteArrayInputStream(bytes);
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        // 得到表
//        Sheet sheet = workbook.getSheetAt(0);
//        // 得到行
//        Row row = sheet.createRow(1);
//        // 得到列
//        Cell cell = row.createCell(1);
//        cell.setCellValue("今日观众----");
//
//        String filePath = FileUtils.getAbsoluteFile(fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(filePath);
//            workbook.write(fos);
////            fos.write(bytes);
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ResponseHelper.success(fileName);
//    }




}
