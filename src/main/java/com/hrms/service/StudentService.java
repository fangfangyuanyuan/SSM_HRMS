package com.hrms.service;

import com.hrms.bean.Student;
import com.hrms.mapper.StudentMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    public Student selectOneByStuId(String stuId){
        return studentMapper.selectOneByStuId(stuId);
    }
    public List<Student> getStuList(Integer offset,Integer limit){ return studentMapper.selectStuByOffsetAndLimit(offset, limit); }
    public int countStu(){return studentMapper.couStus();}
    public int countStuVaCur(String curId) { return studentMapper.countStuVaCur(curId);}
    public List<Student> getStuListVaTea(String curId) { return studentMapper.getStuListVaTea(curId); }

    public int delStuById(String stuId){return studentMapper.delStuById(stuId);}

    public int insertStu(Student student){return studentMapper.insertStu(student);}

    public int updateStu(String stuId,Student student){return studentMapper.updateStuById(stuId,student);}

    //导入学生信息
    public Integer importStudent(MultipartFile studentFile) throws Exception{

        String fileName = studentFile.getOriginalFilename();
        Workbook workbook = null;

        if(fileName.endsWith(XLS))
            workbook = new HSSFWorkbook(studentFile.getInputStream());
        else if(fileName.endsWith(XLSX))
            workbook = new XSSFWorkbook(studentFile.getInputStream());
        else
            throw new Exception("文件不是Excel文件");

        int sheetCount = workbook.getNumberOfSheets();
        int rows = 0;

        for (int i = 0; i < sheetCount; i++)
        {
            Sheet sheet = workbook.getSheetAt(i);
            rows += sheet.getLastRowNum();

            Row tmp = sheet.getRow(0);
            if (tmp == null)
                continue;

            for (int row = 1; row < rows + 1; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;
                Student stu = new Student();

                //处理学号
                Cell cell = r.getCell(0);
                System.out.println("学号：" + cell.getCellType());
                if(cell == null)
                    continue;
                else{
                    cellValue = String.valueOf(cell.getStringCellValue());
                    if (cellValue.length() == 12){
                        stu.setStuId(cellValue);
                    }
                }

                //处理姓名
                cell = r.getCell(1);
                System.out.println("姓名：" + cell.getCellType());
                if(cell == null)
                    continue;
                else{
                    cellValue = String.valueOf(cell.getStringCellValue());
                    if (cellValue.length() >= 2 && cellValue.length() <= 12){
                        stu.setStuName(cellValue);
                    }
                }

                //处理班级
                cell = r.getCell(2);
                System.out.println("班级：" + cell.getCellType());
                if(cell == null)
                    continue;
                else{
                    Integer stuClass = (int)cell.getNumericCellValue();
                    stu.setStuClass(stuClass);
                }

                //处理专业
                cell = r.getCell(3);
                System.out.println("专业：" + cell.getCellType());
                if(cell == null)
                    continue;
                else{
                    cellValue = String.valueOf(cell.getStringCellValue());
                    if (cellValue.length() == 4){
                        stu.setStuMajor(cellValue);
                    }
                }

                //处理学院
                cell = r.getCell(4);
                System.out.println("学院：" + cell.getCellType());
                if(cell == null)
                    continue;
                else{
                    cellValue = String.valueOf(cell.getStringCellValue());
                    if (cellValue.length() > 3 && cellValue.length() < 20){
                        stu.setStuDept(cellValue);
                    }
                }
                studentMapper.insertStu(stu);
            }
        }
        return rows;
    }


    public List<Student> getStuMajorList() { return studentMapper.getStuMajorList(); }

    public List<Student> getClassByMajor(String stuMajor) { return studentMapper.getClassByMajor(stuMajor);}

    public List<Student> selStuByMajorAndClass(String stuMajor, Integer stuClass) { return studentMapper.selStuByMajorAndClass(stuMajor,stuClass);}

}
