package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.dto.ProtFileDto;
import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.vo.ProtFileUploadVo;
import com.mic.eis.mapper.ProtFileMapper;
import com.mic.eis.service.ProtFileService;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.FastDfsUtils;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-10 10:59
 */
@Service
public class ProtFileServiceImpl extends ServiceImpl<ProtFileMapper, ProtFile> implements ProtFileService {


    @Resource
    private ProtFileMapper protFileMapper;

    @Resource
    private FastDfsUtils fastDfsUtils;

    @Override
    public List<ProtFile> selectByProtIdAndCategory(ProtFile protFile) {
        QueryWrapper<ProtFile> wrapper = new QueryWrapper<>();
        wrapper.eq("prot_id", protFile.getProtId())
                .eq("is_delete", 0)
                .like("category", protFile.getCategory());
        return protFileMapper.selectList(wrapper);
    }

    @Override
    public List<ProtFileDto> selectByCategoryAndProtId(ProtFile protFile) {
        List<ProtFileDto> resultList = new ArrayList<>();
        if (protFile.getCategory().equals("")) {
            protFile.setCategory("subProtFile");
        }
        QueryWrapper<ProtFile> wrapper = new QueryWrapper<>();
        wrapper.eq("prot_id", protFile.getProtId())
                .eq("is_delete", 0)
                .like("category", protFile.getCategory())
                .orderByDesc("create_time");  //  降序
        List<ProtFile> protFileList = protFileMapper.selectList(wrapper);
        List<ProtFileDto> aList = new ArrayList<>(), bList = new ArrayList<>(), cList = new ArrayList<>();
        List<ProtFileDto> dList = new ArrayList<>(), eList = new ArrayList<>(), fList = new ArrayList<>();
        List<ProtFileDto> gList = new ArrayList<>(), hList = new ArrayList<>(), iList = new ArrayList<>();
        for (ProtFile file : protFileList) {
            switch (file.getCategory()) {
                case "subProtFile_A":
                    aList.add(setProtFileToProtFileDto(file, "ME Test Report"));
                    break;
                case "subProtFile_B":
                    bList.add(setProtFileToProtFileDto(file, "Dfx Guidline"));
                    break;
                case "subProtFile_C":
                    cList.add(setProtFileToProtFileDto(file, "Feasibility Study Report"));
                    break;
                case "subProtFile_D":
                    dList.add(setProtFileToProtFileDto(file, "Structure Analysis Report"));
                    break;
                case "subProtFile_E":
                    eList.add(setProtFileToProtFileDto(file, "Themal Analysis Report"));
                    break;
                case "subProtFile_F":
                    fList.add(setProtFileToProtFileDto(file, "Tooling DFM Reports"));
                    break;
                case "subProtFile_G":
                    gList.add(setProtFileToProtFileDto(file, "DFMEA"));
                    break;
                case "subProtFile_H":
                    hList.add(setProtFileToProtFileDto(file, "Tolerance Analysis Report"));
                    break;
                case "subProtFile_I":
                    iList.add(setProtFileToProtFileDto(file, "Assembly Notice"));
                    break;
            }
        }
        if (aList.size() > 0) {
            aList.get(0).setIsUpload("true");
        }
        if (bList.size() > 0) {
            bList.get(0).setIsUpload("true");
        }
        if (cList.size() > 0) {
            cList.get(0).setIsUpload("true");
        }
        if (dList.size() > 0) {
            dList.get(0).setIsUpload("true");
        }
        if (eList.size() > 0) {
            eList.get(0).setIsUpload("true");
        }
        resultList.addAll(aList);
        resultList.addAll(bList);
        resultList.addAll(cList);
        resultList.addAll(dList);
        resultList.addAll(eList);
        if (fList.size() > 0) {
            fList.get(0).setIsUpload("true");
        }
        resultList.addAll(fList);
        if (gList.size() > 0) {
            gList.get(0).setIsUpload("true");
        }
        resultList.addAll(gList);
        if (hList.size() > 0) {
            hList.get(0).setIsUpload("true");
        }
        resultList.addAll(hList);
        if (iList.size() > 0) {
            iList.get(0).setIsUpload("true");
        }
        resultList.addAll(iList);
        return resultList;
    }


