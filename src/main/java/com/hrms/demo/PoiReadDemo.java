//package com.hrms.demo;
//
////import jxl.CellType;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.util.StringUtils;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
//import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
//
//public class PoiReadDemo {
//    public static void main(String[] args) throws IOException {
//
//        String path = "d:/";
//
//        String fileName = "test";
//
//        String fileType = "xlsx";
//
////        writer(path, fileName, fileType);
//
//        read(path, fileName, fileType);
//
//    }
//
//    public static void read(String path,String fileName,String Exception) throws Exception
//
//    {
//
////        InputStream stream = new FileInputStream(path+fileName+"."+fileType);
//        InputStream stream = new FileInputStream("C:\\Users\\me\\Desktop\\需求\\Java课程设计_张三.xlsx");
//        Workbook wb = null;
//        Iterator<Sheet> sheets = null;
//
//        if (fileType.equals("xls")) {
//
//            wb = new HSSFWorkbook(stream);
//        }
//
//        else if (fileType.equals("xlsx")) {
//
//            wb = new XSSFWorkbook(stream);
//        }
//        sheets = wb.iterator();
//        if (sheets == null) {
//            throw new Exception("excel中不含有sheet工作表");
//        }            // 遍历excel里每个sheet的数据。
//         while (sheets.hasNext()) {
//            Sheet sheet = sheets.next();
//            List<Student> list = getCellValue(sheet);
//            returnlist.add(list);
//        }
//
//    }
//
//
//    public static String getCellValue(s){
//        String cellValue = "";
//        if(cell == null){
//            return cellValue;
//        }
//        //把数字当成String来读，避免出现1读成1.0的情况
//        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
//            cell.setCellType(Cell.CELL_TYPE_STRING);
//        }
//        //判断数据的类型
//        switch (cell.getCellType()){
//            case Cell.CELL_TYPE_NUMERIC: //数字
//                cellValue = String.valueOf(cell.getNumericCellValue());
//                break;
//            case Cell.CELL_TYPE_STRING: //字符串
//                cellValue = String.valueOf(cell.getStringCellValue());
//                break;
//            case Cell.CELL_TYPE_BOOLEAN: //Boolean
//                cellValue = String.valueOf(cell.getBooleanCellValue());
//                break;
//            case Cell.CELL_TYPE_FORMULA: //公式
//                cellValue = String.valueOf(cell.getCellFormula());
//                break;
//            case Cell.CELL_TYPE_BLANK: //空值
//                cellValue = "";
//                break;
//            case Cell.CELL_TYPE_ERROR: //故障
//                cellValue = "非法字符";
//                break;
//            default:
//                cellValue = "未知类型";
//                break;
//        }
//        return cellValue;
//    }
//
//    // 获取每一个Sheet工作表中的数。
//    private static List<Student> getCellValue(Sheet sheet) {
//        List<Student> list = new ArrayList<Student>();
//        Student student = new Student();
//        // sheet.getPhysicalNumberOfRows():获取的是物理行数，也就是不包括那些空行（隔行）的情况
//        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
////            Map map = new HashMap<>();            // 获得第i行对象
//            Row row = sheet.getRow(i);
//            if (row == null) {
//                continue;
//            } else {
//                //getLastCellNum：获取列数(比最后一列列标大1)
//                // 通常导入的excel是符合需求规范的，这里因为我们准备的excel列数是5列，如果小于7列说明传入的excel文件有问题。
//                if (row.getLastCellNum() < 5) {
//                    continue;
//                }
//                int j = row.getFirstCellNum();// 获取第i行第一个单元格的下标
//                // 学校名称这一列必然是字符串，所有直接使用getStringCellValue()获取单元格内容
//                String stuId = row.getCell(j++).getStringCellValue();// 学号
//                if (StringUtils.isEmpty(stuId)) {
//                    continue;
//                }
//                student.setStuId(stuId);
//                String stuName = row.getCell(j++).getStringCellValue();// 姓名
//                if (StringUtils.isEmpty(stuName)) {
//                    continue;
//                }
//                student.setStuName(stuName);
//
//                Integer stuClass = (int) row.getCell(j++).getNumericCellValue();// 班级(数据库中，年级是int类型,所以这里我们转换为int)
//                if (StringUtils.isEmpty(stuClass)) {
//                    continue;
//                }
//                student.setStuClass(stuClass);
//
//
//                String stuMajor = row.getCell(j++).getStringCellValue();// 专业
//                if (StringUtils.isEmpty(stuMajor)) {
//                    continue;
//                }
//                student.setStuMajor(stuMajor);
//
//                String stuDept = row.getCell(j++).getStringCellValue();// 专业
//                if (StringUtils.isEmpty(stuDept)) {
//                    continue;
//                }
//                student.setStuDept(stuDept);
//
//
//            }
//            return list;
//        }
//    }
//
