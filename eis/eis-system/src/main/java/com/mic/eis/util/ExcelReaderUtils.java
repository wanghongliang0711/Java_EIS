package com.mic.eis.util;

import com.mic.eis.domain.model.PartNumber;
import com.mic.eis.domain.model.ProtMePartList;
import com.mic.eis.domain.model.ProtPilotRunReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-16 14:26
 */
@Component
@Slf4j
public class ExcelReaderUtils {

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    /**
     * 日期格式化
     */
//    private static String formatter = "yyyy-MM-dd HH:mm:ss";
    private static String formatter = "yyyy/MM/dd";
    /**
     * 格式化对象
     */
    private static SimpleDateFormat sdf = null;

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     * @param inputStream 读取文件的输入流
     * @param fileType 文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    public static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (fileType.equalsIgnoreCase(XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    public static List<ProtMePartList> readMePartList(MultipartFile file) {
        Workbook workbook = null;
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty() || fileName.lastIndexOf(".") < 0) {
                return null;
            }
            workbook = getWorkbook(file.getInputStream(), fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
            return parseMePartListExcel(workbook);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static List<ProtMePartList> parseMePartListExcel(Workbook workbook) {
        List<ProtMePartList> resultDataList  = new ArrayList<>();
        // 获取第一个sheet
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            return resultDataList;
        }
        int cellCount = 0;  // 获取总列数
        int rowCount = sheet.getPhysicalNumberOfRows();  // 获取总行数
        Row rowTitle = sheet.getRow(3);
        if(rowTitle!=null){
            cellCount = rowTitle.getLastCellNum();
        }
        if (rowCount > 0 && cellCount > 0) {
            for (int rowNum = 4; rowNum < rowCount; rowNum++){
                Row row = sheet.getRow(rowNum);
                if (null == row) {
                    continue;
                }
                ProtMePartList protMePartList = new ProtMePartList();
                String no = convertCellValueToString(row.getCell(0));
                String bomLevel = "";
                String bomLevel0 = convertCellValueToString(row.getCell(1));
                String bomLevel1 = convertCellValueToString(row.getCell(2));
                String bomLevel2 = convertCellValueToString(row.getCell(3));
                String bomLevel3 = convertCellValueToString(row.getCell(4));
                String bomLevel4 = convertCellValueToString(row.getCell(5));
                if (!bomLevel0.equals("")){ bomLevel = bomLevel0; }
                if (!bomLevel1.equals("")){ bomLevel = bomLevel1; }
                if (!bomLevel2.equals("")){ bomLevel = bomLevel2; }
                if (!bomLevel3.equals("")){ bomLevel = bomLevel3; }
                if (!bomLevel4.equals("")){ bomLevel = bomLevel4; }
                String partNum = convertCellValueToString(row.getCell(6));
                String partDes = convertCellValueToString(row.getCell(7));
                String meterial = convertCellValueToString(row.getCell(8));
                String weight = convertCellValueToString(row.getCell(9));
                String qty = convertCellValueToString(row.getCell(10));
                String vendor = convertCellValueToString(row.getCell(11));
                String unitPrice = convertCellValueToString(row.getCell(12));
                String totalPrice = convertCellValueToString(row.getCell(13));
                String remark = convertCellValueToString(row.getCell(14));
                if(no.equals("") && bomLevel.equals("") && partNum.equals("") && partDes.equals("") &&
                        meterial.equals("") && weight.equals("") && qty.equals("") && vendor.equals("") &&
                        unitPrice.equals("") && totalPrice.equals("") && remark.equals("")) {
                } else {
                    if (!no.equals("")){
                        protMePartList.setNo(Integer.valueOf(no));
                    }
                    protMePartList.setBomLevel(bomLevel);
                    if (!partNum.equals("")){
                        protMePartList.setPartNumber(partNum);
                    }
                    if (!partDes.equals("")){
                       protMePartList.setPartDescription(partDes);
                    }
                    if (!meterial.equals("")) {
                       protMePartList.setMeterial(meterial);
                    }
                    protMePartList.setWeight(weight);
                    protMePartList.setQty(qty);
                    protMePartList.setVendor(vendor);
                    protMePartList.setUnitPrice(unitPrice);
                    protMePartList.setTotalPrice(totalPrice);
                    protMePartList.setRemark(remark);
                    resultDataList.add(protMePartList);
                }
            }
        }
        return resultDataList;
    }


    public static List<ProtPilotRunReport> readPilotRunReport(MultipartFile file) {
        Workbook workbook = null;
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty() || fileName.lastIndexOf(".") < 0) {
                return null;
            }
            workbook = getWorkbook(file.getInputStream(), fileName.substring(fileName.lastIndexOf(".") + 1));
            return parsePilotRunReportExcel(workbook);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static List<ProtPilotRunReport> parsePilotRunReportExcel(Workbook workbook) {
        List<ProtPilotRunReport> resultDataList = new ArrayList<>();
        // 获取第一个sheet
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) { return resultDataList; }
        int cellCount = 0;  // 获取总列数
        int rowCount = sheet.getLastRowNum();  // 获取总行数
        Row rowTitle = sheet.getRow(1);
        if(rowTitle!=null){ cellCount = rowTitle.getLastCellNum(); }
        if (rowCount > 0 && cellCount > 0) {
            for (int rowNum = 3; rowNum < rowCount; rowNum++){
                Row row = sheet.getRow(rowNum);
                if (null == row) { continue; }
                String item = convertCellValueToString(row.getCell(0));
                if (isValidInt(item)) {
                    ProtPilotRunReport protPilotRunReport = new ProtPilotRunReport();
                    protPilotRunReport.setItem(Integer.parseInt(item));
                    String problemDes = convertCellValueToString(row.getCell(1));
                    if (!problemDes.equals("")) {
                        protPilotRunReport.setProblemDes(problemDes);
                    }
                    String dateTime = convertCellValueToString(row.getCell(2));
                    if (!dateTime.equals("")) {
                        protPilotRunReport.setDateTime(dateTime);
                    }
                    String severity = convertCellValueToString(row.getCell(4));
                    if (!severity.equals("")) {
                        protPilotRunReport.setSeverity(severity);
                    }
                    String failRate = convertCellValueToString(row.getCell(3));
                    if (!failRate.equals("")) {
//                        protPilotRunReport.setFailRate(failRate);
                        protPilotRunReport.setFailRate(FigureToPercentage(failRate));
                    }
                    String issuer = convertCellValueToString(row.getCell(5));
                    if (!issuer.equals("")) {
                        protPilotRunReport.setIssuer(issuer);
                    }
                    String owner  = convertCellValueToString(row.getCell(6));
                    if (!owner.equals("")) {
                        protPilotRunReport.setOwner(owner);
                    }
                    String design = convertCellValueToString(row.getCell(7));
                    if (!design.equals("")) {
                        protPilotRunReport.setDesign(design);
                    }
                    String work = convertCellValueToString(row.getCell(8));
                    if (!work.equals("")) {
                        protPilotRunReport.setWork(work);
                    }
                    String material = convertCellValueToString(row.getCell(9));
                    if (!material.equals("")) {
                        protPilotRunReport.setMaterial(material);
                    }
                    String rootCause = convertCellValueToString(row.getCell(11));
                    if (!rootCause.equals("")) {
                        protPilotRunReport.setRootCause(rootCause);
                    }
                    String action = convertCellValueToString(row.getCell(12));
                    if (!action.equals("")) {
                        protPilotRunReport.setAction(action);
                    }
                    String dueDate = convertCellValueToString(row.getCell(13));
                    if (!dueDate.equals("")) {
                        protPilotRunReport.setDueDate(dueDate);
                    }
                    String status = convertCellValueToString(row.getCell(14));
                    if (!status.equals("")) {
                        protPilotRunReport.setStatus(status);
                    }
                    resultDataList.add(protPilotRunReport);
                }
            }
        }
        // 判断 item 是否 是 整数，如果不是舍弃，如果是，保存
            // 这里只读取数据，重复的编号不做处理，在 server 端做处理
//        getPhysicalNumberOfRows()获取的是物理行数，也就是不包括那些空行（隔行）的情况。
//        使用 getLastRowNum() 更符合实际
//        getLastRowNum()获取的是最后一行的编号（编号从0开始）。
        return resultDataList;
    }
    /**
     * 读取Excel文件内容
     * @param file 上传的Excel文件
     * @return 读取结果列表，读取失败时返回null
     */
    public static List<PartNumber> readPartNumExcel(MultipartFile file) {
        Workbook workbook = null;
        try {
            // 获取Excel后缀名
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty() || fileName.lastIndexOf(".") < 0) {
                return null;
            }
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            // 获取Excel工作簿
            workbook = getWorkbook(file.getInputStream(), fileType);
            return parsePartNumExcel(workbook);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    private static List<PartNumber> parsePartNumExcel(Workbook workbook) {
        List<PartNumber> resultDataList  = new ArrayList<>();
        // 获取第一个sheet
        Sheet sheet = workbook.getSheetAt(0);
        // 校验sheet是否合法
        if (sheet == null) {
            return resultDataList;
        }
        // 获取总行数
        int rowCount = sheet.getPhysicalNumberOfRows();
        // 获取总列数
        int cellCount = 0;
        Row rowTitle = sheet.getRow(0);
        if(rowTitle!=null){
            cellCount = rowTitle.getLastCellNum(); // 统计到最后一个不为空的位置
        }
        if (rowCount > 0 && cellCount > 0) {
            for (int rowNum = 1; rowNum < rowCount; rowNum++){
                Row row = sheet.getRow(rowNum);
                if (null == row) {
                    continue;
                }
                PartNumber partNumber = new PartNumber();
                Cell cell0 = row.getCell(0);
                Cell cell1 = row.getCell(1);
                String cell0Str = convertCellValueToString(cell0);
                String cell1Str = convertCellValueToString(cell1);
                // PartNumber 和 PartDes 不为空才会被读取 // 如果不这样，可能会引起其他错误，比如获取值为 null
                if (!cell0Str.equals("") && !cell1Str.equals("")){
                    partNumber.setPartNumber(cell0Str);
                    partNumber.setPartDescription(cell1Str);
                    resultDataList.add(partNumber);
                }
            }
        }
        // 从第二行开始获取数据
        return resultDataList;
    }


    private static String convertCellValueToString(Cell cell) {
        if (cell==null){
            return "";
        }
        String returnValue = "";
        switch (cell.getCellType()) {
            case STRING: // 字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:  //布尔
                returnValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:  //空
                break;
            case NUMERIC:  //数字 （日期/普通类型）
                if (HSSFDateUtil.isCellDateFormatted(cell)) { //日期
                    if (sdf == null) {
                        try {
                            sdf = new SimpleDateFormat(formatter);
                        } catch (Exception e) {
                            sdf = new SimpleDateFormat("yyyy/MM/dd");
                        }
                    }
                    returnValue = sdf.format(cell.getDateCellValue());
                } else {
                    long dataL = Math.round(cell.getNumericCellValue());
                    if((dataL-cell.getNumericCellValue()) == 0){
                        DecimalFormat df = new DecimalFormat("0");
                        returnValue = df.format(cell.getNumericCellValue());
                    } else {
                        DecimalFormat df = new DecimalFormat("0.000");
                        returnValue = df.format(cell.getNumericCellValue());
                    }
                }
                break;
            case ERROR:  // 数据类型错误
                break;
            case FORMULA:   // 公式
//                returnValue = cell.getCellFormula();
//                System.out.println(returnValue);
                try {
                    returnValue = cell.getStringCellValue();
                } catch (IllegalStateException e){
                    long dataL1 = Math.round(cell.getNumericCellValue());
                    if((dataL1-cell.getNumericCellValue()) == 0){
                        DecimalFormat df1 = new DecimalFormat("0");
                        returnValue = df1.format(cell.getNumericCellValue());}
                    else {
                        DecimalFormat df1 = new DecimalFormat("0.000");
                        returnValue = df1.format(cell.getNumericCellValue());
                    }
                }
//                System.out.println(returnValue);
                break;
            default:
                break;

        }
        // 去除 字符串前后空格 去除 换行符
        return returnValue.replaceAll("\r|\n", "").trim();
    }


    /**
     * 判断是不是 整数
     * @param value String
     * @return true false
     */
    public static boolean isValidInt(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断是不是 数字
     * @param value String
     * @return true false
     */
    public static boolean isValidDouble(String value) {
        try {
            Double.valueOf(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**Figure to percentage
     * 数字转 百分比 用于： protPilotRunReport.setFailRate(failRate);
     * @param fail_rate String
     * @return String
     */
    public static String FigureToPercentage(String fail_rate) {
        if (fail_rate.contains("%")) {
            return fail_rate;
        } else {
            if (isValidDouble(fail_rate)) {
                double num1 = Double.parseDouble(fail_rate);
                double num2 = 100.0;
                BigDecimal b1 = new BigDecimal(Double.toString(num1));
                BigDecimal b2 = new BigDecimal(Double.toString(num2));
                String str1 = String.valueOf(b1.multiply(b2).doubleValue());
                if (str1.endsWith(".0")) {
                    str1 = str1.substring(0, str1.length()-2);
                }
                return str1 + "%";
            }
        }
        return fail_rate;
    }

}


