package com.mic.eis.util;

import com.mic.eis.domain.dto.ProtToolPlanDto;
import com.mic.eis.domain.model.ProtMePartList;
import com.mic.eis.domain.model.ProtPilotRunReport;
import com.mic.eis.domain.model.ProtToolTrackList;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author blake.wang
 * @date 2020-09-15 13:57
 */
@Component
@Slf4j
public class ExcelWriteUtils {


    /**
     * 写入 MePartList
     * @param inputStream 输入流
     * @param fileName 文件名
     * @param protMePartLists 数据
     * @param protName 文件名
     * @return Workbook
     */
    public static Workbook writeMePartList(InputStream inputStream, String fileName, List<ProtMePartList> protMePartLists,
                                           String protName){
        try {
            Workbook workbook = null;
            workbook = ExcelReaderUtils.getWorkbook(inputStream, fileName.substring(fileName.lastIndexOf(".") + 1));
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return workbook;
            }
            CellStyle cellStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short)10);
            cellStyle.setFont(font);
            // 填写 Project Name:
            Row row = sheet.createRow(1);
            Cell cell = row.createCell(1);
            cell.setCellValue("Project Name: " +protName);
            // 填写 Approval: 因为 Approval: 这个字串 在Excel中丢失了,是因为填写 Project Name造成的