    @Override
    public List<ProtFile> selectByCategory(String category) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("category", category);
        map.put("is_delete", 0);
        return protFileMapper.selectByMap(map);
    }

    @Override
    public void deleteProtFileByProtId(Long protId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("prot_id", protId);
        map.put("is_delete", 0);
        protFileMapper.deleteByMap(map);
    }

    @Override
    public List<ProtFileDto> selectNewestFile(Long sonProtId) {
        // 没有 最新的 文档，download 按钮 变为灰色
        QueryWrapper<ProtFile> wrapper = new QueryWrapper<>();
        wrapper.eq("prot_id", sonProtId)
                .eq("is_delete", 0)
                .like("category", "subProtFile")
                .orderByDesc("create_time");  //  降序
        List<ProtFile> protFileList = protFileMapper.selectList(wrapper);
        List<ProtFileDto> resultList = new ArrayList<>();
        List<ProtFileDto> aList = new ArrayList<>(), bList = new ArrayList<>();
        List<ProtFileDto> cList = new ArrayList<>();
        List<ProtFileDto> gList = new ArrayList<>(), hList = new ArrayList<>(), iList = new ArrayList<>();
        List<ProtFileDto> dList = new ArrayList<>(), eList = new ArrayList<>(), fList = new ArrayList<>();
        for (ProtFile file : protFileList) {
            switch (file.getCategory()) {
                case "subProtFile_A":
                    if (aList.size() == 0) {
                        aList.add(setProtFileToProtFileDto(file, "ME Test Report"));
                    }
                    break;
                case "subProtFile_B":
                    if (bList.size() == 0) {
                        bList.add(setProtFileToProtFileDto(file, "Dfx Guidline"));
                    }
                    break;
                case "subProtFile_C":
                    if (cList.size() == 0) {
                        cList.add(setProtFileToProtFileDto(file, "Feasibility Study Report"));
                    }
                    break;
                case "subProtFile_D":
                    if (dList.size() == 0) {
                        dList.add(setProtFileToProtFileDto(file, "Structure Analysis Report"));
                    }
                    break;
                case "subProtFile_E":
                    if (eList.size() == 0) {
                        eList.add(setProtFileToProtFileDto(file, "Themal Analysis Report"));
                    }
                    break;
                case "subProtFile_F":
                    if (fList.size() == 0) {
                        fList.add(setProtFileToProtFileDto(file, "Tooling DFM Reports"));
                    }
                    break;
                case "subProtFile_G":
                    if (gList.size() == 0) {
                        gList.add(setProtFileToProtFileDto(file, "DFMEA"));
                    }
                    break;
                case "subProtFile_H":
                    if (hList.size() == 0) {
                        hList.add(setProtFileToProtFileDto(file, "Tolerance Analysis Report"));
                    }
                    break;
                case "subProtFile_I":
                    if (iList.size() == 0) {
                        iList.add(setProtFileToProtFileDto(file, "Assembly Notice"));
                    }
                    break;
            }
        }
        if (aList.size() == 1) {
            resultList.addAll(aList);
        } else {
            ProtFileDto protFileDto = new ProtFileDto();
            protFileDto.setId(1L);
            protFileDto.setFileType("ME Test Report");
            protFileDto.setCategory("subProtFile_A");
            resultList.add(protFileDto);
        }
        if (bList.size() == 1) {
            resultList.addAll(bList);
        } else {
            ProtFileDto protFileDto = new ProtFileDto();
            protFileDto.setId(2L);
            protFileDto.setFileType("Dfx Guidline");
            protFileDto.setCategory("subProtFile_B");
            resultList.add(protFileDto);
        }
        if (cList.size() == 1) {
            resultList.addAll(cList);
        } else {
            ProtFileDto protFileDto = new ProtFileDto();
            protFileDto.setId(3L);
            protFileDto.setFileType("Feasibility Study Report");
            protFileDto.setCategory("subProtFile_C");
            resultList.add(protFileDto);
        }
        if (dList.size() == 1) {
            resultList.addAll(dList);
        } else {
            ProtFileDto protFileDto = new ProtFileDto();
            protFileDto.setId(4L);
            protFileDto.setFileType("Structure Analysis Report");
            protFileDto.setCategory("subProtFile_D");
            resultList.add(protFileDto);
        }
        if (eList.size() == 1) {
            resultList.addAll(eList);
        } else {
            ProtFileDto protFileDto = new ProtFileDto();
            protFileDto.setId(5L);
            protFileDto.setFileType("Themal Analysis Report");
            protFileDto.setCategory("subProtFile_E");
            resultList.add(protFileDto);
        }
        if (fList.size() == 1) {
            resultList.addAll(fList);
        } else {
            ProtFileDto protFileDto = new ProtFileDto();
            protFileDto.setId(6L);
            protFileDto.setFileType("Tooling DFM Reports");
            protFileDto.setCategory("subProtFile_F");
            resultList.add(protFileDto);
        }
        if (gList.size() == 1) {
            resultList.addAll(gList);
        } else {
            ProtFileDto protFileDto = new ProtFileDto();
            protFileDto.setId(7L);
            protFileDto.setFileType("DFMEA");
            protFileDto.setCategory("subProtFile_G");
            resultList.add(protFileDto);
        }
        if (hList.size() == 1) {
            resultList.addAll(hList);
        } else {
            ProtFileDto protFileDto = new ProtFileDto();
            protFileDto.setId(8L);
            protFileDto.setFileType("Tolerance Analysis Report");
            protFileDto.setCategory("subProtFile_H");
            resultList.add(protFileDto);
        }
        if (iList.size() == 1) {
            resultList.addAll(iList);
        } else {
            ProtFileDto protFileDto = new ProtFileDto();
            protFileDto.setId(9L);
            protFileDto.setFileType("Assembly Notice");
            protFileDto.setCategory("subProtFile_I");
            resultList.add(protFileDto);
        }
        for (ProtFileDto protFileDto : resultList) {
            protFileDto.setIsUpload("true");
        }
        return resultList;
    }

    @Override
    public void uploadFile(ProtFileUploadVo protFileUploadVo) throws IOException {
        try {
            ProtFile protFile = new ProtFile();
            protFile.setId(GeneratorCodeUtil.generateKey());
            protFile.setName(protFileUploadVo.getFile().getOriginalFilename());
            double fileSize = Double.parseDouble(String.valueOf(protFileUploadVo.getFile().getSize())) / 1024;
            BigDecimal b = new BigDecimal(fileSize);
            // 2表示2位 ROUND_HALF_UP表明四舍五入
            protFile.setSize(b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            protFile.setProtId(protFileUploadVo.getProtId());
            protFile.setResource(fastDfsUtils.uploadFile(protFileUploadVo.getFile()));
            protFile.setRemark(protFileUploadVo.getRemark());
            protFile.setCategory(protFileUploadVo.getCategory());
            protFile.setCreateTime(new Date());
            protFile.setCreateUser(HttpContextUtil.getCurrentUser().getUsername());
            protFileMapper.insert(protFile);
        } catch (Exception e){
            log.error("上传文件异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void uploadTemplateFile(ProtFileUploadVo protFileUploadVo) throws IOException {
        try {
            ProtFile protFile = new ProtFile();
            protFile.setName(protFileUploadVo.getFile().getOriginalFilename());
            protFile.setId(GeneratorCodeUtil.generateKey());
            double fileSize = Double.parseDouble(String.valueOf(protFileUploadVo.getFile().getSize())) / 1024;
            BigDecimal b = new BigDecimal(fileSize);
            // 2表示2位 ROUND_HALF_UP表明四舍五入
            protFile.setSize(b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            protFile.setResource(fastDfsUtils.uploadFile(protFileUploadVo.getFile()));
            protFile.setProtId(88888888L);
            protFile.setRemark(protFileUploadVo.getRemark());
            protFile.setCategory(protFileUploadVo.getCategory());
            protFile.setCreateTime(new Date());
            deleteByCategory(protFileUploadVo.getCategory());
            protFileMapper.insert(protFile);
        } catch (Exception e){
            log.error("上传TemplateFile异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteByCategory(String category) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("category", category);
            map.put("is_delete", 0);
            List<ProtFile> protFiles = protFileMapper.selectByMap(map);
            protFileMapper.deleteByMap(map);
            for (ProtFile protFile : protFiles) {
                fastDfsUtils.deleteFile(protFile.getResource());
            }
        } catch (Exception e){
            log.error("删除 deleteByCategory 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ProtFile downloadFileById(Long id) {
        try{
            return protFileMapper.selectById(id);
            // 对文件名进行编码处理中文问题
        } catch (Exception e) {
            log.error("下载文件异常");
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * ProtFile  转  ProtFileDto
     * @param file ProtFile
     * @param fileType String fileType
     * @return ProtFileDto
     */
    private ProtFileDto setProtFileToProtFileDto(ProtFile file, String fileType) {
        ProtFileDto protFileDto = new ProtFileDto();
        protFileDto.setId(file.getId());
        protFileDto.setCreateUser(file.getCreateUser());
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        protFileDto.setCreateTime(date.format(file.getCreateTime()));
        protFileDto.setFileType(fileType);
        protFileDto.setName(file.getName());
        protFileDto.setIsUpload("false");
        protFileDto.setRemark(file.getRemark());
        protFileDto.setCategory(file.getCategory());
        return protFileDto;
    }


}