//            row = sheet.createRow(1);
//            cell = row.createCell(9);
//            cell.setCellValue("Approval: ");
            int rowCount = 4;
            for (ProtMePartList protMePartList : protMePartLists) {
                row = sheet.createRow(rowCount);
                if (protMePartList.getNo() != null){
                    cell = row.createCell(0);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protMePartList.getNo());
                }
                String bomLevel = "";
                if(protMePartList.getBomLevel() != null){
                    if (protMePartList.getBomLevel().equals("0")){
                        cell = row.createCell(1);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protMePartList.getBomLevel());
                    }
                    if (protMePartList.getBomLevel().equals("1")){
                        cell = row.createCell(2);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protMePartList.getBomLevel());
                    }
                    if (protMePartList.getBomLevel().equals("2")){
                        cell = row.createCell(3);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protMePartList.getBomLevel());
                    }
                    if (protMePartList.getBomLevel().equals("3")){
                        cell = row.createCell(4);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protMePartList.getBomLevel());
                        bomLevel = "3";
                    }
                    if (protMePartList.getBomLevel().equals("4")){
                        cell = row.createCell(5);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protMePartList.getBomLevel());
                        bomLevel = "4";
                    }
                }
                if(protMePartList.getPartNumber() != null){
                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protMePartList.getPartNumber());
                }
                if(protMePartList.getPartDescription() != null){
                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyle);
                    if (bomLevel.equals("3")) {
                        cell.setCellValue("  "+protMePartList.getPartDescription());
                    } else if (bomLevel.equals("4")){
                        cell.setCellValue("    "+protMePartList.getPartDescription());
                    } else {
                        cell.setCellValue(protMePartList.getPartDescription());
                    }
                }
                if(protMePartList.getMeterial() != null){
                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protMePartList.getMeterial());
                }
                if(protMePartList.getWeight() != null){
                    cell = row.createCell(9);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protMePartList.getWeight());
                }
                if(protMePartList.getQty() != null){
                    cell = row.createCell(10);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protMePartList.getQty());
                }
                if(protMePartList.getVendor() != null){
                    cell = row.createCell(11);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protMePartList.getVendor());
                }
                if(protMePartList.getUnitPrice() != null){
                    cell = row.createCell(12);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protMePartList.getUnitPrice());
                }
                if(protMePartList.getTotalPrice() != null){
                    cell = row.createCell(13);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protMePartList.getTotalPrice());
                }
                if(protMePartList.getRemark() != null){
                    cell = row.createCell(14);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protMePartList.getRemark());
                }
                rowCount = rowCount +1;
            }
            return workbook;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Workbook writeToolTrackList(InputStream inputStream, String fileName, List<ProtToolTrackList> protToolTrackLists,
                                              String protName, HashMap<String, String> latestTimeUser) {
        try {
            Workbook workbook = null;
            workbook = ExcelReaderUtils.getWorkbook(inputStream, fileName.substring(fileName.lastIndexOf(".") + 1));
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return workbook;
            }
            // 填写 PROJECT NAME:   UPDATE DATE:  UPDATE BY:
            Row row = sheet.createRow(2);
            Cell cell = row.createCell(8);
            cell.setCellValue("UPDATE BY:  " +latestTimeUser.get("lastUser"));
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("PROJECT NAME: " +protName);
            cell = row.createCell(8);
            cell.setCellValue("UPDATE DATE: " +latestTimeUser.get("lastTime"));
            int rowCount = 4;
            String partName = "";
            String partNo = "";
            Integer tx = null;
            String datePlan = "";
            String dateAct = "";
            for (ProtToolTrackList protToolTrackList : protToolTrackLists) {
                row = sheet.createRow(rowCount);
                if (protToolTrackList.getParentId() == 0) { // 主类
                    partName = ""; partNo = ""; tx = null; datePlan = ""; dateAct = "";
                    if (protToolTrackList.getPartName() != null) {
                        partName = protToolTrackList.getPartName();
                    }
                    if (protToolTrackList.getPartNo() != null) {
                        partNo = protToolTrackList.getPartNo();
                    }
                    if (protToolTrackList.getTx() != null) {
                        tx = protToolTrackList.getTx();
                    }
                    if (protToolTrackList.getDatePlan() != null) {
                        datePlan = protToolTrackList.getDatePlan();
                    }
                    if (protToolTrackList.getDateAct() != null) {
                        dateAct = protToolTrackList.getDateAct();
                    }
                    continue;
                } else { // 子类 issue
                    row.createCell(0).setCellValue(partName);
                    row.createCell(1).setCellValue(partNo);
                    if ( tx!= null){
                        row.createCell(2).setCellValue("T"+tx.toString());
                    }
                    row.createCell(10).setCellValue(datePlan);
                    row.createCell(11).setCellValue(dateAct);
                    if (protToolTrackList.getItem() != null) {
                        row.createCell(3).setCellValue(protToolTrackList.getItem());
                    }
                    if (protToolTrackList.getIssueDescription() != null) {
                        row.createCell(4).setCellValue(protToolTrackList.getIssueDescription());
                    }
                    if (protToolTrackList.getRootCause() != null) {
                        row.createCell(5).setCellValue(protToolTrackList.getRootCause());
                    }
                    if (protToolTrackList.getAction() != null) {
                        row.createCell(6).setCellValue(protToolTrackList.getAction());
                    }
                    if (protToolTrackList.getIssuePriority() != null) {
                        row.createCell(7).setCellValue(protToolTrackList.getIssuePriority());
                    }
                    if (protToolTrackList.getStatus() != null) {
                        row.createCell(8).setCellValue(protToolTrackList.getStatus());
                    }
                    if (protToolTrackList.getRemark() != null) {
                        row.createCell(9).setCellValue(protToolTrackList.getRemark());
                    }
                }
                rowCount = rowCount +1;
            }
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Workbook writeToolPlan(InputStream inputStream, String fileName, List<ProtToolPlanDto> protToolPlanDtos,
                                         String protName, String approval, HashMap<String, String> lastTimeUserByVer, String fileVer){
        try {
            Workbook workbook = null;
            workbook = ExcelReaderUtils.getWorkbook(inputStream, fileName.substring(fileName.lastIndexOf(".") + 1));
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            if (sheet == null) { return workbook; }
            // 填写 PROJECT NAME    REV:     Approval :     Prepared : Date:
            Row row = sheet.createRow(1);
            Cell cell = row.createCell(1);
            cell.setCellValue("PROJECT NAME: " + protName);
            cell = row.createCell(15);
            cell.setCellValue("Approval : " + approval);
            cell = row.createCell(19);
            cell.setCellValue("Prepared : " + lastTimeUserByVer.get("lastUser"));
            row = sheet.createRow(2);
            cell = row.createCell(1);
            if (!fileVer.equals("now")) {
                cell.setCellValue("REV:  " + fileVer);
            } else {
                cell.setCellValue("REV:  ");
            }
            cell = row.createCell(19);
            cell.setCellValue("Date :  " + lastTimeUserByVer.get("lastTime"));
            int rowCount = 4;
            for (ProtToolPlanDto protToolPlanDto : protToolPlanDtos) {
                row = sheet.createRow(rowCount);
                if (protToolPlanDto.getNo() != null) {
                    row.createCell(0).setCellValue(protToolPlanDto.getNo());
                }
                if (protToolPlanDto.getPartNo() != null) {
                    row.createCell(1).setCellValue(protToolPlanDto.getPartNo());
                }
                if (protToolPlanDto.getPartDes() != null) {
                    row.createCell(2).setCellValue(protToolPlanDto.getPartDes());
                }
                if (protToolPlanDto.getProEFileName() != null) {
                    row.createCell(3).setCellValue(protToolPlanDto.getProEFileName());
                }
                if (protToolPlanDto.getVersion() != null) {
                    row.createCell(4).setCellValue(protToolPlanDto.getVersion());
                }
                if (protToolPlanDto.getWeight() != null) {
                    if (isNumeric(protToolPlanDto.getWeight())) {
                        row.createCell(5).setCellValue(Double.parseDouble(protToolPlanDto.getWeight()));
                    }
//                    row.createCell(5).setCellValue(protToolPlanDto.getWeight());
                }
                if (protToolPlanDto.getChineseName() != null) {
                    row.createCell(6).setCellValue(protToolPlanDto.getChineseName());
                }
                if (protToolPlanDto.getCav() != null) {
                    row.createCell(7).setCellValue(protToolPlanDto.getCav());
                }
                if (protToolPlanDto.getMaterial() != null) {
                    row.createCell(8).setCellValue(protToolPlanDto.getMaterial());
                }
                if (protToolPlanDto.getColorNo() != null) {
                    row.createCell(9).setCellValue(protToolPlanDto.getColorNo());
                }
                if (protToolPlanDto.getPaintingColorNo() != null) {
                    row.createCell(10).setCellValue(protToolPlanDto.getPaintingColorNo());
                }
                if (protToolPlanDto.getPrintingColorNo() != null) {
                    row.createCell(11).setCellValue(protToolPlanDto.getPrintingColorNo());
                }
                if (protToolPlanDto.getCoatingCategory() != null) {
                    row.createCell(12).setCellValue(protToolPlanDto.getCoatingCategory());
                }
                if (protToolPlanDto.getTextureCategory() != null) {
                    if (protToolPlanDto.getTextureCategory().size() > 0){
                        Cell cell1 = row.createCell(13);
                        cell1.setCellStyle(cellStyle);
                        cell1.setCellValue(new XSSFRichTextString(listToString2(protToolPlanDto.getTextureCategory(), "\n")));
//                        cell1.setCellValue(listToString2(protToolPlanDto.getTextureCategory(), "\r\n"));
                    }
                }
                if (protToolPlanDto.getInsertNutSpec() != null) {
                    if (protToolPlanDto.getInsertNutSpec().size() > 0){
                        Cell cell1 = row.createCell(14);
                        cell1.setCellStyle(cellStyle);
                        cell1.setCellValue(new XSSFRichTextString(listToString2(protToolPlanDto.getInsertNutSpec(), "\n")));
//                        row.createCell(14).setCellValue(listToString2(protToolPlanDto.getInsertNutSpec(), "\n"));
                    }
                }
                if (protToolPlanDto.getUnitPrice() != null) {
                    if (isNumeric(protToolPlanDto.getUnitPrice())) {
                        row.createCell(15).setCellValue(Double.parseDouble(protToolPlanDto.getUnitPrice()));
                    }
//                    row.createCell(15).setCellValue(protToolPlanDto.getUnitPrice());
                }
                if (protToolPlanDto.getQty() != null) {
                    row.createCell(16).setCellValue(protToolPlanDto.getQty());
                }
                if (protToolPlanDto.getToolingVender() != null) {
                    row.createCell(17).setCellValue(protToolPlanDto.getToolingVender());
                }
                if (protToolPlanDto.getToolingPrCost() != null) {
                    row.createCell(18).setCellValue(protToolPlanDto.getToolingPrCost());
                }
                if (protToolPlanDto.getPrNumber() != null) {
                    row.createCell(19).setCellValue(protToolPlanDto.getPrNumber());
                }
                if (protToolPlanDto.getSuppliedVendor() != null) {
                    row.createCell(20).setCellValue(protToolPlanDto.getSuppliedVendor());
                }
                if (protToolPlanDto.getRemark() != null) {
                    row.createCell(21).setCellValue(protToolPlanDto.getRemark());
                }
                if (protToolPlanDto.getChildren().size() > 0){
                    for (ProtToolPlanDto child : protToolPlanDto.getChildren()) {
                        rowCount = rowCount +1;
                        row = sheet.createRow(rowCount);
                        if (child.getProEFileName() != null) {
                            row.createCell(3).setCellValue(child.getProEFileName());
                        }
                        if (child.getVersion() != null) {
                            row.createCell(4).setCellValue(child.getVersion());
                        }
                        if (child.getCav() != null) {
                            row.createCell(7).setCellValue(child.getCav());
                        }
                        if (child.getMaterial() != null) {
                            row.createCell(8).setCellValue(child.getMaterial());
                        }
                        if (child.getWeight() != null) {
                            if (isNumeric(child.getWeight())) {
                                row.createCell(5).setCellValue(Double.parseDouble(child.getWeight()));
                            }
                        }
                        if (child.getChineseName() != null) {
                            row.createCell(6).setCellValue(child.getChineseName());
                        }
                        if (child.getColorNo() != null) {
                            row.createCell(9).setCellValue(child.getColorNo());
                        }
                        if (child.getPaintingColorNo() != null) {
                            row.createCell(10).setCellValue(child.getPaintingColorNo());
                        }
                        if (child.getTextureCategory() != null) {
                            if (child.getTextureCategory().size() > 0){
                                Cell cell1 = row.createCell(13);
                                cell1.setCellStyle(cellStyle);
                                cell1.setCellValue(new XSSFRichTextString(listToString2(child.getTextureCategory(), "\n")));
//                                row.createCell(13).setCellValue(listToString2(child.getTextureCategory(), "\n"));
                            }
                        }
                        if (child.getCoatingCategory() != null) {
                            row.createCell(12).setCellValue(child.getCoatingCategory());
                        }
                        if (child.getInsertNutSpec() != null) {
                            if (child.getInsertNutSpec().size() > 0){
                                Cell cell1 = row.createCell(14);
                                cell1.setCellStyle(cellStyle);
                                cell1.setCellValue(new XSSFRichTextString(listToString2(child.getInsertNutSpec(), "\n")));
//                                row.createCell(14).setCellValue(listToString2(child.getInsertNutSpec(), "\n"));
                            }
                        }
                        if (child.getPrintingColorNo() != null) {
                            row.createCell(11).setCellValue(child.getPrintingColorNo());
                        }
                        if (child.getToolingVender() != null) {
                            row.createCell(17).setCellValue(child.getToolingVender());
                        }
                        if (child.getToolingPrCost() != null) {
                            row.createCell(18).setCellValue(child.getToolingPrCost());
                        }
                        if (child.getUnitPrice() != null) {
                            if (isNumeric(child.getUnitPrice())) {
                                row.createCell(15).setCellValue(Double.parseDouble(child.getUnitPrice()));
                            }
//                            row.createCell(15).setCellValue(child.getUnitPrice());
                        }
                        if (child.getQty() != null) {
                            row.createCell(16).setCellValue(child.getQty());
                        }
                        if (child.getPrNumber() != null) {
                            row.createCell(19).setCellValue(child.getPrNumber());
                        }
                        if (child.getRemark() != null) {
                            row.createCell(21).setCellValue(child.getRemark());
                        }
                        if (child.getSuppliedVendor() != null) {
                            row.createCell(20).setCellValue(child.getSuppliedVendor());
                        }
                    }
                }
                rowCount = rowCount +1;
            }
            return workbook;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public static Workbook writePilotRunReport(InputStream inputStream, String fileName, List<ProtPilotRunReport> protPilotRunReports) {
        try {
            Workbook workbook = null;
            workbook = ExcelReaderUtils.getWorkbook(inputStream, fileName.substring(fileName.lastIndexOf(".") + 1));
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) { return workbook; }
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short)10);
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            Row row = null;
            Cell cell = null;
            int rowCount = 3;
            for (ProtPilotRunReport protPilotRunReport : protPilotRunReports) {
                row = sheet.createRow(rowCount);
                if (protPilotRunReport.getItem() !=null) {
                    cell = row.createCell(0);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(protPilotRunReport.getItem());
                    if (protPilotRunReport.getProblemDes() != null) {
                        cell = row.createCell(1);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getProblemDes());
                    }
                    if (protPilotRunReport.getDateTime() != null) {
                        cell = row.createCell(2);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getDateTime());
                    }
                    if (protPilotRunReport.getFailRate() != null) {
                        cell = row.createCell(3);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getFailRate());
                    }
                    if (protPilotRunReport.getSeverity() != null) {
                        cell = row.createCell(4);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getSeverity());
                    }
                    if (protPilotRunReport.getIssuer() != null) {
                        cell = row.createCell(5);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getIssuer());
                    }
                    if (protPilotRunReport.getOwner() != null) {
                        cell = row.createCell(6);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getOwner());
                    }
                    if (protPilotRunReport.getDesign() != null) {
                        cell = row.createCell(7);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getDesign());
                    }
                    if (protPilotRunReport.getWork() != null) {
                        cell = row.createCell(8);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getWork());
                    }
                    if (protPilotRunReport.getMaterial() != null) {
                        cell = row.createCell(9);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getMaterial());
                    }
                    if (protPilotRunReport.getRootCause() != null) {
                        cell = row.createCell(11);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getRootCause());
                    }
                    if (protPilotRunReport.getAction() != null) {
                        cell = row.createCell(12);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getAction());
                    }
                    if (protPilotRunReport.getDueDate() != null) {
                        cell = row.createCell(13);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getDueDate());
                    }
                    if (protPilotRunReport.getStatus() != null) {
                        cell = row.createCell(14);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(protPilotRunReport.getStatus());
                    }
                    rowCount = rowCount +1;
                }
            }
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 判断字符串是否是数字
     * @param str String
     * @return true false
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        return pattern.matcher(str).matches();
    }

    /**
     * list 转 String， 并用 \n 分割
     * @param list List<String>
     * @param separator "\n"
     * @return  String
     */
    public static String listToString2(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i));
                sb.append(separator);
            }
        }
        return sb.toString();
    }


}
